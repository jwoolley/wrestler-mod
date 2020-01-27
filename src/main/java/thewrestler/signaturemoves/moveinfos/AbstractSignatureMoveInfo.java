package thewrestler.signaturemoves.moveinfos;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.commons.net.smtp.SMTP;
import thewrestler.WrestlerMod;
import thewrestler.cards.EndOfCombatListener;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.cards.Chokeslam;
import thewrestler.signaturemoves.cards.SignatureMoveCardEnum;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.SerializedSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractSignatureMoveInfo implements StartOfCombatListener, EndOfCombatListener {
  public final boolean isFirstInstance;

  final AbstractSignatureMoveCard signatureMoveCard;
  final SignatureMoveUpgradeList upgradeList;

  public AbstractSignatureMoveInfo(AbstractSignatureMoveCard signatureMoveCard,
                                   SignatureMoveUpgradeList upgradeList, boolean isFirstInstance){
    this.signatureMoveCard = signatureMoveCard.makeCopy();
    this.upgradeList = new SignatureMoveUpgradeList(upgradeList);
    this.isFirstInstance = isFirstInstance;
    this.manuallyTriggeredCardGain = false;
    if (upgradeList != SignatureMoveUpgradeList.NO_UPGRADES) {
      this.signatureMoveCard.applyUpgrades(upgradeList);
    }
  }

  public AbstractSignatureMoveInfo makeCopy() {
    return this.makeCopy(this.upgradeList);
  }

  public AbstractSignatureMoveInfo makeCopy(SignatureMoveUpgradeList upgradeList) {
    try {
      final Constructor<? extends AbstractSignatureMoveInfo> constructor =
          this.getClass().getConstructor(SignatureMoveUpgradeList.class, boolean.class);

      return constructor.newInstance(upgradeList, this.isFirstInstance);
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException("WrestlerMod failed to auto-generate makeCopy for " + this.getClass().getSimpleName()
          + ". Error: " + e);
    }
  }

  public abstract void onCardPlayed(AbstractCard card);
  public abstract void onCardExhausted(AbstractCard card);
  protected abstract void _atStartOfTurn();
  protected abstract void _atEndOfTurn();
  protected abstract void _atStartOfCombat();
  protected abstract void _atEndOfCombat();
  public abstract void upgradeMove(UpgradeType type);
  public abstract void onEnemyGrappled();
  public abstract String getDynamicConditionText();
  public abstract String getStaticConditionText();

  public AbstractSignatureMoveCard getSignatureMoveCard() {
    return this.signatureMoveCard;
  }

  public void triggerGainCard() {
    AbstractSignatureMoveCard card = this.signatureMoveCard.makeCopy();
    card.setCostForTurn(0);
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(this.signatureMoveCard));
  }

  abstract public boolean _canStillTriggerCardGain();

  protected boolean manuallyTriggeredCardGain;
  public void manuallyTriggerCardGain() {
    this.manuallyTriggeredCardGain = true;
    triggerGainCard();
  }

  public void atStartOfTurn() {
    this._atStartOfTurn();
  }

  public void atEndOfTurn() {
    this._atEndOfTurn();
  };

  public void atStartOfCombat() {
    this.manuallyTriggeredCardGain = false;
    this._atStartOfCombat();
  }

  public void atEndOfCombat() {
    this.manuallyTriggeredCardGain = false;
    this._atEndOfCombat();
  }

  public boolean canStillTriggerCardGain() {
    return !this.manuallyTriggeredCardGain && this._canStillTriggerCardGain();
  };

  static class MoveCardCustomSavable implements CustomSavable<Integer> {
    @Override
    public Integer onSave() {
      isStartOfRun = false;
      AbstractSignatureMoveCard card = WrestlerCharacter.getSignatureMoveInfo().signatureMoveCard;
      WrestlerMod.logger.info("MoveCardCustomSavable saving value: " + SignatureMoveCardEnum.getOrdinal(card) + " card: " + card.name);
      return SignatureMoveCardEnum.getOrdinal(WrestlerCharacter.getSignatureMoveInfo().signatureMoveCard);
    }

    @Override
    public void onLoad(Integer serializedValue) {
      if (serializedValue != null) {
        infoFromSave.cardFromSave = SignatureMoveCardEnum.getCardCopy(serializedValue);
        WrestlerMod.logger.info("MoveCardCustomSavable::onLoad Loaded MoveCardCustomSavable from serialized value: "
            + serializedValue + "; card: "
            + (infoFromSave.cardFromSave != null ? infoFromSave.cardFromSave.cardID : " UNKNOWN"));
      } else {
        WrestlerMod.logger.info("MoveCardCustomSavable::onLoad serialized value not found in save data.");
      }
    }
  }

  static class MoveUpgradeCustomSavable implements CustomSavable<Integer> {
    @Override
    public Integer onSave() {
      SignatureMoveUpgradeList list = WrestlerCharacter.getSignatureMoveInfo().upgradeList;
      WrestlerMod.logger.info("MoveUpgradeCustomSavable::onSave saving value: " + list.getSerializedUpgradeList() + " size: " + list.size());
      return list.getSerializedUpgradeList();
    }

    @Override
    public void onLoad(Integer serializedValue) {
      if (serializedValue != null) {
        infoFromSave.upgradeListFromSave = SignatureMoveUpgradeList.listFromSerializedData(serializedValue);

        WrestlerMod.logger.info(
            "MoveCardCustomSavable::onLoad Loaded MoveUpgradeCustomSavable from serialized value: "
                + serializedValue
                + "; num upgrades: "
                + (infoFromSave.upgradeListFromSave != null ? infoFromSave.upgradeListFromSave.size() : " UNKNOWN"));
      }
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
      WrestlerMod.logger.info("AbstractSignatureMoveInfo::hasCompleteData called");

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
      WrestlerMod.logger.info("AbstractSignatureMoveInfo::loadSaveData save data found. loading from save");
      SignatureMoveCardEnum cardEnum = SignatureMoveCardEnum.getEnum(infoFromSave.cardFromSave);
      WrestlerCharacter.setSignatureMoveInfo(cardEnum.getInfoCopy(infoFromSave.upgradeListFromSave));
    }
  }

  private static InfoDataFromSave infoFromSave = new InfoDataFromSave();

  private static boolean isStartOfRun = false;

  public static boolean isSaveDataValid() {
    return !isStartOfRun && hasCompleteSaveData();
  }

  public static void resetForNewRun() {
    resetSavables();
    isStartOfRun = true;
  }

  public static final String SIGNATURE_CARD_SAVABLE_KEY = WrestlerMod.makeID("SignatureCardCustomSavable");
  public static final String SIGNATURE_UPGRADE_SAVABLE_KEY = WrestlerMod.makeID("SignatureUpgradeCustomSavable");
  private static MoveCardCustomSavable cardSavable = new MoveCardCustomSavable();
  private static MoveUpgradeCustomSavable upgradeSavable = new MoveUpgradeCustomSavable();

  public static MoveCardCustomSavable getCardSavable() {
    return cardSavable;
  }
  public static MoveUpgradeCustomSavable getUpgradeSavable() {
    return upgradeSavable;
  }

  public static void resetSavables() {
    WrestlerMod.logger.info(
        "AbstractSignatureMoveInfo::resetSavable called");
    cardSavable = null;
    upgradeSavable = null;
  }
}