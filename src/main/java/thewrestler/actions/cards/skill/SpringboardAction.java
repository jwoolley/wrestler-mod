package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.omg.PortableInterceptor.ACTIVE;
import thewrestler.powers.WrestlerShackled;
import thewrestler.util.CreatureUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpringboardAction extends AbstractGameAction  {
  private static final float DURATION = Settings.ACTION_DUR_FAST;

  List<AbstractCard> handBeforeDraw;
  public SpringboardAction(int amount) {
    setValues(null, AbstractDungeon.player, amount);
    this.actionType = ActionType.DRAW;
    this.duration = DURATION;
  }

  public void update() {
    if (this.duration == DURATION) {
      handBeforeDraw = new ArrayList<>(AbstractDungeon.player.hand.group);
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.source, this.amount, false));
      AbstractDungeon.actionManager.addToBottom(new SpringboardEnergyAction(handBeforeDraw));
      this.isDone = true;
    }
    this.tickDuration();
  }
}