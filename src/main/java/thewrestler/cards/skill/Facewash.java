package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.cards.skill.FacewashAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Facewash extends CustomCard {
  public static final String ID = "WrestlerMod:Facewash";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "facewash.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int CARD_DRAW_AMOUNT = 2;
  private static final int BLOCK_AMOUNT_PER_CARD = 3;
  private static final int CARD_DRAW_AMOUNT_UPGRADE = 1;
  private static final int COST = 1;

  public Facewash() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(CARD_DRAW_AMOUNT), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = CARD_DRAW_AMOUNT;
    this.baseBlock = this.block = BLOCK_AMOUNT_PER_CARD;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DrawCardAction(this.magicNumber, new FacewashAction(this.block)));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Facewash();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(CARD_DRAW_AMOUNT_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber);
      initializeDescription();
    }
  }
  public static String getDescription(int numCards) {
    return DESCRIPTION + (numCards == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1]) + EXTENDED_DESCRIPTION[2];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}