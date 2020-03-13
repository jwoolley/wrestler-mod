package thewrestler.cards.colorless.status.penalty;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import thewrestler.powers.enqueuedpenaltycard.EnqueuedPenaltyCardPower;

public class GreenPenaltyStatusCard extends AbstractPenaltyStatusCard {
  public static final String ID = "WrestlerMod:GreenPenaltyStatusCard";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = getPenaltyCardImgPath("green.png");

  private static final CardStrings cardStrings;

  private static final int DEX_GAIN = 1;
  private static final int GOLD_LOSS = 2;

  public GreenPenaltyStatusCard() {
    super(ID, NAME, IMG_PATH, getDescription());
    this.magicNumber = this.baseMagicNumber = DEX_GAIN;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (this.dontTriggerOnUseCard) {
      this.exhaust = false;
      AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
        @Override
        public void update() {
          if (p.gold > 0) {
            CardCrawlGame.sound.play("GOLD_JINGLE");

            p.loseGold(Math.min(GOLD_LOSS, p.gold));
            AbstractDungeon.effectList.add(new GainPennyEffect(p, p.hb.cX, p.hb.cY, p.hb.cX, Settings.HEIGHT * .9f,
                false));
          }

          this.isDone = true;
        }
      });
    } else {
      this.exhaust = true;
    }
  }

  @Override
  public void triggerWhenDrawn(){
    AbstractPlayer p = AbstractDungeon.player;
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new DexterityPower(p, DEX_GAIN), DEX_GAIN));
  }

  @Override
  public AbstractPenaltyStatusCard makeCopy() {
    return new GreenPenaltyStatusCard();
  }

  public void triggerOnEndOfTurnForPlayingCard() {
    this.dontTriggerOnUseCard = true;
    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
  }

  private static String getDescription() {
    return DESCRIPTION + GOLD_LOSS + EXTENDED_DESCRIPTION[0];
  }

  @Override
  public void upgrade() { }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }


  private static final String ENQUEUE_POWER_ID = "WrestlerMod:EnqueueGreenCardPower";
  private static final String ENQUEUE_POWER_IMG_NAME = "enqueuegreen.png";
  @Override
  protected EnqueuedPenaltyCardPower getEneueuedCardPower(int amount) {
    return new EnqueueCardPower(amount, ENQUEUE_POWER_ID, NAME, ENQUEUE_POWER_IMG_NAME);
  }
}