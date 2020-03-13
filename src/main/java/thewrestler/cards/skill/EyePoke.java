package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.SprainPower;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class EyePoke extends CustomCard {
  public static final String ID = "WrestlerMod:EyePoke";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "eyepoke.png";

  private static final int DEBUFF_AMOUNT = 1;
  private static final int DEBUFF_AMOUNT_UPGRADE  = 1;

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.BASIC;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 0;

  public EyePoke() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(true), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = DEBUFF_AMOUNT;
    tags.add(WrestlerCardTags.DIRTY);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, true), this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new SprainPower(m, this.magicNumber), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new EyePoke();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(DEBUFF_AMOUNT_UPGRADE);
      this.exhaust = false;
      this.rawDescription = getDescription(false);
      initializeDescription();
    }
  }
  public static String getDescription(boolean exhaust) {
    return DESCRIPTION + (exhaust ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1]);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}