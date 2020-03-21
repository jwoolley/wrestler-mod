package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.PutOnDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Backslide extends CustomCard {
  public static final String ID = "WrestlerMod:Backslide";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "backslide.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = 0;

  private static final int DRAW_AMOUNT = 2;
  private static final int DRAW_AMOUNT_UPGRADE = 1;
  private static final int RETURN_AMOUNT = 1;
  private static final int RETURN_AMOUNT_UPGRADE = 1;
  private static final int PENALTY_CARD_GAIN = 1;

  public Backslide() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(RETURN_AMOUNT), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = DRAW_AMOUNT;
    this.misc = RETURN_AMOUNT;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {

    AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.magicNumber,
        new PenaltyCardInfo.GainPenaltyCardsAction(PENALTY_CARD_GAIN, true)));
    AbstractDungeon.actionManager.addToBottom(new PutOnDeckAction(p, p, this.misc, false));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Backslide();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(DRAW_AMOUNT_UPGRADE);
      this.misc += RETURN_AMOUNT_UPGRADE;
      this.rawDescription = getDescription(this.misc);
      initializeDescription();
    }
  }

  public static String getDescription(int returnAmount) {
    return DESCRIPTION
        + (returnAmount == 1 ? EXTENDED_DESCRIPTION[0] : returnAmount + EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2];
  }

  private static List<AbstractTooltipKeyword> EXTRA_KEYWORDS = Arrays.asList(
      CustomTooltipKeywords.getTooltipKeyword(CustomTooltipKeywords.PENALTY_CARD)
  );

  @Override
  public List<TooltipInfo> getCustomTooltips() {
    return TooltipKeywords.getTooltipInfos(EXTRA_KEYWORDS);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}