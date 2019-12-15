package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;

public class SquaringOffPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("SquaringOffPower");
  public static final String IMG = "squaringoff.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final int TRIGGER_THRESHOLD = 2;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public SquaringOffPower(AbstractCreature owner) {
    this(owner, 0);
  }

  private SquaringOffPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, AbstractDungeon.player, amount, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    final int remainingAttacks = TRIGGER_THRESHOLD - this.amount;
    this.description = DESCRIPTIONS[0] + remainingAttacks + DESCRIPTIONS[1]
        + (remainingAttacks == 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[3])
        + DESCRIPTIONS[4];
  }

  @Override
  public int onAttacked(DamageInfo damageInfo, int amount) {
    if (damageInfo.owner == this.source) {
      this.applyTrigger();
      this.updateDescription();
    }
    return amount;
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
      AbstractDungeon.actionManager.addToBottom(
      new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  private void applyTrigger() {
    super.stackPower(1);
    if (this.amount >= TRIGGER_THRESHOLD) {
      AbstractDungeon.actionManager.addToBottom(
          new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      AbstractDungeon.actionManager.addToBottom(new ApplyGrappledAction(this.owner, this.source));
    }
  }

  @Override
  public AbstractPower makeCopy() {
    return new SquaringOffPower(owner, amount);
  }
}