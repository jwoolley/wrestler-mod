package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.util.info.CombatInfo;

public class SprainPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("SprainPower");
  public static final String IMG = "sprain.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  private boolean loseSprained;

  public SprainPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
    loseSprained = true;
  }

  @Override
  public void atStartOfTurn() {
    this.flash();
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(this.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS),
            AbstractGameAction.AttackEffect.SMASH, false));

    loseSprained = true;
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    if (!isPlayer && loseSprained || isPlayer && CombatInfo.getNumAttacksPlayed() == 0) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));
    } else {
      this.flashWithoutSound();
    }
  }

  @Override
  public void onAttack(DamageInfo info, int amount, AbstractCreature target) {
    loseSprained = false;
  }

  @Override
  public void updateDescription() {
    this.description = this.owner.isPlayer ?
        (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1])
        : (DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3]);
  }

  @Override
  public AbstractPower makeCopy() {
    return new SprainPower(owner, amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}