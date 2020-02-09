package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.cards.skill.SideRollAction;
import thewrestler.cards.colorless.attack.Elbow;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Hammerlock extends CustomCard {
  public static final String ID = "WrestlerMod:Hammerlock";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "hammerlock.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int BLOCK_AMOUNT = 13;
  private static final int BLOCK_AMOUNT_UPGRADE = 1;
  private static final int CARD_AMOUNT = 1;
  private static final int CARD_AMOUNT_UPGRADE = 1;
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

    AbstractDungeon.actionManager.addToBottom(
        new MakeTempCardInDrawPileAction(new Elbow(), this.magicNumber, false, true, false));
  }

  @Override
  public AbstractCard makeCopy() {
    return new SideRoll();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_AMOUNT_UPGRADE);
      this.upgradeMagicNumber(CARD_AMOUNT_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber);
      initializeDescription();
    }
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