package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import thewrestler.WrestlerMod;

import java.util.List;

public class MaintainGrapplePower extends AbstractWrestlerPower implements CloneablePowerInterface {

  public static final String POWER_ID = WrestlerMod.makeID("MaintainGrapplePower");
  public static final String IMG = "maintain_grapple.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  public static final PowerType POWER_TYPE = PowerType.BUFF;

  // TODO: highlight enemy when moused over

  public MaintainGrapplePower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, NAME, IMG, owner, source, amount, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  static public void apply(AbstractCreature source, AbstractCreature owner, int amount,
                           boolean isRefreshOrTargetSwitch) {
    if (source.hasPower(MaintainGrapplePower.POWER_ID)) {
      MaintainGrapplePower powerInstance = (MaintainGrapplePower) source.getPower(MaintainGrapplePower.POWER_ID);

      if (amount > 0) {
        final String flashText = isRefreshOrTargetSwitch ? NAME : DESCRIPTIONS[2];

        //        // THIS ENABLES STACKING OF HP THRESHOLD (if reapplying grapple to the same target). disabling now
        //        if (!isRefreshOrTargetSwitch) {
        //          powerInstance.stackPower(amount);
        //        } else {
        //          powerInstance.amount = amount;
        //        }

        // THIS DISABLES STACKING OF HP THRESHOLD (if reapplying grapple to the same target).
        powerInstance.amount = amount;

        powerInstance.updateDescription();
        powerInstance.flashWithoutSound();
        AbstractDungeon.effectList.add(
            new PowerBuffEffect(source.hb.cX - source.animX, source.hb.cY + source.hb.height / 2.0F, flashText));

      }
    } else {
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(source, owner,
          new MaintainGrapplePower(source, owner, amount), amount));
    }
  }

  public static void clear(AbstractCreature source, AbstractCreature owner) {
    if (source.hasPower(MaintainGrapplePower.POWER_ID)) {
      CardCrawlGame.sound.play("BOUNCE_METALLIC_1");
      AbstractDungeon.actionManager.addToBottom(
          new RemoveSpecificPowerAction(source, owner, MaintainGrapplePower.POWER_ID));
    }
  }

  @Override
  public int onAttackedToChangeDamage(DamageInfo info, int amount) {
    if (amount > 0) {
      AbstractDungeon.actionManager.addToBottom(
          new ReducePowerAction(this.owner, this.owner, this, amount));
    }
    return amount;
  }

  @Override
  public void onRemove() {
      List<AbstractMonster> grappledEnemies = GrappledPower.getGrappledEnemies();
      if (!grappledEnemies.isEmpty()) {
        AbstractMonster m = grappledEnemies.get(0);
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, this.owner, GrappledPower.POWER_ID));
      }
   }

  @Override
  public AbstractPower makeCopy() {
    return new MaintainGrapplePower(owner, source, amount);
  }
}
