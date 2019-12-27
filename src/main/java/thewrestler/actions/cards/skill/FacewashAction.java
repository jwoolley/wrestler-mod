package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FacewashAction extends AbstractGameAction  {
  private AbstractPlayer player;
  private static final float DURATION = Settings.ACTION_DUR_XFAST;
  private boolean cardDrawFinished;

  public FacewashAction(int cardDrawAmount) {
    this.source = this.target = this.player = AbstractDungeon.player;
    this.actionType = ActionType.CARD_MANIPULATION;
    this.duration = DURATION;
    this.amount = cardDrawAmount;
  }

  public void update() {
    if (this.duration <= DURATION) {
      if (!cardDrawFinished) {
        AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.source, this.amount, false));
        cardDrawFinished = true;
      } else {
        AbstractDungeon.actionManager.addToBottom(new FacewashDiscardAction());
        this.isDone = true;
      }
    }
    this.tickDuration();
  }
}