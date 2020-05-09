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

import static java.awt.SystemColor.info;

public class InjuredPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("InjuredPower");
  public static final String IMG = "injured.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  private boolean persistInjury = false;

  private static final int STACKS_PER_ATTACK = 1;

  public InjuredPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, NAME, IMG, owner, source, amount, POWER_TYPE);
    updateDescription();
  }

  public static int getTotalInjuryAmount(AbstractCreature creature) {
    return creature.hasPower(InjuredPower.POWER_ID) ? creature.getPower(POWER_ID).amount : 0;
  }

  @Override
  public void atStartOfTurn() {
    this.persistInjury = false;
  }

  @Override
  public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
    if (target != this.owner) {
      this.stackPower(STACKS_PER_ATTACK);
      this.persistInjury = true;
    }
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    if (this.amount > 0) {
      CardCrawlGame.sound.play("SPRINGBOARD_1");
      CardCrawlGame.sound.play("BONE_CRUNCH_1");

      AbstractDungeon.actionManager.addToBottom(
          new DamageAction(this.owner, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS),
              AbstractGameAction.AttackEffect.SMASH, false));
    }
    if (this.amount == 1) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));
    } else {
      AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.source, POWER_ID, getReductionAmount()));
    }
  }

  private int getReductionAmount() {
    return (int) Math.round(Math.ceil(this.amount / 2.0f));
  }

  @Override
  public void updateDescription() {
    final String yourOrIts = isPlayer() ? DESCRIPTIONS[3] : DESCRIPTIONS[4];
    this.description = (isPlayer() ? DESCRIPTIONS[0] : DESCRIPTIONS[1]) + this.amount
        + DESCRIPTIONS[2] + yourOrIts + DESCRIPTIONS[5]
        + (isPlayer() ? DESCRIPTIONS[6] : DESCRIPTIONS[7]) + STACKS_PER_ATTACK + DESCRIPTIONS[8]
        + (isPlayer() ? DESCRIPTIONS[9] : DESCRIPTIONS[10]) + DESCRIPTIONS[11]
        + yourOrIts + DESCRIPTIONS[12] + getReductionAmount() + DESCRIPTIONS[13] + yourOrIts + DESCRIPTIONS[14]
        + (isPlayer() ? DESCRIPTIONS[15] : DESCRIPTIONS[16]) + DESCRIPTIONS[17];
  }

  private boolean isPlayer() {
    return this.owner.isPlayer;
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