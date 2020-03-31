package thewrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.actions.power.AbstractMoveRandomCardsFromCardGroupToHandAction;

import java.util.ArrayList;
import java.util.function.Predicate;

public class MoveRandomCardsFromDiscardToHandAction extends AbstractMoveRandomCardsFromCardGroupToHandAction {
  public MoveRandomCardsFromDiscardToHandAction(int numberOfCards, Predicate<AbstractCard> predicate) {
    super(numberOfCards, predicate);
  }

  @Override
  public ArrayList<AbstractCard> getAllCardsFromGroup() {
    return AbstractDungeon.player.discardPile.group;
  }

  @Override
  protected void removeCard(AbstractCard card) {
    AbstractDungeon.player.discardPile.removeCard(card);
  }
}