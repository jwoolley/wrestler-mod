package thewrestler.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.AbstractCardWithPreviewCard;
import thewrestler.cards.colorless.attack.Elbow;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Hammerlock extends AbstractCardWithPreviewCard {
  public static final String ID = "WrestlerMod:Hammerlock";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "hammerlock.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;
  private static AbstractCard PREVIEW_CARD;

  private static final int BLOCK_AMOUNT = 10;
  private static final int BLOCK_AMOUNT_UPGRADE = 3;
  private static final int CARD_AMOUNT = 2;
  private static final int COST = 2;

  public Hammerlock() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(CARD_AMOUNT), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    this.baseMagicNumber = this.magicNumber = CARD_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Elbow(), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Hammerlock();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_AMOUNT_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber);
      initializeDescription();
    }
  }

  @Override
  public AbstractCard getPreviewCard() {
    return getBonusCard();
  }

  private static AbstractCard getBonusCard() {
    if (PREVIEW_CARD == null) {
      PREVIEW_CARD = new Elbow();
    }
    return PREVIEW_CARD;
  }


  public static String getDescription(int numCards) {
    return DESCRIPTION
        + (numCards == 1 ? EXTENDED_DESCRIPTION[0] : numCards + EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}