package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;

import javax.xml.stream.events.DTD;

public class PersistentInjuryPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("PersistentInjuryPower");
  public static final String IMG = "persistentinjury.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  public PersistentInjuryPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, NAME, IMG, owner, source, amount, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount;
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    if (isPlayer == this.owner.isPlayer) {
      CardCrawlGame.sound.play("SPRINGBOARD_1");
      CardCrawlGame.sound.play("BONE_CRUNCH_1");

      AbstractDungeon.actionManager.addToBottom(
          new DamageAction(this.owner, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS),
              AbstractGameAction.AttackEffect.SMASH, false));

      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.source,
          new InjuredPower(this.owner, this.source, this.amount), this.amount));
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, this.ID));
    }
  }

  @Override
  public AbstractPower makeCopy() {
    return new PersistentInjuryPower(owner, source, amount);
  }
}