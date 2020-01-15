package thewrestler.util.info.approval;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.cards.skill.AbstractApprovalListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    int actualChangeAmount = amount;
    if (this.amount + amount > MAX_APPROVAL) {
      actualChangeAmount  = MAX_APPROVAL;
    }

    boolean becamePopular = this.amount <= 0 && this.amount + actualChangeAmount > 0;

    this.amount += actualChangeAmount;

    List<AbstractApprovalListener> cards = getApprovalListenerCards();

    if (actualChangeAmount > 0) {
      final int changeAmount = actualChangeAmount; // closure dictates that this needs to be final
      cards.forEach(c -> c.onApprovalChanged(changeAmount, this.amount));

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

    int actualChangeAmount = amount;
    if (this.amount - actualChangeAmount < MIN_APPROVAL) {
      actualChangeAmount  = MIN_APPROVAL;
    }

    boolean becameUnopular = this.amount >= 0 && this.amount - actualChangeAmount < 0;

    this.amount -= actualChangeAmount;

    List<AbstractApprovalListener> cards = getApprovalListenerCards();

    if (actualChangeAmount > 0) {
      final int changeAmount = actualChangeAmount; // closure dictates that this needs to be final
      cards.forEach(c -> c.onApprovalChanged(-changeAmount, this.amount));

      if (becameUnopular) {
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
}