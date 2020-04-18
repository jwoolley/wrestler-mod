package thewrestler.cards.colorless.status.penalty;

import com.badlogic.gdx.graphics.Color;
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
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.orbs.BasePenaltyOrb;
import thewrestler.orbs.GreenPenaltyOrb;
import thewrestler.orbs.RedPenaltyOrb;

public class RedPenaltyStatusCard extends AbstractPenaltyStatusCard {
  public static final String ID = "WrestlerMod:RedPenaltyStatusCard";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;

  public static final String TOOLTIP_KEYWORD_KEY = CustomTooltipKeywords.PENALTY_CARD_RED;
  public static final String IMG_KEY = "red";
  public static final String IMG_PATH = getPenaltyCardImgPath(IMG_KEY + ".png");

  private static final CardStrings cardStrings;

  private static final int STRENGTH_GAIN = 2;
  private static final int DAMAGE = 3;

  public RedPenaltyStatusCard() {
    super(ID, NAME, IMG_PATH, IMG_KEY, getDescription(), TOOLTIP_KEYWORD_KEY);
    this.magicNumber = this.baseMagicNumber = STRENGTH_GAIN;
    this.misc = DAMAGE;
  }

  @Override
  public void triggerOnCardUsed(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new StrengthPower(p, STRENGTH_GAIN), STRENGTH_GAIN));
  }

  @Override
  protected Color getFlashColor() {
    return Color.SCARLET;
  }

  @Override
  public BasePenaltyOrb getOrb() {
    return new RedPenaltyOrb();
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
  public void triggerOnEndOfTurn() {
    AbstractPlayer p = AbstractDungeon.player;

    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(p, new DamageInfo(p, DAMAGE, DamageInfo.DamageType.THORNS),
            AbstractGameAction.AttackEffect.FIRE));
  }
}
