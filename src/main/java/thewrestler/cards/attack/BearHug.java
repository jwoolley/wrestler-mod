package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.BearHugPower;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class BearHug extends CustomCard {
  public static final String ID = "WrestlerMod:BearHug";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "bearhug.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 6;
  private static final int DEBUFFS_PER_SKILL = 1;

  public BearHug() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(true), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = DEBUFFS_PER_SKILL;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new BearHugPower(m, this.magicNumber), this.magicNumber, true));
  }

  @Override
  public AbstractCard makeCopy() {
    return new BearHug();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.exhaust = false;
      this.rawDescription = getDescription(false);
      this.initializeDescription();
    }
  }

  public static String getDescription(boolean hasExhaust) {
    return DESCRIPTION + (hasExhaust ? EXTENDED_DESCRIPTION[0] : "");
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}