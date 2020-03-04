package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ChokePower;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import thewrestler.actions.cards.attack.SharpshooterAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Sharpshooter extends CustomCard {
  public static final String ID = "WrestlerMod:Sharpshooter";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "sharpshooter.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE_PER_TRIGGER = 3;
  private static final int DEBUFF_STACKS = 1;

  // TODO: Add Keyword for Choked

  public Sharpshooter() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(true), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE_PER_TRIGGER;
    this.baseMagicNumber = this.magicNumber = DEBUFF_STACKS;
    this.isEthereal = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new ConstrictedPower(m, p, 1), this.magicNumber));

    AbstractDungeon.actionManager.addToBottom(new SharpshooterAction(m, p, this.damage));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Sharpshooter();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.isEthereal = false;
      this.rawDescription = getDescription(false);
      initializeDescription();
    }
  }

  public static String getDescription(boolean isEthereal) {
    return (isEthereal ? EXTENDED_DESCRIPTION[0] : "") + DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}