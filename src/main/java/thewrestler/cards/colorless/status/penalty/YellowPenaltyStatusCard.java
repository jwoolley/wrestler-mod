package thewrestler.cards.colorless.status.penalty;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import org.apache.commons.lang3.StringUtils;
import thewrestler.keywords.CustomTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.orbs.BasePenaltyOrb;
import thewrestler.orbs.GreenPenaltyOrb;
import thewrestler.orbs.YellowPenaltyOrb;
import thewrestler.powers.BravadoPower;

public class YellowPenaltyStatusCard extends AbstractPenaltyStatusCard {
  public static final String ID = "WrestlerMod:YellowPenaltyStatusCard";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;

  public static final String TOOLTIP_KEYWORD_KEY = CustomTooltipKeywords.PENALTY_CARD_YELLOW;
  public static final String IMG_KEY = "yellow";
  public static final String IMG_PATH = getPenaltyCardImgPath(IMG_KEY + ".png");

  private static final CardStrings cardStrings;

  private static final int ENERGY_GAIN = 1;
  private static final int NUM_DAZED = 2;

  public YellowPenaltyStatusCard() {
    super(ID, NAME, IMG_PATH, IMG_KEY, getDescription(ENERGY_GAIN), TOOLTIP_KEYWORD_KEY);
    this.magicNumber = this.baseMagicNumber = NUM_DAZED;
    this.misc = ENERGY_GAIN;
  }

  @Override
  public void triggerOnCardUsed(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EnergizedPower(p, this.misc), this.misc));
  }

  @Override
  protected Color getFlashColor() {
    return Color.YELLOW;
  }

  @Override
  public BasePenaltyOrb getOrb() {
    return new YellowPenaltyOrb();
  }

  @Override
  public AbstractPenaltyStatusCard makeCopy() {
    AbstractPenaltyStatusCard card = new YellowPenaltyStatusCard();
    card.applyDiscounts();
    return card;
  }

  private static String getDescription(int energyAmount) {
    return DESCRIPTION
        + StringUtils.repeat(EXTENDED_DESCRIPTION[0], energyAmount)
        + EXTENDED_DESCRIPTION[1];
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
    AbstractDungeon.actionManager.addToTop(new MakeTempCardInDiscardAction(new Dazed(), this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, BravadoPower.POWER_ID, this.magicNumber));
  }
}
