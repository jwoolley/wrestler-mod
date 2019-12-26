package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;

public class TripleThreatPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("TripleThreatPower");
  public static final String IMG = "triplethreat.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  private final AbstractCard bonusCard;

  public TripleThreatPower(AbstractCreature owner, int amount, AbstractCard bonusCard) {
    super(POWER_ID, NAME, IMG, owner, AbstractDungeon.player, amount, POWER_TYPE);
    this.bonusCard = bonusCard;
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
  public void atEndOfTurn(boolean isPlayer) {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));
  }

  private void applyTrigger() {
    AbstractDungeon.actionManager.addToBottom(
        new MakeTempCardInHandAction(this.bonusCard.makeStatEquivalentCopy()));
    AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
    AbstractDungeon.actionManager.addToBottom(new SFXAction("GONG_STRIKE_1"));
  }

  @Override
  public AbstractPower makeCopy() {
    return new TripleThreatPower(owner, amount, this.bonusCard);
  }
}