package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.patches.ui.GridSelectScreenShowboatPatch;

public class ShowboatAction  extends AbstractGameAction {
  public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("ShowboatAction")).TEXT;

  private AbstractCard cardToMove;

  public ShowboatAction() {
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.duration = (this.startDuration = Settings.ACTION_DUR_FAST);
  }

  public void update() {
    if (this.duration == this.startDuration) {
      CardGroup discardPile = AbstractDungeon.player.discardPile;

      if ((discardPile.isEmpty())) {
        this.isDone = true;
      } else if ((discardPile.size() == 1)) {
        this.cardToMove = discardPile.group.get(0);
      } else {
        GridSelectScreenShowboatPatch.enableShowboatGrid();
        AbstractDungeon.gridSelectScreen.open(discardPile, 1, TEXT[0], true);
      }
      tickDuration();
      return;
    }
    if (this.cardToMove == null) {
      if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
        this.cardToMove = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
        GridSelectScreenShowboatPatch.disableShowboatGrid();
      }
    } else {
      this.cardToMove.upgrade();
      applyShowboatModifier(this.cardToMove);
      this.cardToMove.freeToPlayOnce = true;
      AbstractDungeon.player.discardPile.removeCard(this.cardToMove);
      AbstractDungeon.player.hand.moveToDeck(this.cardToMove, false);

      AbstractDungeon.gridSelectScreen.selectedCards.clear();
      AbstractDungeon.player.hand.refreshHandLayout();
      this.isDone = true;
    }
    tickDuration();
  }

  public static AbstractCard applyShowboatModifier(AbstractCard card) {
    if (!card.hasTag(WrestlerCardTags.DIRTY)) {
      card.tags.add(WrestlerCardTags.DIRTY);
      card.rawDescription = TEXT[1] + card.rawDescription;
      card.initializeDescription();
    }
    return  card;
  }
}