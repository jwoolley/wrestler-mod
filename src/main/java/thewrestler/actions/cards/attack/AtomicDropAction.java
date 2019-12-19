package thewrestler.actions.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import org.apache.logging.log4j.Logger;
import thewrestler.WrestlerMod;
import thewrestler.powers.IrradiatedPower;

public class AtomicDropAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  private final boolean useFastMode;
  private boolean secondTick;
  private final int damage;

  public AtomicDropAction(AbstractCreature target, AbstractCreature source, int damage, int powerStacks, boolean useFastMode) {
    this.actionType = ActionType.DAMAGE;
    this.target = target;
    this.source = source;

    this.damageType = DamageInfo.DamageType.NORMAL;
    this.damage = damage;
    this.amount = powerStacks;

    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
    this.useFastMode = useFastMode;

    secondTick = false;
  }

  @Override
  public void update() {
    if (!this.secondTick) {
      AbstractDungeon.effectList.add(
          new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY));

      AbstractDungeon.actionManager.addToTop(new SFXAction("BOOM_LOWFREQ_1"));

      AbstractDungeon.actionManager.addToBottom(
          new DamageAction(this.target, new DamageInfo(this.source, this.damage, this.damageType),
              AbstractGameAction.AttackEffect.NONE));

      this.secondTick = true;
    } else if (useFastMode || this.duration <= ACTION_DURATION) {
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(target, source, new IrradiatedPower(target, this.amount), this.amount));
      this.isDone = true;
    }
    this.tickDuration();
  }
}