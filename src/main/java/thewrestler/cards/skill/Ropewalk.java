package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.cards.skill.RopewalkAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Ropewalk extends CustomCard {
  public static final String ID = "WrestlerMod:Ropewalk";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "ropewalk.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int BLOCK_AMOUNT = 11;
  private static final int COST = 2;

  public Ropewalk() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(false), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    AbstractDungeon.actionManager.addToBottom(new RopewalkAction(this.upgraded));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Ropewalk();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.rawDescription = getDescription(true);
      initializeDescription();
    }
  }
  public static String getDescription(boolean isUpgraded) {
    return DESCRIPTION + (!isUpgraded ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1]);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}