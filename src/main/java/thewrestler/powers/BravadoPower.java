package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;
import thewrestler.characters.WrestlerCharacter;

public class BravadoPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("BravadoPower");
  public static final String IMG = "bravado.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  private static final int AMOUNT_REQUIRED = 5;

  public BravadoPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
  }

  @Override
  public void stackPower(int stackAmount) {
    super.stackPower(stackAmount);

    if (this.amount >= AMOUNT_REQUIRED) {
      WrestlerCharacter.getSignatureMoveInfo().triggerGainTrademarkMove();
      // AbstractDungeon.actionManager.addToTop(new GainTradeMarkMoveAction());
      CardCrawlGame.sound.play("BOXING_BELL_1");
      CardCrawlGame.sound.play("BOXING_BELL_1");
      this.flash();
      this.amount -= AMOUNT_REQUIRED;
      if (this.amount <= 0) {
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      }
    }
  }

  public static int getAmountRequired() {
    return AMOUNT_REQUIRED;
  }

  public static int getPlayerAmount() {
    AbstractPlayer player = AbstractDungeon.player;
    if (AbstractDungeon.isPlayerInDungeon() && player.hasPower(POWER_ID)) {
      return player.getPower(POWER_ID).amount;
    }
    return 0;
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public AbstractPower makeCopy() {
    return new BravadoPower(owner, amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}