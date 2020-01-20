package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.powers.WrestlerShackled;
import thewrestler.util.CreatureUtils;

import java.util.Collections;
import java.util.List;

public class ButterflyAction extends AbstractGameAction  {
  private static final float DURATION = Settings.ACTION_DUR_XFAST;

  private int numDebuffsRemoved;

  public ButterflyAction(AbstractCreature target, AbstractCreature source, int amount) {
    setValues(target, source, -1);
    this.actionType = ActionType.ENERGY;
    this.duration = DURATION;
    this.amount = amount;
    this.numDebuffsRemoved = 0;
  }

  public void update() {
    if (this.duration == DURATION) {
      List<AbstractPower> debuffs = CreatureUtils.getDebuffs(this.target);
      if (target.hasPower(WrestlerShackled.POWER_ID)) {
        debuffs.add(target.getPower(WrestlerShackled.POWER_ID));
      }

      this.numDebuffsRemoved = Math.min(debuffs.size(), this.amount);

      Collections.shuffle(debuffs);

      for (int i = 0; i < numDebuffsRemoved; i++) {
        AbstractDungeon.actionManager.addToBottom(
            new RemoveSpecificPowerAction(this.target, this.source, debuffs.get(i)));
      }

      if (numDebuffsRemoved > 0) {
        CardCrawlGame.sound.play("LASER_SHORT_1");
      } else {
        CardCrawlGame.sound.play("MAP_SELECT_3");
      }
    }
    else {
      if (numDebuffsRemoved > 0) {
        CardCrawlGame.sound.play("WING_FLUTTER_1");
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.numDebuffsRemoved));
      }
      this.isDone = true;
    }
    this.tickDuration();
  }
}