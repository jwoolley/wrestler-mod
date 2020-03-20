package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.attack.Elbow;
import thewrestler.util.CreatureUtils;
import thewrestler.util.info.CombatInfo;

import java.util.stream.Collectors;

public class FeudPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("FeudPower");
  public static final String IMG = "feud.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public FeudPower(int amount) {
    super(POWER_ID, NAME, IMG, AbstractDungeon.player, AbstractDungeon.player, amount, POWER_TYPE);
  }

  @Override
  public void onAttack(DamageInfo info, int amount, AbstractCreature target) {
    if (target.hasPower(FeudRivalPower.POWER_ID)) {
      AbstractDungeon.actionManager.addToTop(
          new RemoveSpecificPowerAction(target, AbstractDungeon.player, FeudRivalPower.POWER_ID));
      triggerPower();
    } else if (target != AbstractDungeon.player) {
      AbstractDungeon.actionManager.addToTop(
          new ApplyPowerAction(target, AbstractDungeon.player, new FeudRivalPower(target)));

      AbstractDungeon.getCurrRoom().monsters.monsters.stream()
          .filter(mo -> mo.hasPower(FeudRivalPower.POWER_ID) && mo != target)
          .forEach(mo -> AbstractDungeon.actionManager.addToTop(
              new RemoveSpecificPowerAction(mo, AbstractDungeon.player, FeudRivalPower.POWER_ID)));
    }
  }



  @Override
  public void stackPower(int amount) {
    super.stackPower(amount);
    AbstractDungeon.getCurrRoom().monsters.monsters.stream()
        .filter(mo -> mo.hasPower(FeudRivalPower.POWER_ID))
        .forEach(mo ->  {
          final AbstractPower power = mo.getPower(FeudRivalPower.POWER_ID);
          power.flash();
          power.updateDescription();
        });
  }


  @Override
  public void onRemove() {
    CreatureUtils.getLivingMonsters().stream()
        .filter(m -> m.hasPower(FeudRivalPower.POWER_ID))
        .forEach(m -> AbstractDungeon.actionManager.addToTop(
            new RemoveSpecificPowerAction(m, AbstractDungeon.player, FeudRivalPower.POWER_ID)));
  }

  private void triggerPower() {
    flash();

    // trigger N times (instead of gaining N Elbows once) for the effect
    for (int i = 0; i < this.amount; i++) {
      AbstractDungeon.actionManager.addToTop(
          new MakeTempCardInDrawPileAction(new Elbow(), 1, true, true));
    }
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0]
      + this.amount
      + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2])
      + DESCRIPTIONS[3];
  }

  @Override
  public AbstractPower makeCopy() {
    return new FeudPower(amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
