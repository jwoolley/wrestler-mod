package thewrestler.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.GainStrengthPower;

public class WrestlerShackled extends GainStrengthPower {
  public static final PowerType POWER_TYPE = PowerType.BUFF;
  public WrestlerShackled(AbstractCreature owner, int newAmount) {
    super(owner, newAmount);
    this.type = POWER_TYPE;
  }
}