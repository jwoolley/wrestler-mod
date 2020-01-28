package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.WrestlerMod;
import thewrestler.util.info.CombatInfo;

public class FloatOverPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("FloatOverPower");
  public static final String IMG = "floatover.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public FloatOverPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    if (isPlayer && AbstractDungeon.player.hand.isEmpty()) {
      flash();
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(this.owner, this.owner, new TemporaryBufferPower(this.owner, this.amount), this.amount));
    }
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public AbstractPower makeCopy() {
    return new FloatOverPower(owner, amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}