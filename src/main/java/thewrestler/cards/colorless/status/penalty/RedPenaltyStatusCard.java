package thewrestler.cards.colorless.status.penalty;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class RedPenaltyStatusCard extends AbstractPenaltyStatusCard {
  public static final String ID = "WrestlerMod:RedPenaltyStatusCard";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = getPenaltyCardImgPath("red.png");

  private static final CardStrings cardStrings;

  private static final int STRENGTH_GAIN = 1;
  private static final int DAMAGE = 2;

  public RedPenaltyStatusCard() {
    super(ID, NAME, IMG_PATH, getDescription());
    this.magicNumber = this.baseMagicNumber = STRENGTH_GAIN;
    this.misc = DAMAGE;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new StrengthPower(p, STRENGTH_GAIN), STRENGTH_GAIN));
  }

  @Override
  public AbstractPenaltyStatusCard makeCopy() {
    return new RedPenaltyStatusCard();
  }

  private static String getDescription() {
    return DESCRIPTION + DAMAGE + EXTENDED_DESCRIPTION[0];
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
    AbstractPlayer p = AbstractDungeon.player;

    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(p, new DamageInfo(p, DAMAGE, DamageInfo.DamageType.THORNS),
            AbstractGameAction.AttackEffect.FIRE));
  }
}
