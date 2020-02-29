package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FacewashAction extends AbstractGameAction  {
  private static final float DURATION = Settings.ACTION_DUR_XFAST;

  public FacewashAction(int blockAmountPerCard) {
    this.actionType = AbstractGameAction.ActionType.WAIT;
    this.duration = DURATION;
    this.amount = blockAmountPerCard;
  }

  public void update() {
    for (AbstractCard c : DrawCardAction.drawnCards) {
      if (c.type == AbstractCard.CardType.ATTACK) {
        AbstractDungeon.actionManager.addToTop(
            new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.amount));
      }
    }
    this.isDone = true;
  }
}