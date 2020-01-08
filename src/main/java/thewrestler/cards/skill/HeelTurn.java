package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.actions.GainGoldAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class HeelTurn extends CustomCard {
  public static final String ID = "WrestlerMod:HeelTurn";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "heelturn.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 3;

  private static final int INTANGIBLE_AMOUNT = 1;
  private static final int STRENGTH_AMOUNT = 2;
  private static final int UPGRADED_STRENGTH_AMOUNT = 1;
  private static final int GOLD_AMOUNT = 6;

  public HeelTurn() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = GOLD_AMOUNT;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new SFXAction("BOO_CROWD_1"));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, INTANGIBLE_AMOUNT), INTANGIBLE_AMOUNT));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new StrengthPower(p, STRENGTH_AMOUNT), STRENGTH_AMOUNT));

    AbstractDungeon.actionManager.addToBottom(new GainGoldAction(this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new HeelTurn();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(UPGRADED_STRENGTH_AMOUNT);
      this.rawDescription = getDescription();
      initializeDescription();
    }
  }

  public static String getDescription() {
    return DESCRIPTION + INTANGIBLE_AMOUNT + EXTENDED_DESCRIPTION[0] + STRENGTH_AMOUNT + EXTENDED_DESCRIPTION[1];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}