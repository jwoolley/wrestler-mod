package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.cards.skill.AbstractPenaltyCardListener;

public class OpportunistPower extends AbstractWrestlerPower implements CloneablePowerInterface, AbstractPenaltyCardListener {
  public static final String POWER_ID = WrestlerMod.makeID("OpportunistPower");
  public static final String IMG = "opportunist.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  private int blockAmount;

  public OpportunistPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
  }

  @Override
  public void stackPower(int amount) {
    this.blockAmount += amount;
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public AbstractPower makeCopy() {
    return new OpportunistPower(owner, this.blockAmount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }

  @Override
  public void onGainedWarningCard() {

  }

  @Override
  public void onGainedPenaltyCard() {
    this.flash();
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, this.owner,
        this.amount));
  }
}