package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;

public class GrayMatterPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("GrayMatterPower");
  public static final String IMG = "graymatter.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final int CARDS_PER_TRIGGER = 1;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  public GrayMatterPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, AbstractDungeon.player, amount, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0]
      + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3])
      + DESCRIPTIONS[4];
  }

  @Override
  public int onAttackedToChangeDamage(DamageInfo info, int amount) {
    if (info.owner == this.source && info.type == DamageInfo.DamageType.NORMAL) {
      this.flash();
      this.applyTrigger();
      this.updateDescription();
    }
    return amount;
  }

  @Override
  public void atStartOfTurn() {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));
  }

  private void applyTrigger() {
    AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, CARDS_PER_TRIGGER));
    AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
    AbstractDungeon.actionManager.addToBottom(new SFXAction("SLIME_ATTACK_2"));
  }

  @Override
  public AbstractPower makeCopy() {
    return new GrayMatterPower(owner, amount);
  }
}