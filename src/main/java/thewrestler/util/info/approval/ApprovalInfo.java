package thewrestler.util.info.approval;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;

public class ApprovalInfo {
  public static final String APPROVAL_KEYWORD_ID = WrestlerMod.makeID("Approval");
  public static final String DIRTY_KEYWORD_ID = WrestlerMod.makeID("Dirty");
  public final static int STARTING_AMOUNT = 0;
  public final static int MIN_APPROVAL = -50;
  public final static int MAX_APPROVAL = 50;
  private int amount;

  private int numDirtyCardsPlayed = 0;

  public ApprovalInfo() {
    this.amount = STARTING_AMOUNT;
  }

  public final static int APPROVAL_DEFAULT_DELTA = 5;

  public void increaseApproval() {
    increaseApproval(APPROVAL_DEFAULT_DELTA);
  }

  public void increaseApproval(int amount) {
    if (amount < 0) {
      decreaseApproval(-amount);
      return;
    }
    if (this.amount + amount > MAX_APPROVAL) {
      this.amount = MAX_APPROVAL;
      return;
    }
    this.amount += amount;
  }

  public void decreaseApproval() {
    decreaseApproval(APPROVAL_DEFAULT_DELTA);
  }

  public void decreaseApproval(int amount) {
    if (amount < 0) {
      increaseApproval(-amount);
      return;
    }
    if (this.amount - amount < MIN_APPROVAL) {
      this.amount = MIN_APPROVAL;
      return;
    }
    this.amount -= amount;
  }

  public int getApprovalAmount() {
    return this.amount;
  }

  public boolean isLiked() {
    return this.amount > 0;
  }

  public boolean isDisliked() {
    return this.amount < 0;
  }

  public void reset() {
    this.amount = 0;
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
    checkIfDirtyCardsPlayed();
  }

  private void checkIfDirtyCardsPlayed() {
    if (numDirtyCardsPlayed == 0) {
      WrestlerMod.logger.info("ApprovalInfo::checkIfDirtyCardsPlayed no dirty cards played, increasing approval");
      // TODO: display appropriate VFX/SFX
      increaseApproval();
    }
  }

  public void atEndOfTurn(){
    checkIfDirtyCardsPlayed();
    WrestlerMod.logger.info("ApprovalInfo::atEndOfTurn approval: " +this.amount);
  }

  public void atStartOfTurn(){
    numDirtyCardsPlayed = 0;
  }
}