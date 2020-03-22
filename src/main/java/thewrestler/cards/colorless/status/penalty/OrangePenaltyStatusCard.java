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
import thewrestler.keywords.CustomTooltipKeywords;

public class OrangePenaltyStatusCard extends AbstractPenaltyStatusCard {
  public static final String ID = "WrestlerMod:OrangePenaltyStatusCard";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;

  public static final String TOOLTIP_KEYWORD_KEY = CustomTooltipKeywords.PENALTY_CARD_ORANGE;
  public static final String IMG_KEY = "orange";
  public static final String IMG_PATH = getPenaltyCardImgPath(IMG_KEY + ".png");

  private static final CardStrings cardStrings;

  private static final int DAMAGE = 5;
  private static final int FRAIL_AMOUNT = 1;

  public OrangePenaltyStatusCard() {
    super(ID, NAME, IMG_PATH, IMG_KEY, getDescription(), TOOLTIP_KEYWORD_KEY);
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

  @Override
  public void triggerOnCardGained() {

  }
}
