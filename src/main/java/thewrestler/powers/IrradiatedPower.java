package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;

public class IrradiatedPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("IrradiatedPower");
  public static final String IMG = "irradiated.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final int ENERGY_PER_TRIGGER = 1;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  private int remainingTriggersThisTurn;

  public IrradiatedPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, AbstractDungeon.player, amount, POWER_TYPE);
    remainingTriggersThisTurn = amount;
  }

  @Override
  public void atStartOfTurn() {
    remainingTriggersThisTurn = amount;
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0]
        + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3])
        + DESCRIPTIONS[4];
  }

  @Override
  public int onAttackedToChangeDamage(DamageInfo info, int amount) {
    if (this.remainingTriggersThisTurn > 0 && info.owner == this.source && info.type == DamageInfo.DamageType.NORMAL) {
      this.flash();
      this.applyTrigger();
      remainingTriggersThisTurn--;
    }
    return amount;
  }

  @Override
  public void stackPower(int amount) {
    super.stackPower(amount);
    updateDescription();
    this.remainingTriggersThisTurn += amount;
  }

  @Override
  public void update(int slot) {
    super.update(slot);

    if (this.remainingTriggersThisTurn > 0) {
      this.owner.tint.color.set(Color.CHARTREUSE);
      this.owner.tint.changeColor(Color.WHITE.cpy());
    }
  }

  @Override
  public void reducePower(int amount) {
    WrestlerMod.logger.info("IrradiatedPower::reducePower called. amount: " + amount);
    super.reducePower(amount);
  }

  private void applyTrigger() {
    AbstractDungeon.actionManager.addToTop(new SFXAction("ELECTRO_INTERFERENCE_1"));
//    AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
    AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_PER_TRIGGER));
  }

  @Override
  public AbstractPower makeCopy() {
    return new IrradiatedPower(owner, amount);
  }
}