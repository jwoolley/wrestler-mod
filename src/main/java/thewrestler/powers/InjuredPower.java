package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sun.security.krb5.internal.APOptions;
import thewrestler.WrestlerMod;
import thewrestler.util.info.CombatInfo;

public class InjuredPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("InjuredPower");
  public static final String IMG = "injured.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  public InjuredPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
  }

  @Override
  public int onAttackedToChangeDamage(DamageInfo info, int damageAmount){
    if (amount > 0 && info.owner != this.owner) {
      triggerPower(this.amount);
    }
    return damageAmount;
  }

  @Override
  public void onInitialApplication() {
    triggerPower(this.amount);
  }

  private void triggerPower(int injuredAmount) {
    CardCrawlGame.sound.play("SPRINGBOARD_1");
    CardCrawlGame.sound.play("BONE_CRUNCH_1");

    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(this.owner, new DamageInfo(this.source, injuredAmount, DamageInfo.DamageType.THORNS),
            AbstractGameAction.AttackEffect.SMASH, false));
  }


  public void stackPower(int amount) {
    super.stackPower(amount);
    triggerPower(this.amount);
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));
  }

  @Override
  public void updateDescription() {
    this.description = this.owner.isPlayer ?
        (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1])
        : (DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3]);
  }

  @Override
  public AbstractPower makeCopy() {
    return new InjuredPower(owner, amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}