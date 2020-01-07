package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class BlowOff extends CustomCard {
  public static final String ID = "WrestlerMod:BlowOff";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "blowoff.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int BLOCK_AMOUNT = 6;
  private static final int DISCARD_AMOUNT = 2;
  private static final int DISCARD_AMOUNT_UPGRADE = -1;
  private static final int COST = 0;

  public BlowOff() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(DISCARD_AMOUNT), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    this.baseMagicNumber = this.magicNumber = DISCARD_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, this.magicNumber, false));
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
  }

  @Override
  public AbstractCard makeCopy() {
    return new BlowOff();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();

      if (this.magicNumber + DISCARD_AMOUNT_UPGRADE > -1) {
        this.upgradeMagicNumber(DISCARD_AMOUNT_UPGRADE);
      }
    }
  }
  public static String getDescription(int numCards) {
    return DESCRIPTION
        + (numCards == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}