package thewrestler.cards.power;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.IronManHealPower;
import thewrestler.powers.IronManPower;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class IronMan extends CustomCard {
  public static final String ID = WrestlerMod.makeID("IronMan");

  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "ironman.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = 1;
  private static final int HEAL_AMOUNT = 5;
  private static final int HEAL_AMOUNT_UPGRADE = 2;

  public IronMan() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = HEAL_AMOUNT;
    this.tags.add(AbstractCard.CardTags.HEALING);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new IronManPower(p, 1), 1));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new IronManHealPower(p, this.magicNumber), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new IronMan();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(HEAL_AMOUNT_UPGRADE);
      initializeDescription();
    }
  }

  public static String getDescription() {
    return DESCRIPTION + IronManPower.HP_THRESHOLD + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}