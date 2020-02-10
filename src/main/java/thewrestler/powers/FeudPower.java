package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.BeamCell;
import com.megacrit.cardcrawl.cards.green.Neutralize;
import com.megacrit.cardcrawl.cards.red.Flex;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.attack.Elbow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeudPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("FeudPower");
  public static final String IMG = "feud.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public FeudPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
  }

  @Override
  public void atStartOfTurnPostDraw() {
    flash();
    AbstractDungeon.actionManager.addToBottom(
        new MakeTempCardInDrawPileAction(new Elbow(), this.amount, true, true));
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0]
        + this.amount
        + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2])
        + DESCRIPTIONS[3];
  }

  @Override
  public AbstractPower makeCopy() {
    return new ProvenTacticsPower(owner, amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
