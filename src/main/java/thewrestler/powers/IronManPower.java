package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;

public class IronManPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("IronManPower");
  public static final String IMG = "ironman.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final int HP_THRESHOLD = 5;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public IronManPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, AbstractDungeon.player, amount, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    this.description  = DESCRIPTIONS[0] + HP_THRESHOLD + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
  }

  @Override
  public void atStartOfTurn() {
    if (this.owner.currentHealth <= HP_THRESHOLD) {
      flash();
      AbstractDungeon.actionManager.addToBottom(new SFXAction("METAL_MAN_RIFF_2"));
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(this.owner, this.owner, new TemporaryBufferPower(this.owner, this.amount), this.amount));
    }
  }

  @Override
  public AbstractPower makeCopy() {
    return new IronManPower(owner, amount);
  }
}