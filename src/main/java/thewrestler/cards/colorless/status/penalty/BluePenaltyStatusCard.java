package thewrestler.cards.colorless.status.penalty;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import org.apache.commons.lang3.StringUtils;
import thewrestler.actions.power.GainPlatedArmorRandomMonsterAction;
import thewrestler.powers.BravadoPower;
import thewrestler.powers.enqueuedpenaltycard.EnqueuedPenaltyCardPower;

public class BluePenaltyStatusCard extends AbstractPenaltyStatusCard {
  public static final String ID = "WrestlerMod:BluePenaltyStatusCard";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = getPenaltyCardImgPath("blue.png");

  private static final CardStrings cardStrings;

  private static final int CARD_DRAW_AMOUNT = 2;
  private static final int ENEMY_PLATED_ARMOR_GAIN = 2;

  public BluePenaltyStatusCard() {
    super(ID, NAME, IMG_PATH, getDescription(CARD_DRAW_AMOUNT));
    this.magicNumber = this.baseMagicNumber = ENEMY_PLATED_ARMOR_GAIN;
    this.misc = CARD_DRAW_AMOUNT;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, this.misc), this.misc));
  }

  @Override
  public AbstractPenaltyStatusCard makeCopy() {
    return new BluePenaltyStatusCard();
  }

  private static String getDescription(int drawAmount) {
    return DESCRIPTION + drawAmount + EXTENDED_DESCRIPTION[0];
  }

  @Override
  public void upgrade() { }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }

  private static final String ENQUEUE_POWER_ID = "WrestlerMod:EnqueueBlueCardPower";
  private static final String ENQUEUE_POWER_IMG_NAME = getPenaltyCardImgPath("enqueueblue.png");
  @Override
  protected EnqueuedPenaltyCardPower getEneueuedCardPower(int amount) {
    return new EnqueueCardPower(amount, ENQUEUE_POWER_ID, NAME, ENQUEUE_POWER_IMG_NAME);
  }

  @Override
  public void triggerOnCardGained() {
    AbstractPlayer p = AbstractDungeon.player;
    AbstractDungeon.actionManager.addToBottom(new GainPlatedArmorRandomMonsterAction(p, ENEMY_PLATED_ARMOR_GAIN));
  }
}
