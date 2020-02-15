package thewrestler.cards.colorless.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class PowderPotion extends CustomCard {
  public static final String ID = WrestlerMod.makeID("PowderPotion");
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "powderpotion1.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.SPECIAL;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = -2;
  private static final int POTION_AMOUNT = 1;

  public PowderPotion() {
    this(NAME, IMG_PATH, POTION_AMOUNT);
  }

  public PowderPotion(String name, String imgPath, int potionAmount) {
    super(ID, name, getCardResourcePath(imgPath), COST, getDescription(potionAmount), TYPE,
        CardColor.COLORLESS, RARITY, TARGET);
    this.magicNumber = this.baseMagicNumber = potionAmount;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    onChoseThisOption();
  }

  public void onChoseThisOption() {
    for (int i = 0; i < this.magicNumber; i++) {
      AbstractDungeon.actionManager.addToBottom(
          new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new PowderPotion();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(1);
      this.rawDescription = getDescription(this.magicNumber);
    }
  }

  private static String getDescription(int numPotions) {
    return (numPotions == 1 ? DESCRIPTION : EXTENDED_DESCRIPTION[1]);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}