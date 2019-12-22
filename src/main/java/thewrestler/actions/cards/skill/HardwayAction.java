package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.cards.power.WrestlerShackled;
import thewrestler.util.CreatureUtils;

public class HardwayAction extends AbstractGameAction  {
  private static final float DURATION = Settings.ACTION_DUR_XFAST;

  public HardwayAction(AbstractCreature target, AbstractCreature source) {
    setValues(target, source, -1);
    this.actionType = ActionType.DEBUFF;
    this.duration = DURATION;
  }

  public void update() {
    if (this.duration == DURATION) {
      this.amount = CreatureUtils.getDebuffs(this.target).size();
      AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this.target));
      if (this.target.hasPower(WrestlerShackled.POWER_ID)) {
        AbstractDungeon.actionManager.addToBottom(
            new RemoveSpecificPowerAction(this.target, this.source, WrestlerShackled.POWER_ID));
      }
    }
    else {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(this.target, this.source,
              new StrengthPower(this.target, -this.amount), -this.amount, true));
      this.isDone = true;
    }
    this.tickDuration();
  }
}