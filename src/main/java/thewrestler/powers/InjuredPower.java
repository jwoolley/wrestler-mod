package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;

public class InjuredPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("InjuredPower");
  public static final String IMG = "injured.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  public InjuredPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, NAME, IMG, owner, source, amount, POWER_TYPE);
    updateDescription();
  }

  @Override
  public int onAttackedToChangeDamage(DamageInfo info, int damageAmount){
    if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0 && amount > 0 && info.owner != this.owner) {
      final int injuryDamage = Math.min(damageAmount, this.amount);
      triggerPower(injuryDamage);
    }
    return damageAmount;
  }

  public static int getTotalInjuryAmount(AbstractCreature creature) {
    return creature.hasPower(InjuredPower.POWER_ID) ? creature.getPower(InjuredPower.POWER_ID).amount : 0;
  }

  private void triggerPower(int injuredAmount) {
    CardCrawlGame.sound.play("BONE_CRUNCH_1");

    if (injuredAmount >= this.amount) {
      AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.source, this.ID));
      AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.source,
          new PersistentInjuryPower(this.owner, this.source, this.amount), this.amount));
    }
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));
  }

  @Override
  public void updateDescription() {
    this.description = (this.owner.isPlayer ? DESCRIPTIONS[0] : DESCRIPTIONS[1])
        + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3]
        + (this.owner.isPlayer ? DESCRIPTIONS[4] : DESCRIPTIONS[5])
        + this.amount + DESCRIPTIONS[6];
  }

  @Override
  public AbstractPower makeCopy() {
    return new InjuredPower(owner, source, amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}