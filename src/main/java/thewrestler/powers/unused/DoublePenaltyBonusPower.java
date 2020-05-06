package thewrestler.powers.unused;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.powers.AbstractWrestlerPower;

public class DoublePenaltyBonusPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("DoublePenaltyBonusPower");
  public static final String IMG = "shortarm.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public DoublePenaltyBonusPower(int amount) {
    super(POWER_ID, NAME, IMG, AbstractDungeon.player, AbstractDungeon.player, amount, POWER_TYPE);
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0]
        + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3])
        + DESCRIPTIONS[4];
  }

  @Override
  public AbstractPower makeCopy() {
    return new DoublePenaltyBonusPower(this.amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}