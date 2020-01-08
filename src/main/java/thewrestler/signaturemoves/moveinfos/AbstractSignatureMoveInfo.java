package thewrestler.signaturemoves.moveinfos;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.WrestlerMod;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.cards.SignatureMoveCardEnum;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.SerializedSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractSignatureMoveInfo {
  public final boolean isFirstInstance;

  final AbstractSignatureMoveCard signatureMoveCard;
  final SignatureMoveUpgradeList upgradeList;

  public AbstractSignatureMoveInfo(AbstractSignatureMoveCard signatureMoveCard,
                                   SignatureMoveUpgradeList upgradeList, boolean isFirstInstance){
    this.signatureMoveCard = signatureMoveCard.makeCopy();
    this.upgradeList = new SignatureMoveUpgradeList(upgradeList);
    this.isFirstInstance = isFirstInstance;

    if (upgradeList != SignatureMoveUpgradeList.NO_UPGRADES) {
      this.signatureMoveCard.applyUpgrades(upgradeList);
    }

    cardSavable = new MoveCardCustomSavable();
    upgradeSavable = new MoveUpgradeCustomSavable();
  }

  public AbstractSignatureMoveInfo makeCopy() {
    return this.makeCopy(this.upgradeList);
  }

  public AbstractSignatureMoveInfo makeCopy(SignatureMoveUpgradeList upgradeList) {
    try {
      final Constructor<? extends AbstractSignatureMoveInfo> constructor =
          this.getClass().getConstructor(SignatureMoveUpgradeList.class, boolean.class);

      return constructor.newInstance(this.upgradeList, this.isFirstInstance);
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException("WrestlerMod failed to auto-generate makeCopy for " + this.getClass().getSimpleName()
          + ". Error: " + e);
    }
  }

  public abstract void onCardPlayed(AbstractCard card);
  public abstract void atStartOfTurn();
  public abstract void atEndOfTurn();
  public abstract void atStartOfCombat();
  public abstract void atEndOfCombat();
  public abstract void upgradeMove(UpgradeType type);
  public abstract void onEnemyGrappled();
  public abstract String getDynamicConditionText();
  public abstract String getStaticConditionText();

  public static final String SIGNATURE_CARD_SAVABLE_KEY = WrestlerMod.makeID("SignatureCardCustomSavable");
  public static final String SIGNATURE_UPGRADE_SAVABLE_KEY = WrestlerMod.makeID("SignatureUpgradeCustomSavable");

  private MoveCardCustomSavable cardSavable;
  private MoveUpgradeCustomSavable upgradeSavable;

  public AbstractSignatureMoveCard getSignatureMoveCard() {
    return this.signatureMoveCard;
  }

  public void triggerGainCard() {
    AbstractSignatureMoveCard card = this.signatureMoveCard.makeCopy();
    card.setCostForTurn(0);
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(this.signatureMoveCard));
  }

  abstract public boolean canStillTriggerCardGain();

  public void registerSaves() {
    BaseMod.addSaveField(SIGNATURE_CARD_SAVABLE_KEY, this.cardSavable);
    BaseMod.addSaveField(SIGNATURE_UPGRADE_SAVABLE_KEY, this.upgradeSavable);
  }

  class MoveCardCustomSavable implements CustomSavable<Integer> {
    @Override
    public Integer onSave() {
      WrestlerMod.logger.info("MoveCardCustomSavable saving value: " + SignatureMoveCardEnum.getOrdinal(signatureMoveCard));
      return SignatureMoveCardEnum.getOrdinal(signatureMoveCard);
    }

    @Override
    public void onLoad(Integer serializedValue) {
      infoFromSave.cardFromSave = SignatureMoveCardEnum.getCardCopy(serializedValue);
      WrestlerMod.logger.info("Loaded MoveCardCustomSavable from serialized value: " + serializedValue
          + "; card: " + (infoFromSave.cardFromSave != null ? infoFromSave.cardFromSave.cardID : " UNKNOWN"));
    }
  }

  class MoveUpgradeCustomSavable implements CustomSavable<Integer> {
    @Override
    public Integer onSave() {
      WrestlerMod.logger.info("MoveUpgradeCustomSavable saving value: " + upgradeList.getSerializedUpgradeList());
      return upgradeList.getSerializedUpgradeList();
    }

    @Override
    public void onLoad(Integer serializedValue) {
      infoFromSave.upgradeListFromSave = SignatureMoveUpgradeList.listFromSerializedData(serializedValue);

      WrestlerMod.logger.info(
          "Loaded MoveUpgradeCustomSavable from serialized value: "
          + serializedValue
          + "; num upgrades: "
          + (infoFromSave.upgradeListFromSave != null ? infoFromSave.upgradeListFromSave.size() : " UNKNOWN"));
    }
  }

  static public boolean hasCompleteSaveData() {
    return infoFromSave.hasCompleteData();
  }

  // TODO: move to SignatureMoveInfoLoader (or remove that class)
  static class InfoDataFromSave {
    AbstractSignatureMoveCard cardFromSave;
    SignatureMoveUpgradeList upgradeListFromSave;

    boolean hasCompleteData() {
      if (cardFromSave != null && SignatureMoveCardEnum.getEnum(cardFromSave) == null) {
        WrestlerMod.logger.warn(
            "AbstractSignatureMoveInfo::hasCompleteData unable to find card enum for " + cardFromSave.cardID);
        return false;
      }
      return cardFromSave != null && upgradeListFromSave != null;
    }
  }

  public static void loadSaveData() {
    WrestlerMod.logger.info("AbstractSignatureMoveInfo::loadSaveData called");

    if (infoFromSave.hasCompleteData()) {
      // TODO: load save data into WrestlerCharacter.signatureMoveInfo
      SignatureMoveCardEnum cardEnum = SignatureMoveCardEnum.getEnum(infoFromSave.cardFromSave);
      WrestlerCharacter.setSignatureMoveInfo(cardEnum.getInfoCopy(infoFromSave.upgradeListFromSave));
    }
  }

  static InfoDataFromSave infoFromSave = new InfoDataFromSave();
}