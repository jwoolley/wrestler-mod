package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import thewrestler.WrestlerMod;

public class SideRollAction extends AbstractGameAction  {
  private static final UIStrings uiStrings =
      CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("SideRollAction"));
  public static final String[] TEXT = uiStrings.TEXT;
  private AbstractPlayer player;
  private static final float DURATION = Settings.ACTION_DUR_XFAST;

  private AbstractCard selectedCard;
  private boolean cardSelectionFinished;

  public SideRollAction() {
    this.source = this.target = this.player = AbstractDungeon.player;
    this.actionType = AbstractGameAction.ActionType.DISCARD;
    this.duration = DURATION;
  }

  public void update() {
    if (this.duration == DURATION) {
      if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
        this.isDone = true;
        return;
      }
      if (this.player.hand.size() <= 1) {
        if (this.player.hand.size() == 1) {
          AbstractCard c = this.player.hand.getTopCard();
          selectedCard = c;
          this.player.hand.moveToDiscardPile(c);
          c.triggerOnManualDiscard();
          GameActionManager.incrementDiscard(false);
        }
        AbstractDungeon.player.hand.applyPowers();
        cardSelectionFinished = true;
        tickDuration();
        return;
      }
      if (this.player.hand.size() > 1) {
        AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
      }
      AbstractDungeon.player.hand.applyPowers();
      cardSelectionFinished = true;
      tickDuration();
      return;
    }
    else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
      // TODO: GET 1ST SELECTED CARD ONLY
      for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
        cardSelectionFinished = true;
        selectedCard = c;
        this.player.hand.moveToDiscardPile(c);
        c.triggerOnManualDiscard();
        GameActionManager.incrementDiscard(false);
      }
      AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
    } else if (cardSelectionFinished) {
      if (selectedCard != null && selectedCard.type == AbstractCard.CardType.ATTACK) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.player, 1));
      }
      this.isDone = true;
      this.tickDuration();
      return;
    }
    tickDuration();
  }
}
