package thewrestler.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.AbstractCardWithPreviewCard;
import thewrestler.cards.attack.TornadoTag;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class SafetyTag extends AbstractCardWithPreviewCard {
  public static final String ID = "WrestlerMod:SafetyTag";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "safetytag.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;
  private static AbstractCard BASE_PREVIEW_CARD;
  private static AbstractCard UPGRADED_PREVIEW_CARD;


  private static final int COST = 1;
  private static final int BLOCK_AMOUNT = 7;
  private static final int BLOCK_AMOUNT_UPGRADE = 2;

  public SafetyTag() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(TornadoTag.NAME), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    AbstractDungeon.actionManager.addToBottom(
        new MakeTempCardInDiscardAction(this.getPreviewCard().makeStatEquivalentCopy(), 1));
  }

  @Override
  public AbstractCard getPreviewCard() {
    return getPreviewCard(this.upgraded);
  }

  @Override
  public AbstractCard makeCopy() {
    return new SafetyTag();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_AMOUNT_UPGRADE);
      this.rawDescription = getDescription(this.getPreviewCard().name);
      initializeDescription();
    }
  }

  private static AbstractCard getPreviewCard(boolean upgraded) {
    if (!upgraded) {
      if (BASE_PREVIEW_CARD == null) {
        BASE_PREVIEW_CARD = new TornadoTag();
      }
      return BASE_PREVIEW_CARD;
    } else {
      if (UPGRADED_PREVIEW_CARD == null) {
        UPGRADED_PREVIEW_CARD = new TornadoTag();
        UPGRADED_PREVIEW_CARD.upgrade();
      }
      return UPGRADED_PREVIEW_CARD;
    }
  }

  public static String getDescription(String otherCardName) {
    return DESCRIPTION + otherCardName + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    BASE_PREVIEW_CARD = new TornadoTag();
    UPGRADED_PREVIEW_CARD = new TornadoTag();
    UPGRADED_PREVIEW_CARD.upgrade();
  }
}