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



  public ShortarmPower(int amount) {
    super(POWER_ID, NAME, IMG, AbstractDungeon.player, AbstractDungeon.player, amount, POWER_TYPE);
  }


  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.type == AbstractCard.CardType.ATTACK && card.hasTag(WrestlerCardTags.DIRTY)) {
      flash();
    }
  }

  @Override
  public float atDamageGive(float damage, DamageInfo.DamageType damageType, AbstractCard card) {
    if (card.type == AbstractCard.CardType.ATTACK && card.hasTag(WrestlerCardTags.DIRTY)) {
      damage += this.amount;
    }
    return damage;
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public AbstractPower makeCopy() {
    return new ShortarmPower(this.amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}