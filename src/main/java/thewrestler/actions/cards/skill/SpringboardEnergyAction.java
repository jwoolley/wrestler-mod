package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.omg.PortableInterceptor.ACTIVE;
import thewrestler.powers.WrestlerShackled;
import thewrestler.util.CreatureUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpringboardEnergyAction extends AbstractGameAction  {
  private static final float DURATION = Settings.ACTION_DUR_FAST;

  List<AbstractCard> handBeforeDraw;
  public SpringboardEnergyAction(List<AbstractCard> handBeforeDraw) {
    setValues(null, AbstractDungeon.player);
    this.actionType = ActionType.DRAW;
    this.duration = DURATION;
    this.handBeforeDraw = new ArrayList<>(handBeforeDraw);
  }

  public void update() {
    if (this.duration == DURATION) {
      List<AbstractCard> cardsInHand = AbstractDungeon.player.hand.group;
      final List<AbstractCard> attacksDrawn = AbstractDungeon.player.hand.group.stream()
          .filter(c -> !handBeforeDraw.contains(c) && c.type == AbstractCard.CardType.ATTACK)
          .collect(Collectors.toList());

      if (!attacksDrawn.isEmpty()) {
        CardCrawlGame.sound.play("SPRINGBOARD_1");
      }
      attacksDrawn.forEach(c -> {
        c.superFlash();
        AbstractDungeon.actionManager.addToTop(new GainEnergyAction(1));
      });
      this.isDone = true;
    }
    this.tickDuration();
  }
}