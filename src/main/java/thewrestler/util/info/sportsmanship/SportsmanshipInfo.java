package thewrestler.util.info.sportsmanship;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import thewrestler.WrestlerMod;
import thewrestler.cards.EndOfCombatListener;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.cards.skill.AbstractSportsmanshipListener;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.BasicUtils;
import thewrestler.util.info.CombatInfo;
import thewrestler.util.info.penaltycard.AbstractPenaltyCard;
import thewrestler.util.info.penaltycard.PenaltyCardGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SportsmanshipInfo implements StartOfCombatListener, EndOfCombatListener {
  public static final String SPORTSMANSHIP_KEYWORD_ID = WrestlerMod.makeID("Sportsmanship");
  public static final String DIRTY_KEYWORD_ID = WrestlerMod.makeID("Dirty");

  public static final int DIRTY_CARD_UNSPORTING_INCREASE_THRESHOLD = 2;
  public static final int MAX_DIRTY_CARDS_FOR_UNSPORTING_REDUCTION = 0;
  public final static int MAX_UNSPORTING = 3;

  private final PenaltyCardGroup penaltyCardGroup;

  public SportsmanshipInfo() {
    penaltyCardGroup = new PenaltyCardGroup();
  }

  public PenaltyCardGroup getPenaltyCards() {
    return penaltyCardGroup;
  }

  public void resetUnsporting(boolean isEndOfTurnChange) {
    decreaseUnsporting(this.penaltyCardGroup.size(), isEndOfTurnChange);
  }

  private void increaseUnsporting() {
    increaseUnsporting(1, false);
  }

  private void increaseUnsporting(boolean isEndOfTurnCheck) {
    increaseUnsporting(1, isEndOfTurnCheck);
  }

  public void increaseUnsporting(int amount, boolean isEndOfTurnChange) {
    if (amount < 0) {
      decreaseUnsporting(-amount, isEndOfTurnChange);
      return;
    }

    if (getNumPenaltyCards() >= MAX_UNSPORTING) {
      return;
    }

    final int changeAmount = (getNumPenaltyCards() + amount) >= MAX_UNSPORTING ? MAX_UNSPORTING - getNumPenaltyCards() : amount;

    boolean wasSporting = this._isSporting();



    if (changeAmount > 0) {
      List<AbstractSportsmanshipListener> cards = getUnsportingListenerCards();
      cards.forEach(c -> c.onUnsportingChanged(changeAmount, getNumPenaltyCards(), isEndOfTurnChange));

      AbstractDungeon.actionManager.addToBottom(new RemovePenaltyCardAction(this.penaltyCardGroup, changeAmount));

      boolean becameUnsporting = wasSporting && _isUnsporting();
      if (becameUnsporting) {
        cards.forEach(AbstractSportsmanshipListener::onBecomeUnsporting);
      }
    }
  }

  public void decreaseUnsporting(boolean isEndOfTurnChange) {
    decreaseUnsporting(1, isEndOfTurnChange);
  }

  public void decreaseUnsporting() {
    decreaseUnsporting(1, false);
  }

  private int getNumPenaltyCards() {
    return this.penaltyCardGroup.size();
  }

  public void decreaseUnsporting(int amount, boolean isEndOfTurnChange) {
    if (amount < 0) {
      increaseUnsporting(-amount, isEndOfTurnChange);
      return;
    }

    if (this.penaltyCardGroup.isEmpty()) {
      return;
    }

    final int previousAmount = getNumPenaltyCards();
    final int changeAmount = (previousAmount - amount < 0) ? previousAmount : amount;

    boolean wasSporting = this._isSporting();

    AbstractDungeon.actionManager.addToBottom(new RemovePenaltyCardAction(this.penaltyCardGroup, changeAmount));

    if (changeAmount > 0) {
      List<AbstractSportsmanshipListener> cards = getUnsportingListenerCards();
      cards.forEach(c -> c.onUnsportingChanged(-changeAmount, getNumPenaltyCards(), isEndOfTurnChange));

      boolean becameSporting = !wasSporting && _isSporting();
      if (becameSporting) {
        cards.forEach(AbstractSportsmanshipListener::onBecomeSporting);
      }
    }
  }

  static class RemovePenaltyCardAction extends AbstractGameAction {
    private final static float ACTION_DURATION =  Settings.ACTION_DUR_XFAST;
    private final PenaltyCardGroup penaltyCardGroup;
    public RemovePenaltyCardAction(PenaltyCardGroup penaltyCardGroup, int amount) {
      this.duration = Settings.ACTION_DUR_XFAST;;
      this.actionType = AbstractGameAction.ActionType.SPECIAL;
      this.penaltyCardGroup = penaltyCardGroup;
      this.amount = amount;
    }
    @Override
    public void update() {
      if (this.duration < ACTION_DURATION) {
        penaltyCardGroup.remove();
        if (this.amount > 1) {
          AbstractDungeon.actionManager.addToBottom(
              new RemovePenaltyCardAction(penaltyCardGroup, this.amount - 1));
        }
        this.isDone = true;
        return;
      }
      tickDuration();
    }
  }

  static class AddPenaltyCardAction extends AbstractGameAction {
    private final static float ACTION_DURATION =  Settings.ACTION_DUR_XFAST;
    private final PenaltyCardGroup penaltyCardGroup;
    public AddPenaltyCardAction(PenaltyCardGroup penaltyCardGroup, int amount) {
      this.duration = Settings.ACTION_DUR_XFAST;;
      this.actionType = AbstractGameAction.ActionType.SPECIAL;
      this.penaltyCardGroup = penaltyCardGroup;
      this.amount = amount;
    }
    @Override
    public void update() {
      if (this.duration < ACTION_DURATION) {
        penaltyCardGroup.remove();
        if (this.amount > 1) {
          AbstractDungeon.actionManager.addToBottom(
              new AddPenaltyCardAction(penaltyCardGroup, this.amount - 1));
        }
        this.isDone = true;
        return;
      }
      tickDuration();
    }
  }

  public int getUnsportingAmount() {
    return getNumPenaltyCards();
  }

  public int getUnsportingAtEndOfCombat() {
    return getNumPenaltyCards();
  }

  private boolean _isSporting() {
    return this.penaltyCardGroup.isEmpty();
  }

  private boolean _isUnsporting() {
    return !this.penaltyCardGroup.isEmpty();
  }

  public static boolean hasSportsmanshipInfo() {
    return BasicUtils.isPlayingAsWrestler() && WrestlerCharacter.hasSportsmanshipInfo();
  }

  public static int getAmount() {
    return hasSportsmanshipInfo() ? WrestlerCharacter.getSportsmanshipInfo().getUnsportingAmount() : 0;
  }

  public static boolean isSporting() {
    return hasSportsmanshipInfo() && WrestlerCharacter.getSportsmanshipInfo()._isSporting();
  }

  public static boolean isUnsporting() {
    return hasSportsmanshipInfo() && WrestlerCharacter.getSportsmanshipInfo()._isUnsporting();
  }

  public static boolean isAmountUnpopular(int amount) {
    return amount < 0;
  }

  public void reset() {
    this.penaltyCardGroup.clear();
  }
//
//  public void setUnsportingEndOfCombatValueFromSave(int amount) {
//    if (amount > MAX_UNSPORTING || amount < MIN_UNSPORTING) {
//      WrestlerMod.logger.warn("UnsportingInfo::setUnsportingEndOfCombatValueFromSave attempted to set invalid value: " + amount + "; setting to 0.");
//      this.amountEndOfLastCombat = 0;
//      return;
//    }
//    this.amountEndOfLastCombat = amountEndOfLastCombat;
//  }

  // TODO: does the decrement logic still belong here?
  public void onCardUsed(AbstractCard card) {
    if (card.hasTag(WrestlerCardTags.DIRTY)) {
      CombatInfo.incrementDirtyCardsPlayedCount();
      //      if (CombatInfo.getNumDirtyCardsPlayed() == DIRTY_CARD_UNSPORTING_INCREASE_THRESHOLD) {
      //        WrestlerMod.logger.info("UnsportingInfo::onCardUsed dirty card played, decreasing sportsmanship");
      //        increaseUnsporting();
      //      }
    }
  }

  // TODO: add hook to make this call (or use PostBattleSubscriber hook)
  public void onVictory(AbstractCard card) {
    WrestlerMod.logger.info("UnsportingInfo:: onVictory called");
    endOfTurnUnsportingCheck();
  }

  public void atEndOfTurn(){
    endOfTurnUnsportingCheck();
    WrestlerMod.logger.info("UnsportingInfo::_atEndOfTurn sportsmanship: " + getNumPenaltyCards());
  }


  private void endOfTurnUnsportingCheck() {
    checkNumDirtyCardsPlayed(true);
  }

  private void checkNumDirtyCardsPlayed(boolean isEndOfTurnCheck) {
    if (CombatInfo.getNumDirtyCardsPlayed() >= DIRTY_CARD_UNSPORTING_INCREASE_THRESHOLD) {
      increaseUnsporting(isEndOfTurnCheck);
    }
    else if (CombatInfo.getNumDirtyCardsPlayed() <= MAX_DIRTY_CARDS_FOR_UNSPORTING_REDUCTION) {
      WrestlerMod.logger.info("UnsportingInfo::checkNumDirtyCardsPlayed " + CombatInfo.getNumDirtyCardsPlayed() + " dirty card(s) played, decreaseing sportsmanship");
      // TODO: display appropriate VFX/SFX
      decreaseUnsporting(isEndOfTurnCheck);
    }
  }

  public void atStartOfTurn(){
    penaltyCardGroup.forEach(AbstractPenaltyCard::atStartOfTurn);
  }


  public void atStartOfCombat(){
    penaltyCardGroup.clear();
  }

  public void atEndOfCombat() {
    penaltyCardGroup.clear();
  }

  public static List<AbstractCard> getPlayersDirtyCards() {
    final List<AbstractCard> dirtyCards = new ArrayList<>();

    AbstractPlayer player = AbstractDungeon.player;

    dirtyCards.addAll(player.drawPile.group);
    dirtyCards.addAll(player.hand.group);
    dirtyCards.addAll(player.discardPile.group);
    dirtyCards.addAll(player.exhaustPile.group);

    return dirtyCards;
  }

  public static List<AbstractSportsmanshipListener> getUnsportingListenerCards() {
    final List<AbstractSportsmanshipListener> cards = new ArrayList<>();

    AbstractPlayer player = AbstractDungeon.player;
    cards.addAll(player.drawPile.group.stream()
        .filter(c -> c instanceof AbstractSportsmanshipListener).map(c -> (AbstractSportsmanshipListener)c).collect(Collectors.toList()));
    cards.addAll(player.hand.group.stream()
        .filter(c -> c instanceof AbstractSportsmanshipListener).map(c -> (AbstractSportsmanshipListener)c).collect(Collectors.toList()));
    cards.addAll(player.discardPile.group.stream()
        .filter(c -> c instanceof AbstractSportsmanshipListener).map(c -> (AbstractSportsmanshipListener)c).collect(Collectors.toList()));
    cards.addAll(player.exhaustPile.group.stream()
        .filter(c -> c instanceof AbstractSportsmanshipListener).map(c -> (AbstractSportsmanshipListener)c).collect(Collectors.toList()));
    return cards;
  }

  static class UnsportingCustomSavable implements CustomSavable<Integer> {
    @Override
    public Integer onSave() {
      isStartOfRun = false;
      WrestlerMod.logger.info("UnsportingCustomSavable saving value: " + WrestlerCharacter.getSportsmanshipInfo().getNumPenaltyCards());

     // TODO: need to track the penatlty cards (use enum and key id)
      return WrestlerCharacter.getSportsmanshipInfo().getNumPenaltyCards();
    }

    @Override
    public void onLoad(Integer serializedValue) {
      infoFromSave.unsportingEndOfCombatFromSave = serializedValue;
      WrestlerMod.logger.info("Loaded UnsportingCustomSavable from save : " + infoFromSave.unsportingEndOfCombatFromSave);
    }
  }

  public static void loadSaveData() {
    WrestlerMod.logger.info("UnsportingCustomSavable::loadSaveData called");

    if (infoFromSave.hasCompleteData()) {
      WrestlerMod.logger.info("UnsportingCustomSavable::loadSaveData save data found. loading from save");
      WrestlerCharacter.resetSportsmanshipInfo();
      WrestlerCharacter.setSportsmanShipInfoFromSave(infoFromSave.unsportingEndOfCombatFromSave);
    }
  }

  static class InfoDataFromSave {
    Integer unsportingEndOfCombatFromSave;

    boolean hasCompleteData() {
      return unsportingEndOfCombatFromSave != null;
    }
  }

  private static InfoDataFromSave infoFromSave = new InfoDataFromSave();

  private static boolean isStartOfRun = false;

  public static boolean isSaveDataValid() {
    return !isStartOfRun && infoFromSave.unsportingEndOfCombatFromSave != null;
  }

  public static int getUnsportingEndOfCombatFromSave() {
    if (!isSaveDataValid()) {
      return 0;
    } else {
      return infoFromSave.unsportingEndOfCombatFromSave;
    }
  }

  public static void resetForNewRun() {
    resetSavable();
    isStartOfRun = true;
  }

  public static final String UNSPORTING_SAVABLE_KEY = WrestlerMod.makeID("UnsportingCustomSavable");
  private static UnsportingCustomSavable unsportingSavable = new UnsportingCustomSavable();

  public static UnsportingCustomSavable getUnsportingSavable() {
    return unsportingSavable;
  }

  public static void resetSavable() {
    WrestlerMod.logger.info(
        "UnsportingInfo::resetSavable called");
    unsportingSavable = null;
  }
}