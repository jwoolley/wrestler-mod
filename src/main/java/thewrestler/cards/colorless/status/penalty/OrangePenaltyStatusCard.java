package thewrestler.cards.colorless.status.penalty;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import thewrestler.powers.enqueuedpenaltycard.EnqueuedPenaltyCardPower;

public class OrangePenaltyStatusCard extends AbstractPenaltyStatusCard {
  public static final String ID = "WrestlerMod:OrangePenaltyStatusCard";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = getPenaltyCardImgPath("orange.png");

  private static final CardStrings cardStrings;

  private static final int DAMAGE = 5;
  private static final int FRAIL_AMOUNT = 1;

  public OrangePenaltyStatusCard() {
    super(ID, NAME, IMG_PATH, getDescription());
    this.damage = this.baseDamage = DAMAGE;
    this.magicNumber = this.baseMagicNumber = FRAIL_AMOUNT;
    this.isMultiDamage = true;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (this.dontTriggerOnUseCard) {
      this.exhaust = false;
      // setting isSourceMonster to true prevents the frail from falling off immediately
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(p, p, new FrailPower(p, this.magicNumber, true)));
    } else {
      this.exhaust = true;
    }
  }

  @Override
  public void triggerWhenDrawn(){
    AbstractPlayer p = AbstractDungeon.player;
    AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageType,
        AbstractGameAction.AttackEffect.FIRE, true));
  }

  @Override
  public AbstractPenaltyStatusCard makeCopy() {
    return new OrangePenaltyStatusCard();
  }

  public void triggerOnEndOfTurnForPlayingCard() {
    this.dontTriggerOnUseCard = true;
    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
  }

  private static String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public void upgrade() { }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }

  private static final String ENQUEUE_POWER_ID = "WrestlerMod:EnqueueOrangeCardPower";
  private static final String ENQUEUE_POWER_IMG_NAME = getPenaltyCardImgPath("enqueueorange.png");
  @Override
  protected EnqueuedPenaltyCardPower getEneueuedCardPower(int amount) {
    return new EnqueueCardPower(amount, ENQUEUE_POWER_ID, NAME, ENQUEUE_POWER_IMG_NAME);
  }
}
