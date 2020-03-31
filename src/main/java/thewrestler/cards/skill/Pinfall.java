package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Pinfall extends CustomCard {
  public static final String ID = "WrestlerMod:Pinfall";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "pinfall.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int BLOCK_AMOUNT = 3;
  private static final int BLOCK_AMOUNT_UPGRADE = 2;
  private static final int COST = 0;

  public Pinfall() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    this.selfRetain = false;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    this.selfRetain = false;
  }

  @Override
  public void triggerOnEndOfPlayerTurn() {
    super.triggerOnEndOfPlayerTurn();
    if (this.selfRetain) {
      this.flash();
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Pinfall();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_AMOUNT_UPGRADE);
      this.rawDescription = getDescription();
      initializeDescription();
    }
  }

  public static String getDescription() {
    return DESCRIPTION;
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

  @Override
  public void onPlayCard(AbstractCard card, AbstractMonster m) {
    if (card.hasTag(WrestlerCardTags.PENALTY)) {
      AbstractDungeon.actionManager.addToBottom(new DiscardToHandAction(this));
    }
  }
}