package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.cards.attack.TakeToTheMatAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class TakeToTheMat extends CustomCard {
  public static final String ID = "WrestlerMod:TakeToTheMat";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "taketothemat.png";

  private static final int HP_PERCENT_THRESHOLD = 50;
  private static final int HP_PERCENT_THRESHOLD_UPGRADE = 10;

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.BASIC;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 2;
  private static final int DAMAGE = 10;
  private static final int DAMAGE_UPGRADE = 2;

  public TakeToTheMat() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = HP_PERCENT_THRESHOLD;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new TakeToTheMatAction(m, this.damage, HP_PERCENT_THRESHOLD));
  }

  @Override
  public AbstractCard makeCopy() {
    return new TakeToTheMat();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.upgradeMagicNumber(HP_PERCENT_THRESHOLD_UPGRADE);
      this.rawDescription = getDescription();
      initializeDescription();
    }
  }
  public static String getDescription() {
    return DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}