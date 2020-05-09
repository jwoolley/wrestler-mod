package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.WrestlerMod;
import thewrestler.util.info.CombatInfo;

public class PenaltyLockedPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("PenaltyLockedPower");
  public static final String IMG = "scrapper.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public PenaltyLockedPower(int amount) {
    super(POWER_ID, NAME, IMG, AbstractDungeon.player, AbstractDungeon.player, amount, POWER_TYPE);
  }

  @Override
  public void atStartOfTurnPostDraw() {
    this.flash();
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0]
        + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3]);
  }

  @Override
  public AbstractPower makeCopy() {
    return new PenaltyLockedPower(amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}