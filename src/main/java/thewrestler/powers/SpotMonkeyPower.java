package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.sun.webkit.graphics.WCTextRun;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.cards.skill.AbstractPenaltyCardListener;

public class SpotMonkeyPower extends AbstractWrestlerPower implements CloneablePowerInterface, AbstractPenaltyCardListener {
  public static final String POWER_ID = WrestlerMod.makeID("SpotMonkeyPower");
  public static final String IMG = "spotmonkey.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  private int strengthAmount;

  public SpotMonkeyPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
  }

  @Override
  public void stackPower(int amount) {
    this.strengthAmount += amount;
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0]
        + this.amount + DESCRIPTIONS[1]
        + this.amount + DESCRIPTIONS[2];
  }

  @Override
  public AbstractPower makeCopy() {
    return new SpotMonkeyPower(owner, this.strengthAmount);
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
  public void onPlayCard(AbstractCard card, AbstractMonster target) {
    if (card.hasTag(WrestlerCardTags.PENALTY)) {
      this.flash();
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.amount), this.amount));
    }
  }

  @Override
  public void onGainedPenaltyCard() {

  }
}