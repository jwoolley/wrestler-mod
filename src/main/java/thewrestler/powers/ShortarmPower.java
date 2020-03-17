package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.util.CardUtil;

import javax.smartcardio.Card;
import java.util.stream.Collectors;

public class ShortarmPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("ShortarmPower");
  public static final String IMG = "shortarm.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public ShortarmPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
    updateExistingColorlessAttacks(amount);
  }

  public void stackPower(int stackAmount) {
    this.fontScale = 8.0F;
    this.amount += stackAmount;
    updateExistingColorlessAttacks(stackAmount);
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  @Override
  public void onPlayCard(AbstractCard card, AbstractMonster m) {

  }

  @Override
  public void onAfterUseCard(AbstractCard card, UseCardAction action) {

  }

  public void applyDamageIncrease(AbstractCard card) {
    if (shouldApplyPower(card)) {
      card.damage = (card.baseDamage = card.baseDamage + this.amount);
      card.isDamageModified = true;
    }
  }

  public boolean shouldApplyPower(AbstractCard card) {
    return card.color == AbstractCard.CardColor.COLORLESS && card.type == AbstractCard.CardType.ATTACK;
  }

  private void updateExistingColorlessAttacks(int increase) {
    CardUtil.forAllCardsInCombat(c -> c.baseDamage += increase, this::shouldApplyPower);
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