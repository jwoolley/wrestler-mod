package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FacewashDiscardAction extends AbstractGameAction  {
  private AbstractPlayer player;
  private static final float DURATION = Settings.ACTION_DUR_XFAST;

  private AbstractCard discardedCard;
  private boolean discardFinished;
  private boolean isAttackCard;

  private AbstractMonster targetMonster;

  public FacewashDiscardAction() {
    this.source = this.target = this.player = AbstractDungeon.player;
    this.actionType = ActionType.DISCARD;
    this.duration = DURATION;
  }

  public void update() {
    if (this.duration <= DURATION) {
      if (!discardFinished) {
        final CardGroup hand = this.player.hand;
        if (hand.size() <= 1) {
          if (hand.size() == 1) {
            AbstractCard c = this.player.hand.getTopCard();
            discardedCard = c;
          }
        } else {
          discardedCard = hand.group.get(AbstractDungeon.cardRandomRng.random(0, hand.size() - 1));
        }
        if (discardedCard != null) {
          discardedCard.freeToPlayOnce = true;
          if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.targetMonster = AbstractDungeon.getCurrRoom().monsters
                .getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (discardedCard.type == AbstractCard.CardType.ATTACK && targetMonster != null
                && discardedCard.canUse(AbstractDungeon.player, this.targetMonster)) {
              isAttackCard = true;
              AbstractDungeon.player.limbo.group.add(discardedCard);
            } else {
              discardedCard.freeToPlayOnce = false;
              AbstractDungeon.actionManager.addToTop(
                  new DiscardSpecificCardAction(discardedCard, AbstractDungeon.player.hand));
              AbstractDungeon.actionManager.addToTop(new WaitAction(0.4f));
            }
          }
          discardedCard.current_y = (-200.0F * Settings.scale);
          discardedCard.target_x = (Settings.WIDTH / 2.0F + 200.0F * Settings.scale);
          discardedCard.target_y = (Settings.HEIGHT / 2.0F);
          discardedCard.targetAngle = 0.0F;
          discardedCard.lighten(false);
          discardedCard.drawScale = 0.12F;
          discardedCard.targetDrawScale = 0.75F;
        }
        discardFinished = true;
      } else {
        if (isAttackCard) {
          discardedCard.applyPowers();
          AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(discardedCard, targetMonster, discardedCard.energyOnUse, true));
          AbstractDungeon.actionManager.addToTop(new UnlimboAction(discardedCard));
          if (!Settings.FAST_MODE) {
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
          } else {
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
          }
        }
        this.isDone = true;
      }
    }
    this.tickDuration();
  }
}