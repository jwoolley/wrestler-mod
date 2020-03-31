package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import thewrestler.WrestlerMod;

public class GlowerAction extends AbstractGameAction {
  public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(
      WrestlerMod.makeID("CheapHeatAction")).TEXT;
  final private AbstractPlayer player;

  AbstractCard cardToExhaust;
  public GlowerAction() {
    this.actionType = ActionType.ENERGY;
    this.duration = (this.startDuration = Settings.ACTION_DUR_FAST);
    this.player = AbstractDungeon.player;
    this.cardToExhaust = null;
  }

  public void update() {
    final CardGroup discardCards = AbstractDungeon.player.discardPile;

    if (this.duration == this.startDuration) {
      if (discardCards.isEmpty()) {
        this.isDone = true;
        return;
      } else if (discardCards.size() == 1) {
        this.cardToExhaust = discardCards.group.get(0);
        this.cardToExhaust.lighten(false);
      } else {
        AbstractDungeon.gridSelectScreen.open(discardCards, 1, TEXT[0], false);
      }

      tickDuration();
      return;
    }

    if (this.cardToExhaust != null) {
      this.player.discardPile.moveToExhaustPile(cardToExhaust);

      final AbstractCard cardCopyForEffects = this.cardToExhaust.makeStatEquivalentCopy();
      AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(cardCopyForEffects));
      AbstractDungeon.topLevelEffectsQueue.add(new ExhaustCardEffect(cardCopyForEffects));

      if (this.cardToExhaust.costForTurn == -1) {
        AbstractDungeon.actionManager.addToTop(new GainEnergyAction(EnergyPanel.getCurrentEnergy()));
      } else if (this.cardToExhaust.costForTurn > 0) {
        AbstractDungeon.actionManager.addToTop(new GainEnergyAction(cardToExhaust.costForTurn));
      }
      this.isDone = true;
    } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
      this.cardToExhaust = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
      this.cardToExhaust.lighten(false);
      this.cardToExhaust.unhover();

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