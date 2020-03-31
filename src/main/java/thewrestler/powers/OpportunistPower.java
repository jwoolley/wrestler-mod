package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.cards.skill.AbstractPenaltyCardListener;
import thewrestler.util.CreatureUtils;

public class OpportunistPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("OpportunistPower");
  public static final String IMG = "opportunist.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  private int drawAmount;

  public OpportunistPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
  }

  @Override
  public void stackPower(int amount) {
    this.drawAmount += amount;
    updateDescription();
  }

  @Override
  public void atStartOfTurnPostDraw() {
    if (CreatureUtils.getLivingMonsters().stream().noneMatch(m -> m.getIntentBaseDmg() >= 0)) {
      AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.drawAmount));
    }
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public AbstractPower makeCopy() {
    return new OpportunistPower(owner, this.drawAmount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}