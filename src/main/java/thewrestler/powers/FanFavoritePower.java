package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;

public class FanFavoritePower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("FanFavoritePower");
  public static final String IMG = "fanfavorite.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public static final int ATTACKS_PER_TRIGGER = 3;
  public static final int BASE_BLOCK_AMOUNT = 4;
  public static final int BLOCK_AMOUNT_UPGRADE = 1;

  private int blockAmount;

  public FanFavoritePower(AbstractCreature owner, int blockAmount) {
    super(POWER_ID, NAME, IMG, owner, owner, 0, POWER_TYPE);
    this.blockAmount = blockAmount;
  }

  @Override
  public void atStartOfTurn() {
    this.amount = 0;
    updateDescription();
  }

  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.type == AbstractCard.CardType.ATTACK) {
      this.amount++;
      if (this.amount % ATTACKS_PER_TRIGGER == 0) {
        flash();
        CardCrawlGame.sound.play("CHEER_CROWD_1");
//        AbstractDungeon.actionManager.addToBottom(new SFXAction("CHEER_CROWD_1"));
        this.amount = 0;
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, this.owner, this.blockAmount));
      }
    }
    updateDescription();
  }

  @Override
  public void stackPower(int amount) {
      this.blockAmount += amount;
      updateDescription();
  }

  @Override
  public void updateDescription() {
    final int attacksRemaining = ATTACKS_PER_TRIGGER - this.amount;
    this.description = DESCRIPTIONS[0] + this.ATTACKS_PER_TRIGGER + DESCRIPTIONS[1] +
      + this.blockAmount + DESCRIPTIONS[2]
      + attacksRemaining + (attacksRemaining == 1 ? DESCRIPTIONS[3] : DESCRIPTIONS[4])
      + DESCRIPTIONS[5];
  }

  @Override
  public AbstractPower makeCopy() {
    return new FanFavoritePower(owner, this.blockAmount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}