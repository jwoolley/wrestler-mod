package thewrestler.util.info.approval;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.cards.skill.AbstractApprovalListener;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.BasicUtils;
import thewrestler.util.info.CombatInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApprovalInfo {
  public static final String APPROVAL_KEYWORD_ID = WrestlerMod.makeID("Approval");
  public static final String CLEAN_FIGHTING_KEYWORD_ID = WrestlerMod.makeID("CleanFighting");
  public static final String DIRTY_KEYWORD_ID = WrestlerMod.makeID("Dirty");
  public static final int MAX_ATTACKS_FOR_APPROVAL = 1;
  public static final int MAX_DEBUFFS_FOR_APPROVAL = 0;
  private final static int STARTING_AMOUNT = 0;
  private final static int MIN_APPROVAL = -50;
  private final static int MAX_APPROVAL = 50;
  private int amount;

  private int numDirtyCardsPlayed = 0;

  public ApprovalInfo() {
    this.amount = STARTING_AMOUNT;
  }

  public final static int APPROVAL_DEFAULT_DELTA = 5;

  private void increaseApproval() {
    increaseApproval(APPROVAL_DEFAULT_DELTA);
  }

  public void increaseApproval(int amount) {
    if (amount < 0) {
      decreaseApproval(-amount);
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
      cards.forEach(c -> c.onApprovalChanged(changeAmount, this.amount));

      boolean becamePopular = previousAmount <= 0 && this.amount > 0;
      if (becamePopular) {
        cards.forEach(AbstractApprovalListener::onBecomeLiked);
      }
    }
  }

  public void decreaseApproval() {
    decreaseApproval(APPROVAL_DEFAULT_DELTA);
  }

  public void decreaseApproval(int amount) {
    if (amount < 0) {
      increaseApproval(-amount);
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
      cards.forEach(c -> c.onApprovalChanged(-changeAmount, this.amount));

      boolean becameUnpopular = previousAmount >= 0 && this.amount < 0;
      if (becameUnpopular) {
        cards.forEach(AbstractApprovalListener::onBecomeDisliked);
      }
    }
  }

  public int getApprovalAmount() {
    return this.amount;
  }

  public boolean isLiked() {
    return this.amount > 0;
  }

  public static boolean hasApprovalInfo() {
    return BasicUtils.isPlayingAsWrestler() && WrestlerCharacter.hasApprovalInfo();
  }

  public static int getAmount() {
    return hasApprovalInfo() ? WrestlerCharacter.getApprovalInfo().getApprovalAmount() : 0;
  }


  public static boolean isPopular() {
    return hasApprovalInfo() && WrestlerCharacter.getApprovalInfo().isLiked();
  }

  public static boolean isUnpopular() {
    return hasApprovalInfo() && WrestlerCharacter.getApprovalInfo().isDisliked();
  }

  public boolean isDisliked() {
    return this.amount < 0;
  }

  public void reset() {
    this.amount = 0;
  }

  public void setApprovalValueFromSave(int amount) {
    if (amount > MAX_APPROVAL || amount < MIN_APPROVAL) {
      WrestlerMod.logger.warn("ApprovalInfo::setApprovalValueFromSave attempted to set invalid value: " + amount + "; setting to 0.");
      this.amount = 0;
      return;
    }
    this.amount = amount;
  }

  public void onCardUsed(AbstractCard card) {
    if (card.hasTag(WrestlerCardTags.DIRTY)) {
      WrestlerMod.logger.info("ApprovalInfo::onCardUsed dirty card played, decreasing approval");
      numDirtyCardsPlayed++;
      decreaseApproval();
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
    checkNumAttacksPlayed();
  }

  private void checkIfDirtyCardsPlayed() {
    if (numDirtyCardsPlayed == 0) {
      WrestlerMod.logger.info("ApprovalInfo::checkIfDirtyCardsPlayed no dirty cards played, increasing approval");
      // TODO: display appropriate VFX/SFX
      increaseApproval();
    }
  }

  private void checkNumAttacksPlayed() {
    if (CombatInfo.getNumAttacksPlayed() <= MAX_ATTACKS_FOR_APPROVAL) {
      WrestlerMod.logger.info("ApprovalInfo::checkNumAttacksPlayed " + CombatInfo.getNumAttacksPlayed() + " attack(s) played, increasing approval");
      // TODO: display appropriate VFX/SFX
      increaseApproval();
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
    numDirtyCardsPlayed = 0;
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
      return WrestlerCharacter.getApprovalInfo().amount;
    }

    @Override
    public void onLoad(Integer serializedValue) {
      infoFromSave.approvalFromSave = serializedValue;
      WrestlerMod.logger.info("Loaded ApprovalCustomSavable from save : " + infoFromSave.approvalFromSave);
    }
  }

  public static void loadSaveData() {
    WrestlerMod.logger.info("ApprovalCustomSavable::loadSaveData called");

    if (infoFromSave.hasCompleteData()) {
      WrestlerMod.logger.info("ApprovalCustomSavable::loadSaveData save data found. loading from save");
      WrestlerCharacter.resetApprovalInfo();
      WrestlerCharacter.setApprovalInfoFromSave(infoFromSave.approvalFromSave);
    }
  }

  static class InfoDataFromSave {
    Integer approvalFromSave;

    boolean hasCompleteData() {
      return approvalFromSave != null;
    }
  }

  private static InfoDataFromSave infoFromSave = new InfoDataFromSave();

  private static boolean isStartOfRun = false;

  public static boolean isSaveDataValid() {
    return !isStartOfRun && infoFromSave.approvalFromSave != null;
  }

  public static int getApprovalFromSave() {
    if (!isSaveDataValid()) {
      return 0;
    } else {
      return infoFromSave.approvalFromSave;
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