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
import thewrestler.WrestlerMod;
import thewrestler.util.info.CombatInfo;

public class InjuredPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("InjuredPower");
  public static final String IMG = "injured.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  private boolean reapplyInjured;

  public InjuredPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
    reapplyInjured = false;
  }

  @Override
  public void atStartOfTurn() {
    this.flash();
    CardCrawlGame.sound.play("SPRINGBOARD_1");
    CardCrawlGame.sound.play("BONE_CRUNCH_1");
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(this.owner, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS),
            AbstractGameAction.AttackEffect.SMASH, false));

    WrestlerMod.logger.info("SprainPower::_atStartOfTurn called. setting reapplyInjured to false: " + this.reapplyInjured);

    this.reapplyInjured = false;
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    WrestlerMod.logger.info("SprainPower::_atEndOfTurn called. isPlayer: " + isPlayer
        + "; reapplyInjured: " + this.reapplyInjured + "; numAttacksPlayed: " + CombatInfo.getNumAttacksPlayed());
    if (!isPlayer && !this.reapplyInjured || isPlayer && CombatInfo.getNumAttacksPlayed() == 0) {
      WrestlerMod.logger.info("SprainPower::_atEndOfTurn removing power");
    } else {
      this.flashWithoutSound();
      CardCrawlGame.sound.play("SPRINGBOARD_1");
      CardCrawlGame.sound.play("SNAP_LIGAMENT_1");
      AbstractDungeon.actionManager.addToBottom(
          new DamageAction(this.owner, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS),
              AbstractGameAction.AttackEffect.SMASH, false));
    }
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));

  }

  @Override
  public void onAttack(DamageInfo info, int amount, AbstractCreature target) {
    if (info.owner == this.owner && this.owner != target && info.type == DamageInfo.DamageType.NORMAL) {
      WrestlerMod.logger.info("SprainPower::onAttack called. setting reapplyInjured to true");
      this.reapplyInjured = true;
    }
  }

  @Override
  public void updateDescription() {
    this.description = this.owner.isPlayer ?
        (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2])
        : (DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4] + this.amount + DESCRIPTIONS[5]);
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