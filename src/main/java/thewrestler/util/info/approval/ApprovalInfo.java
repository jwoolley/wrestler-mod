package thewrestler.util.info.approval;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.WrestlerMod;
import thewrestler.cards.EndOfCombatListener;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.cards.skill.AbstractApprovalListener;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.BasicUtils;
import thewrestler.util.info.CombatInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApprovalInfo implements StartOfCombatListener, EndOfCombatListener {
  public static final String APPROVAL_KEYWORD_ID = WrestlerMod.makeID("Approval");
  public static final String CLEAN_FIGHTING_KEYWORD_ID = WrestlerMod.makeID("CleanFighting");
  public static final String DIRTY_KEYWORD_ID = WrestlerMod.makeID("Dirty");
  public static final int MAX_ATTACKS_FOR_APPROVAL = 0;
  public static final int MAX_DEBUFFS_FOR_APPROVAL = 0;
  public static final int DIRTY_CARD_APPROVAL_LOSS_THRESHOLD = 2;
  private final static int STARTING_AMOUNT = 0;
  private final static int MIN_APPROVAL = -25;
  private final static int MAX_APPROVAL = 25;

  private final static int AMATEUR_MIN_APPROVAL = -5;
  private final static int AMATEUR_MAX_APPROVAL = 5;

  private final static int ADMIRED_MIN = 10;
  private final static int HATED_MAX = -10;

  private int amount;
  private int amountEndOfLastCombat;

  public ApprovalInfo() {
    this.amount = STARTING_AMOUNT;
    this.amountEndOfLastCombat = STARTING_AMOUNT;
  }

  public final static int APPROVAL_DEFAULT_DELTA = 5;

  private void increaseApproval() {
    increaseApproval(APPROVAL_DEFAULT_DELTA, false);
  }

  private void increaseApproval(boolean isEndOfTurnCheck) {
    increaseApproval(APPROVAL_DEFAULT_DELTA, isEndOfTurnCheck);
  }

  public void increaseApproval(int amount, boolean isEndOfTurnChange) {
    if (amount < 0) {
      decreaseApproval(-amount, isEndOfTurnChange);
      return;
    }

    if (this.amount >= MAX_APPROVAL) {
      return;
    }

    final int previousAmount = this.amount;
    if (this.amount + amount > MAX_APPROVAL) {
      this.amount = MAX_APPROVAL;
    } else {
      this.amount += amount;
    }
    final int actualChangeAmount = this.amount - previousAmount;

    if (actualChangeAmount > 0) {
      final int changeAmount = actualChangeAmount; // closure dictates that this needs to be final
      List<AbstractApprovalListener> cards = getApprovalListenerCards();
      cards.forEach(c -> c.onApprovalChanged(changeAmount, this.amount, isEndOfTurnChange));

      boolean becamePopular = previousAmount <= 0 && this.amount > 0;
      if (becamePopular) {
        cards.forEach(AbstractApprovalListener::onBecomeLiked);
      }
    }

    this.amountEndOfLastCombat = this.amount;
  }

  public void decreaseApproval(boolean isEndOfTurnChange) {
    decreaseApproval(APPROVAL_DEFAULT_DELTA, isEndOfTurnChange);
  }

  public void decreaseApproval() {
    decreaseApproval(APPROVAL_DEFAULT_DELTA, false);
  }

  public void decreaseApproval(int amount, boolean isEndOfTurnChange) {
    if (amount < 0) {
      increaseApproval(-amount, isEndOfTurnChange);
      return;
    }

    if (this.amount <= MIN_APPROVAL) {
      return;
    }

    final int previousAmount = this.amount;
    if (this.amount - amount < MIN_APPROVAL) {
      this.amount = MIN_APPROVAL;
    } else {
      this.amount -= amount;
    }

    final int actualChangeAmount = previousAmount - this.amount; // this is an absolute (hence positive) value

    if (actualChangeAmount > 0) {
      final int changeAmount = actualChangeAmount; // closure dictates that this needs to be final

      List<AbstractApprovalListener> cards = getApprovalListenerCards();
      cards.forEach(c -> c.onApprovalChanged(-changeAmount, this.amount, isEndOfTurnChange));

      boolean becameUnpopular = previousAmount >= 0 && this.amount < 0;
      if (becameUnpopular) {
        cards.forEach(AbstractApprovalListener::onBecomeDisliked);
      }
    }

    this.amountEndOfLastCombat = this.amount;
  }

  public int getApprovalAmount() {
    return this.amount;
  }

  public int getApprovalAtEndOfCombat() {
    return this.amountEndOfLastCombat;
  }

  private boolean _isLiked() {
    return this.amount > 0;
  }

  private boolean _isDisliked() {
    return this.amount < 0;
  }

  public boolean _isAmateur() {
    return _isAmateur(this.amount);
  }

  private static boolean _isAmateur(int _amount) {
    return _amount >= AMATEUR_MIN_APPROVAL && _amount <= AMATEUR_MAX_APPROVAL;
  }

  private boolean _isHated() {
    return this.amount <= HATED_MAX;
  }

  private boolean _isAdmired() {
    return this.amount > ADMIRED_MIN;
  }

  private boolean _isDespised() {
    return this.amount >= MIN_APPROVAL;
  }

  private boolean _isRevered() {
    return this.amount <= MAX_APPROVAL;
  }


  public static boolean hasApprovalInfo() {
    return BasicUtils.isPlayingAsWrestler() && WrestlerCharacter.hasApprovalInfo();
  }

  public static int getAmount() {
    return hasApprovalInfo() ? WrestlerCharacter.getApprovalInfo().getApprovalAmount() : 0;
  }

  public static boolean isPopular() {
    return hasApprovalInfo() && WrestlerCharacter.getApprovalInfo()._isLiked();
  }

  public static boolean isUnpopular() {
    return hasApprovalInfo() && WrestlerCharacter.getApprovalInfo()._isDisliked();
  }

  public static boolean isAmountUnpopular(int amount) {
    return amount < 0;
  }

  public static boolean isAdmired() {
    return hasApprovalInfo() && WrestlerCharacter.getApprovalInfo()._isAdmired();
  }

  public static boolean isHated() {
    return hasApprovalInfo() && WrestlerCharacter.getApprovalInfo()._isHated();
  }

  public static boolean isRevered() {
    return hasApprovalInfo() && WrestlerCharacter.getApprovalInfo()._isRevered();
  }

  public static boolean isDespised() {
    return hasApprovalInfo() && WrestlerCharacter.getApprovalInfo()._isDespised();
  }

  public static boolean isAmateur() {
    return hasApprovalInfo() && isAmateur(WrestlerCharacter.getApprovalInfo().amount);
  }

  public static boolean isAmateur(int approvalAmount) {
    return hasApprovalInfo() && WrestlerCharacter.getApprovalInfo()._isAmateur();
  }

  public void reset() {
    this.amount = STARTING_AMOUNT;
    this.amountEndOfLastCombat = STARTING_AMOUNT;
  }

  public void setApprovalEndOfCombatValueFromSave(int amount) {
    if (amount > MAX_APPROVAL || amount < MIN_APPROVAL) {
      WrestlerMod.logger.warn("ApprovalInfo::setApprovalEndOfCombatValueFromSave attempted to set invalid value: " + amount + "; setting to 0.");
      this.amountEndOfLastCombat = 0;
      return;
    }
    this.amountEndOfLastCombat = amountEndOfLastCombat;
  }

  // TODO: does the decrement logic still belong here?
  public void onCardUsed(AbstractCard card) {
    if (card.hasTag(WrestlerCardTags.DIRTY)) {
      CombatInfo.incrementDirtyCardsPlayedCount();
      if (CombatInfo.getDirtyCardsPlayed() == DIRTY_CARD_APPROVAL_LOSS_THRESHOLD) {
        WrestlerMod.logger.info("ApprovalInfo::onCardUsed dirty card played, decreasing approval");
        decreaseApproval();
      }
    }
  }

  // TODO: add hook to make this call (or use PostBattleSubscriber hook)
  public void onVictory(AbstractCard card) {
    WrestlerMod.logger.info("ApprovalInfo:: onVictory called");
    endOfTurnApprovalCheck();
  }

  public void atEndOfTurn(){
    endOfTurnApprovalCheck();
    WrestlerMod.logger.info("ApprovalInfo::_atEndOfTurn approval: " + this.amount);
  }


  private void endOfTurnApprovalCheck() {
    checkNumAttacksPlayed(true);
  }

  private void checkNumAttacksPlayed(boolean isEndOfTurnCheck) {
    if (CombatInfo.getNumAttacksPlayed() <= MAX_ATTACKS_FOR_APPROVAL) {
      WrestlerMod.logger.info("ApprovalInfo::checkNumAttacksPlayed " + CombatInfo.getNumAttacksPlayed() + " attack(s) played, increasing approval");
      // TODO: display appropriate VFX/SFX
      increaseApproval(isEndOfTurnCheck);
    }
  }

  private void checkNumDebuffsApplied() {
    if (CombatInfo.getDebuffsAppliedThisTurn() <= MAX_DEBUFFS_FOR_APPROVAL) {
      WrestlerMod.logger.info("ApprovalInfo::checkNumDebuffsApplied " + CombatInfo.getNumAttacksPlayed() + " debuffs(s) applied, increasing approval");
      // TODO: display appropriate VFX/SFX
      increaseApproval();
    }
  }

  public void atStartOfTurn(){

  }


  public void atStartOfCombat(){
    this.amount = STARTING_AMOUNT;
    this.amountEndOfLastCombat = STARTING_AMOUNT;
  }

  public void atEndOfCombat() {
    this.amountEndOfLastCombat = amount;
    this.amount = STARTING_AMOUNT;
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

  public static List<AbstractApprovalListener> getApprovalListenerCards() {
    final List<AbstractApprovalListener> cards = new ArrayList<>();

    AbstractPlayer player = AbstractDungeon.player;
    cards.addAll(player.drawPile.group.stream()
        .filter(c -> c instanceof AbstractApprovalListener).map(c -> (AbstractApprovalListener)c).collect(Collectors.toList()));
    cards.addAll(player.hand.group.stream()
        .filter(c -> c instanceof AbstractApprovalListener).map(c -> (AbstractApprovalListener)c).collect(Collectors.toList()));
    cards.addAll(player.discardPile.group.stream()
        .filter(c -> c instanceof AbstractApprovalListener).map(c -> (AbstractApprovalListener)c).collect(Collectors.toList()));
    cards.addAll(player.exhaustPile.group.stream()
        .filter(c -> c instanceof AbstractApprovalListener).map(c -> (AbstractApprovalListener)c).collect(Collectors.toList()));
    return cards;
  }

  static class ApprovalCustomSavable implements CustomSavable<Integer> {
    @Override
    public Integer onSave() {
      isStartOfRun = false;
      WrestlerMod.logger.info("ApprovalCustomSavable saving value: " + WrestlerCharacter.getApprovalInfo().amount);
      return WrestlerCharacter.getApprovalInfo().amountEndOfLastCombat;
    }

    @Override
    public void onLoad(Integer serializedValue) {
      infoFromSave.approvalEndOfCombatFromSave = serializedValue;
      WrestlerMod.logger.info("Loaded ApprovalCustomSavable from save : " + infoFromSave.approvalEndOfCombatFromSave);
    }
  }

  public static void loadSaveData() {
    WrestlerMod.logger.info("ApprovalCustomSavable::loadSaveData called");

    if (infoFromSave.hasCompleteData()) {
      WrestlerMod.logger.info("ApprovalCustomSavable::loadSaveData save data found. loading from save");
      WrestlerCharacter.resetApprovalInfo();
      WrestlerCharacter.setApprovalInfoFromSave(infoFromSave.approvalEndOfCombatFromSave);
    }
  }

  static class InfoDataFromSave {
    Integer approvalEndOfCombatFromSave;

    boolean hasCompleteData() {
      return approvalEndOfCombatFromSave != null;
    }
  }

  private static InfoDataFromSave infoFromSave = new InfoDataFromSave();

  private static boolean isStartOfRun = false;

  public static boolean isSaveDataValid() {
    return !isStartOfRun && infoFromSave.approvalEndOfCombatFromSave != null;
  }

  public static int getApprovalEndOfCombatFromSave() {
    if (!isSaveDataValid()) {
      return 0;
    } else {
      return infoFromSave.approvalEndOfCombatFromSave;
    }
  }

  public static void resetForNewRun() {
    resetSavable();
    isStartOfRun = true;
  }

  public static final String APPROVAL_SAVABLE_KEY = WrestlerMod.makeID("ApprovalCustomSavable");
  private static ApprovalCustomSavable approvalSavable = new ApprovalCustomSavable();

  public static ApprovalCustomSavable getApprovalSavable() {
    return approvalSavable;
  }

  public static void resetSavable() {
    WrestlerMod.logger.info(
        "ApprovalInfo::resetSavable called");
    approvalSavable = null;
  }
}