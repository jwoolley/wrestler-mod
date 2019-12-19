package thewrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.WrestlerMod;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ChooseAndAddFilteredDiscardCardsToHandAction extends AbstractGameAction {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(
        WrestlerMod.makeID("ChooseAndAddFilteredDiscardCardsToHandAction")).TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional;
    private Predicate<AbstractCard> predicate;
    private final String[] chooserUiStrings;

  public ChooseAndAddFilteredDiscardCardsToHandAction(int numberOfCards, Predicate<AbstractCard> predicate,
                                                      String[] chooserUiStrings, boolean optional) {
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.duration = (this.startDuration = Settings.ACTION_DUR_FAST);
    this.player = AbstractDungeon.player;
    this.numberOfCards = numberOfCards;
    this.optional = optional;
    this.predicate = predicate;
    this.chooserUiStrings = chooserUiStrings;
  }

  public ChooseAndAddFilteredDiscardCardsToHandAction(int numberOfCards) {
    this(numberOfCards, c -> true, TEXT, false);
  }

  public ChooseAndAddFilteredDiscardCardsToHandAction(int numberOfCards, Predicate<AbstractCard> predicate,
                                                      String[] chooserUiStrings) {
    this(numberOfCards, predicate, chooserUiStrings, false);
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

      if ((matchingCards.size() <= this.numberOfCards) && (!this.optional)) {
        cardsToMove = new ArrayList<>(matchingCards.group);
        for (AbstractCard c : cardsToMove) {
          if (this.player.hand.size() < 10) {
            this.player.hand.addToHand(c);
            AbstractDungeon.player.discardPile.removeCard(c);
          }
          c.lighten(false);
        }
        this.isDone = true;
        return;
      }

      final String chooserText = numberOfCards == 1
        ? chooserUiStrings[0]
        : chooserUiStrings[1] + this.numberOfCards + chooserUiStrings[2];

      if (this.numberOfCards == 1) {
        if (this.optional) {
          AbstractDungeon.gridSelectScreen.open(matchingCards, this.numberOfCards, true, chooserText);
        } else {
          AbstractDungeon.gridSelectScreen.open(matchingCards, this.numberOfCards, chooserText, false);
        }
      } else if (this.optional) {
        AbstractDungeon.gridSelectScreen.open(matchingCards, this.numberOfCards, true, chooserText);
      } else {
        AbstractDungeon.gridSelectScreen.open(matchingCards, this.numberOfCards, chooserText, false);
      }
      tickDuration();
      return;
    }
    if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
      for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
        if (this.player.hand.size() < 10) {
          this.player.hand.addToHand(c);
          this.player.discardPile.removeCard(c);
        }
        c.lighten(false);
        c.unhover();
      }
      for (AbstractCard c : this.player.discardPile.group) {
        c.unhover();
        c.target_x = CardGroup.DISCARD_PILE_X;
        c.target_y = 0.0F;
      }
      AbstractDungeon.gridSelectScreen.selectedCards.clear();
      AbstractDungeon.player.hand.refreshHandLayout();
    }
    tickDuration();
  }
}