package thewrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import thewrestler.effects.ReturnCardToDrawPileEffect;
import thewrestler.effects.ShowCardEffect;
import thewrestler.effects.ShowCardFromDrawPileEffect;

public class UpgradeRandomCardInDrawPileAction extends AbstractGameAction {

  final private float POINT_FIVE_CHECKPOINT = ShowCardEffect.EFFECT_DUR / 0.4f + ReturnCardToDrawPileEffect.EFFECT_DUR;
  final private float POINT_EIGHT_CHECKPOINT = ShowCardEffect.EFFECT_DUR / 0.875f + ReturnCardToDrawPileEffect.EFFECT_DUR;

  final private float SECOND_CHECKPOINT_PRIME =  ReturnCardToDrawPileEffect.EFFECT_DUR / 0.85f;
  final private float FULL_EFFECT_DURATION = (ShowCardFromDrawPileEffect.EFFECT_DUR + ReturnCardToDrawPileEffect.EFFECT_DUR);
  final private AbstractCard card;

  public UpgradeRandomCardInDrawPileAction() {
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.duration = FULL_EFFECT_DURATION;
    this.card = getRandomUpgradeableCard(AbstractDungeon.player.drawPile);
  }

  private boolean performedUpgrade = false;
  private boolean startedReturn = false;
  private boolean showedUpgrade = false;

  private AbstractCard showCard;

  public void update() {
    if (this.duration == FULL_EFFECT_DURATION) {
      if (this.card == null) {
        this.isDone = true;
        return;
      }
      upgradeCard(card);
      showCard = card.makeStatEquivalentCopy();
      AbstractDungeon.effectsQueue.add(new ShowCardFromDrawPileEffect(showCard));
    } else if (this.duration < POINT_FIVE_CHECKPOINT && !showedUpgrade) {
      showedUpgrade = true;
      AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
   } else if (this.duration < POINT_EIGHT_CHECKPOINT && !performedUpgrade) {
      performedUpgrade = true;
//      AbstractDungeon.effectsQueue.add(new ShowCardEffect(this.card.makeStatEquivalentCopy()));
    } else if (this.duration < SECOND_CHECKPOINT_PRIME && !startedReturn) {
      startedReturn = true;
      AbstractDungeon.effectsQueue.add(new ReturnCardToDrawPileEffect(showCard));
    } else if (this.duration < 0.0f) {
      this.isDone = true;
      if (!Settings.FAST_MODE) {
        addToTop(new WaitAction(0.25F));
      }
    }
    tickDuration();
  }

  private AbstractCard getRandomUpgradeableCard(CardGroup group) {
    CardGroup upgradables = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    for (AbstractCard c : group.group) {
      if ((c.canUpgrade()) && (c.type != AbstractCard.CardType.STATUS)) {
        upgradables.addToTop(c);
      }
    }
    if (upgradables.size() > 0) {
      upgradables.shuffle();
      return upgradables.getTopCard();
    }
    return null;
  }

  public void upgradeCard(AbstractCard card) {
    card.upgrade();
    card.applyPowers();
  }
}
