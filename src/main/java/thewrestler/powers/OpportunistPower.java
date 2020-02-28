package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.cards.skill.AbstractSportsmanshipListener;

public class OpportunistPower extends AbstractWrestlerPower implements CloneablePowerInterface, AbstractSportsmanshipListener {
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

  public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
    if (damageAmount > 0) {
      AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
    }
    return 0;
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
  public void onUnsportingChanged(int changeAmount, int newValue, boolean isEndOfTurnChange) {
    if (changeAmount != 0) {
      this.flash();
      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, this.owner,
          this.amount * Math.abs(changeAmount)));
    }
  }

  @Override
  public void onBecomeSporting() {

  }

  @Override
  public void onBecomeUnsporting() {

  }
}