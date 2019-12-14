package theplaceholder.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GrappledPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  private static final int DMG_DECREASE_PCT = 25;
  private static final int DMG_INCREASE_PCT = 25;
  public static final int MAX_HP_PCT = 10;

  public static final String POWER_ID = "GrappledPower";
  public static final String IMG = "grappled.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public GrappledPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, NAME, IMG, owner, source, amount);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + DMG_DECREASE_PCT + DESCRIPTIONS[1]  + DMG_INCREASE_PCT + DESCRIPTIONS[2]
      + this.amount + DESCRIPTIONS[3];
  }

  @Override
  public AbstractPower makeCopy() {
    return new GrappledPower(owner, source, amount);
  }
}
