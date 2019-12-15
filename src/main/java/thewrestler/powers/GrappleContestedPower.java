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

public class GrappleContestedPower extends AbstractWrestlerPower implements CloneablePowerInterface {

  public static final String POWER_ID = WrestlerMod.makeID("GrappleContestedPower");
  public static final String IMG = "grapple_contested.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  public static final PowerType POWER_TYPE = PowerType.BUFF;

  // TODO: highlight enemy when moused over

  public GrappleContestedPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, NAME, IMG, owner, source, amount, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  static public void applyGrappleContested(AbstractCreature source, AbstractCreature owner, int amount) {
    // TODO: application logic should be in GrappleContestedPower
    if (source.hasPower(GrappleContestedPower.POWER_ID)) {
      GrappleContestedPower contestedPower = (GrappleContestedPower) source.getPower(GrappleContestedPower.POWER_ID);
      contestedPower.amount = amount;
      contestedPower.flash();

      AbstractDungeon.effectList.add(
          new PowerBuffEffect(source.hb.cX - source.animX, source.hb.cY + source.hb.height / 2.0F, NAME));

      contestedPower.updateDescription();
    } else {
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(source, owner,
          new GrappleContestedPower(source, owner, amount), amount));
    }
  }

  public static void clearGrappleContested(AbstractCreature source, AbstractCreature owner) {
    if (source.hasPower(GrappleContestedPower.POWER_ID)) {
      AbstractDungeon.actionManager.addToBottom(
          new RemoveSpecificPowerAction(source, owner, GrappleContestedPower.POWER_ID));
    }
  }

  @Override
  public int onAttackedToChangeDamage(DamageInfo info, int amount) {
    AbstractDungeon.actionManager.addToBottom(
        new ReducePowerAction(this.owner, this.owner, this, amount));
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
    return new GrappleContestedPower(owner, source, amount);
  }
}
