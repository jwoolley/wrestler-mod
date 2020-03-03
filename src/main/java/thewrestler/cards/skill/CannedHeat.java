package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.ChooseAndAddFilteredDiscardCardsToHandAction;
import thewrestler.actions.MoveRandomCardsFromDiscardToHandAction;
import thewrestler.actions.MoveRandomCardsFromDrawPileToHandAction;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class CannedHeat extends CustomCard {
  public static final String ID = "WrestlerMod:CannedHeat";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cannedheat.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  private static final int ENERGY_PER_PENALTY_CARD = 1;
  private static final int NUM_CARDS_DISCARDED = 2;
  private static final int NUM_CARDS_DISCARDED_UPGRADE = -1;

  public CannedHeat() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(NUM_CARDS_DISCARDED), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = NUM_CARDS_DISCARDED;
    this.tags.add(WrestlerCardTags.DIRTY);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DiscardAction(p, p, this.magicNumber, false));

    if (SportsmanshipInfo.isUnsporting()) {
      AbstractDungeon.actionManager.addToBottom(
          new GainEnergyAction(ENERGY_PER_PENALTY_CARD * SportsmanshipInfo.getAmount()));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new CannedHeat();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_CARDS_DISCARDED_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber);
      initializeDescription();
    }
  }

  public static String getDescription(int numCardsDiscarded) {
    return DESCRIPTION
        + (numCardsDiscarded == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
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