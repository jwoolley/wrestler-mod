package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;

public class ShortarmPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("ShortarmPower");
  public static final String IMG = "shortarm.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  private boolean attackWasDirty;
  private boolean usedAttack;

  public ShortarmPower(AbstractCreature owner, int sprainAmount) {
    super(POWER_ID, NAME, IMG, owner, owner, sprainAmount, POWER_TYPE);
    this.attackWasDirty = false;
    this.usedAttack = false;
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  @Override
  public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
    if ((damageAmount > 0) && (target != this.owner) && (info.type == DamageInfo.DamageType.NORMAL)) {
      if (this.attackWasDirty) {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(target, this.owner, new InjuredPower(target, this.amount), this.amount));
      }

      //      testing version that lasts until EOT
      //      this.usedAttack = true;
    }
  }

  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.type == AbstractCard.CardType.ATTACK && card.hasTag(WrestlerCardTags.DIRTY)) {
      this.attackWasDirty = true;
    } else {
      this.attackWasDirty = false;
    }
  }

  @Override
  public void onAfterUseCard(AbstractCard card, UseCardAction action) {
    if (this.usedAttack) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public AbstractPower makeCopy() {
    return new ShortarmPower(owner, this.amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}