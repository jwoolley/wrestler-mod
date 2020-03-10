package thewrestler.cards.colorless.status.penalty;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.powers.enqueuedpenaltycard.EnqueuedPenaltyCardPower;

public class RedPenaltyStatusCard extends AbstractPenaltyStatusCard {
  public static final String ID = "WrestlerMod:RedPenaltyStatusCard";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = getPenaltyCardImgPath("red.png");

  private static final CardStrings cardStrings;

  private static final int STRENGTH_GAIN = 1;
  private static final int HP_LOSS = 2;

  public RedPenaltyStatusCard() {
    super(ID, NAME, IMG_PATH, getDescription());
    this.magicNumber = this.baseMagicNumber = STRENGTH_GAIN;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (this.dontTriggerOnUseCard) {
      AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, HP_LOSS));
    }
  }

  @Override
  public void triggerWhenDrawn(){
    AbstractPlayer p = AbstractDungeon.player;
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new StrengthPower(p, STRENGTH_GAIN), STRENGTH_GAIN));
  }

  @Override
  public AbstractPenaltyStatusCard makeCopy() {
    return new RedPenaltyStatusCard();
  }

  public void triggerOnEndOfTurnForPlayingCard() {
    this.dontTriggerOnUseCard = true;
    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
  }

  private static String getDescription() {
    return DESCRIPTION + HP_LOSS + EXTENDED_DESCRIPTION[0];
  }

  @Override
  public void upgrade() { }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }

  private static final String ENQUEUE_POWER_ID = "WrestlerMod:EnqueueRedCardPower";
  private static final String ENQUEUE_POWER_IMG_NAME = getPenaltyCardImgPath("enqueuered.png");
  @Override
  protected EnqueuedPenaltyCardPower getEneueuedCardPower(int amount) {
    return new EnqueueCardPower(amount, ENQUEUE_POWER_ID, NAME, ENQUEUE_POWER_IMG_NAME);
  }
}
