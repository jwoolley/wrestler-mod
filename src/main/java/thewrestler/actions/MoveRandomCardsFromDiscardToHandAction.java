package thewrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.Predicate;

public class MoveRandomCardsFromDiscardToHandAction extends AbstractGameAction {
  final private AbstractPlayer player;
  final private int numberOfCards;
  final private Predicate<AbstractCard> predicate;

  public MoveRandomCardsFromDiscardToHandAction(int numberOfCards, Predicate<AbstractCard> predicate) {
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.duration = (this.startDuration = Settings.ACTION_DUR_FAST);
    this.player = AbstractDungeon.player;
    this.numberOfCards = numberOfCards;
    this.predicate = predicate;
  }

  private static ArrayList<AbstractCard> getDiscardCards() {
    return AbstractDungeon.player.discardPile.group;
  }

  private CardGroup getMatchingCards() {
    final CardGroup matchingCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    getDiscardCards().stream().filter(c -> this.predicate.test(c)).forEach(c -> matchingCards.addToTop(c));
    return matchingCards;
  }

  public void update() {
    final CardGroup matchingCards = getMatchingCards();

    ArrayList<AbstractCard> cardsToMove;

    if (this.duration == this.startDuration) {
      if ((matchingCards.isEmpty()) || (this.numberOfCards <= 0)) {
        this.isDone = true;
        return;
      }

      if ((matchingCards.size() <= this.numberOfCards)) {
        cardsToMove = new ArrayList<>(matchingCards.group);
      } else {
        cardsToMove = new ArrayList<>();
        ArrayList<Integer> cardToMoveIndexes = new ArrayList<>();
        for (int i = 0; i < this.numberOfCards; i++) {
          int index = AbstractDungeon.miscRng.random(0,matchingCards.size() - 1);
          if (!cardToMoveIndexes.contains(index)) {
            cardsToMove.add(matchingCards.group.get(index));
            cardToMoveIndexes.add(index);
          } else {
            i--;
          }
        }
      }

      for (AbstractCard c : cardsToMove) {
        if (this.player.hand.size() < 10) {
          this.player.hand.addToHand(c);
          AbstractDungeon.player.discardPile.removeCard(c);
        }
        c.lighten(false);
      }
      this.isDone = true;
      tickDuration();
    }
  }
}