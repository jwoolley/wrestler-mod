package thewrestler.powers.unused;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;
import thewrestler.powers.AbstractWrestlerPower;

public class CurtainJerkerPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("CurtainJerkerPower");
  public static final String IMG = "curtainjerker.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public CurtainJerkerPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
  }

  @Override
  public int onAttacked(DamageInfo info, int amount) {
    flash();
    AbstractDungeon.actionManager.addToBottom(new ApplyGrappledAction(this.owner, this.source));
    AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
    return amount;
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0];
  }

  @Override
  public AbstractPower makeCopy() {
    return new CurtainJerkerPower(owner, amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}