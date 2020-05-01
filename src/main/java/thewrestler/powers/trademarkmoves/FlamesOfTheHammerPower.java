package thewrestler.powers.trademarkmoves;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.powers.AbstractWrestlerPower;
import thewrestler.signaturemoves.cards.attack.BurningHammer;

public class FlamesOfTheHammerPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("FlamesOfTheHammerPower");
  public static final String IMG = "hammerflame.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  public FlamesOfTheHammerPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, NAME, IMG, owner, source, amount, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public void onPlayCard(AbstractCard card, AbstractMonster monster) {
    if (monster == this.owner && monster.hasPower(FlamesOfTheHammerPower.POWER_ID) && card.type == AbstractCard.CardType.ATTACK) {
      CardCrawlGame.sound.play("FORGE_HAMMER_1");
      flash();
      AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(new BurningHammer()));
      AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.source, this));
    }
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    this.flash();
    CardCrawlGame.sound.play("ATTACK_FIRE");
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(this.owner, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS),
            AbstractGameAction.AttackEffect.FIRE, false));
  }

  @Override
  public AbstractPower makeCopy() {
    return new FlamesOfTheHammerPower(owner, source, amount);
  }
}