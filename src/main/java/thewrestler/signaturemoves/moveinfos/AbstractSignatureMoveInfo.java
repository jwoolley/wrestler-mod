package thewrestler.signaturemoves.moveinfos;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.WrestlerMod;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.cards.SignatureMoveCardEnum;
import thewrestler.signaturemoves.upgrades.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractSignatureMoveInfo implements AbstractSignatureMoveInfoInterface {
  public final boolean isFirstInstance;

  public static final boolean SIGNATURE_MOVES_ENABLED = true;

  final AbstractSignatureMoveCard signatureMoveCard;
  final SignatureMoveUpgradeList upgradeList;
  final UpgradeGroup eligibleUpgradeGroup;
  UpgradeGroup currentUpgradeGroup;

  public AbstractSignatureMoveInfo(AbstractSignatureMoveCard signatureMoveCard,
                                   SignatureMoveUpgradeList upgradeList, boolean isFirstInstance){
    this.signatureMoveCard = signatureMoveCard.makeCopy();
    this.upgradeList = new SignatureMoveUpgradeList(upgradeList);
    this.eligibleUpgradeGroup = signatureMoveCard.getAllEligibleUpgrades();
    currentUpgradeGroup = new UpgradeGroup();
    this.isFirstInstance = isFirstInstance;
    this.resetForNewCombat();
    if (upgradeList != SignatureMoveUpgradeList.NO_UPGRADES) {
      this.signatureMoveCard.applyUpgrades(upgradeList);
    }
  }

  @Override
  public void upgradeMove(UpgradeType type, UpgradeRarity rarity) {
    // TODO: increment upgrade of specified type (not 1)
    this.upgradeList.add(new AbstractSignatureMoveUpgrade(type,1, rarity));

    if (type == UpgradeType.COST_REDUCTION) {
      AbstractSignatureMoveCard card = WrestlerCharacter.getSignatureMoveInfo().getSignatureMoveCardReference();
      if (card.cost > 0) {
        card.upgradeCost(card.cost - 1);
        card.name = "Standing " + card.name;
        card.upgraded = true;
        card.reinitialize();
      }
    }
  }

  public void triggerGainTrademarkMove() {
    this.triggerGainCard();
  }

  public abstract boolean gainedCardThisCombat();
  public abstract void flagCardAsGained();
  public abstract void resetForNewCombat();

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

  protected abstract void _atStartOfTurn();
  protected abstract void _atEndOfTurn();
  protected abstract void _atStartOfCombat();
  protected abstract void _atEndOfCombat();

  @Override
  public AbstractSignatureMoveCard getSignatureMoveCardReference() {
    return this.signatureMoveCard;
  }

  @Override
  public SignatureMoveUpgradeList getUpgradeList() {
    return this.upgradeList;
  }


  public void triggerGainCard() {
    if (this.canStillTriggerCardGain()) {
      triggerGainCard(false,false);
    }
  }

  protected void triggerGainCard(boolean toDeck, boolean onTop) {
    AbstractSignatureMoveCard card = this.signatureMoveCard.makeCopy();
    card.setCostForTurn(0);

    if (toDeck) {
      AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(this.signatureMoveCard, 1, !onTop, onTop));
    } else {
      AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(this.signatureMoveCard));
    }

    this.flagCardAsGained();
  }

  abstract protected boolean _canStillTriggerCardGain();

  public void manuallyTriggerCardGain(boolean toDeck, boolean onTop) {
    triggerGainCard(toDeck, onTop);
  }

  public void atStartOfTurn() {
    this._atStartOfTurn();
  }

  public void atEndOfTurn() {
    this._atEndOfTurn();
  };

  public void atStartOfCombat() {
    this.resetForNewCombat();
    this._atStartOfCombat();
  }

  public void atEndOfCombat() {
    this.resetForNewCombat();
    this._atEndOfCombat();
  }

  @Override
  public boolean canStillTriggerCardGain() {
    return !this.gainedCardThisCombat() && this._canStillTriggerCardGain();
  }

  static class MoveCardCustomSavable implements CustomSavable<Integer> {
    @Override
    public Integer onSave() {
      isStartOfRun = false;
      AbstractSignatureMoveCard card = WrestlerCharacter.getSignatureMoveInfo().getSignatureMoveCardReference();
      WrestlerMod.logger.info("MoveCardCustomSavable saving value: " + SignatureMoveCardEnum.getOrdinal(card) + " card: " + card.name);
      return SignatureMoveCardEnum.getOrdinal(WrestlerCharacter.getSignatureMoveInfo().getSignatureMoveCardReference());
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
      SignatureMoveUpgradeList list = WrestlerCharacter.getSignatureMoveInfo().getUpgradeList();
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

    if (SIGNATURE_MOVES_ENABLED && infoFromSave.hasCompleteData()) {
      // TODO: load save data into WrestlerCharacter.signatureMoveInfo
      WrestlerMod.logger.info("AbstractSignatureMoveInfo::loadSaveData save data found. loading from save");
      SignatureMoveCardEnum cardEnum = SignatureMoveCardEnum.getEnum(infoFromSave.cardFromSave);
      WrestlerCharacter.setSignatureMoveInfo(cardEnum.getInfoCopy(infoFromSave.upgradeListFromSave));
    }
  }

  private static InfoDataFromSave infoFromSave = new InfoDataFromSave();

  private static boolean isStartOfRun = false;

  public static boolean isSaveDataValid() {
    return SIGNATURE_MOVES_ENABLED && !isStartOfRun && hasCompleteSaveData();
  }

  public static void resetForNewRun() {
    if (SIGNATURE_MOVES_ENABLED) {
      resetSavables();
      isStartOfRun = true;
    }
  }

  public static final String SIGNATURE_CARD_SAVABLE_KEY = WrestlerMod.makeID("SignatureCardCustomSavable");
  public static final String SIGNATURE_UPGRADE_SAVABLE_KEY = WrestlerMod.makeID("SignatureUpgradeCustomSavable");

  private static MoveCardCustomSavable createCardCustomSavable() {
    return SIGNATURE_MOVES_ENABLED ? new MoveCardCustomSavable() : null;
  }

  private static MoveUpgradeCustomSavable createUpgradeCustomSavable() {
    return SIGNATURE_MOVES_ENABLED ? new MoveUpgradeCustomSavable() : null;
  }

  private static MoveCardCustomSavable cardSavable = createCardCustomSavable();
  private static MoveUpgradeCustomSavable upgradeSavable = createUpgradeCustomSavable();

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