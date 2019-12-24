package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;

public class IronManPower extends AbstractWrestlerPower implements CloneablePowerInterface, OnPlayerDeathPower {
  public static final String POWER_ID = WrestlerMod.makeID("IronManPower");
  public static final String IMG = "ironman.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final int HEAL_AMOUNT = 1;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public IronManPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, AbstractDungeon.player, amount, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    this.description = (this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2])
        + DESCRIPTIONS[3] + HEAL_AMOUNT + DESCRIPTIONS[4];
  }

  @Override
  public void atStartOfTurn() {
    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  @Override
  public AbstractPower makeCopy() {
    return new IronManPower(owner, amount);
  }

  @Override
  public boolean onPlayerDeath(AbstractPlayer abstractPlayer, DamageInfo damageInfo) {
    flash();
    AbstractDungeon.actionManager.addToTop(new SFXAction("METAL_MAN_RIFF_1"));
    AbstractDungeon.player.heal(HEAL_AMOUNT, true);
    return false;
  }
}