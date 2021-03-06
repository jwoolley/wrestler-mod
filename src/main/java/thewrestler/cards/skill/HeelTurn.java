package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.apache.commons.lang3.StringUtils;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class HeelTurn extends CustomCard {
  public static final String ID = "WrestlerMod:HeelTurn";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "heelturn.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 3;

  private static final int INTANGIBLE_AMOUNT = 1;
  private static final int STRENGTH_AMOUNT = 2;
  private static final int STRENGTH_AMOUNT_UPGRADE = 1;
  private static final int PENALTY_CARD_GAIN = 2;

  public HeelTurn() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(PENALTY_CARD_GAIN), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = STRENGTH_AMOUNT;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new SFXAction("BOO_CROWD_1"));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, INTANGIBLE_AMOUNT), INTANGIBLE_AMOUNT));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));

    PenaltyCardInfo.gainPenaltyCards(2);
  }

  @Override
  public AbstractCard makeCopy() {
    return new HeelTurn();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(STRENGTH_AMOUNT_UPGRADE);
      this.rawDescription = getDescription(PENALTY_CARD_GAIN);
      initializeDescription();
    }
  }

  public static String getDescription(int penaltyCardAmount) {
    return DESCRIPTION + INTANGIBLE_AMOUNT + EXTENDED_DESCRIPTION[0]
        +  StringUtils.repeat(EXTENDED_DESCRIPTION[1], penaltyCardAmount)
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