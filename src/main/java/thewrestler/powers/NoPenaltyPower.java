package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.actions.GainPenaltyCardAction;

public class NoPenaltyPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("NoPenaltyPower");
  public static final String IMG = "nopenalty.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public NoPenaltyPower() {
    super(POWER_ID, NAME, IMG, AbstractDungeon.player, AbstractDungeon.player, -1, POWER_TYPE);
  }


  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0];
  }

  @Override
  public AbstractPower makeCopy() {
    return new NoPenaltyPower();
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}