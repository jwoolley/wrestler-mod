package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.attack.Elbow;
import thewrestler.util.info.CombatInfo;

public class FeudRivalPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("FeudRivalPower");
  public static final String IMG = "feudrival.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public FeudRivalPower(AbstractCreature owner) {
    super(POWER_ID, NAME, IMG, owner, AbstractDungeon.player, -1, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    final int feudAmount = getFeudAmount();
    this.description = DESCRIPTIONS[0]
        + feudAmount
        + (feudAmount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2])
        + DESCRIPTIONS[3];
  }

  private static int getFeudAmount() {
    return AbstractDungeon.player.hasPower(FeudPower.POWER_ID)
        ? AbstractDungeon.player.getPower(FeudPower.POWER_ID).amount
        : 0;
  }

  @Override
  public AbstractPower makeCopy() {
    return new FeudRivalPower(owner);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
