package thewrestler.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.actions.power.AbstractMoveRandomCardsFromCardGroupToHandAction;

import java.util.ArrayList;
import java.util.function.Predicate;

public class MoveRandomCardsFromDrawPileToHandAction extends AbstractMoveRandomCardsFromCardGroupToHandAction {
  public MoveRandomCardsFromDrawPileToHandAction(int numberOfCards, Predicate<AbstractCard> predicate) {
    super(numberOfCards, predicate);
  }

  @Override
  public ArrayList<AbstractCard> getAllCardsFromGroup() {
    return AbstractDungeon.player.drawPile.group;
  }

  @Override
  protected void removeCard(AbstractCard card) {
    AbstractDungeon.player.drawPile.removeCard(card);
  }
}