package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class WindUpKick extends CustomCard {
  public static final String ID = "WrestlerMod:WindUpKick";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "windupkick.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 0;
  private static final int DAMAGE = 9;
  private static final int DAMAGE_UPGRADE = 3;
  private static final int NUM_SKILLS_REQUIRED = 2;

  public WindUpKick() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = NUM_SKILLS_REQUIRED;
    this.isMultiDamage = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAllEnemiesAction(p, this.multiDamage, this.damageType,
            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
  }

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    boolean canUse = super.canUse(p, m);

    if (!canUse) {
      return false;
    }

    if (canUse && getNumNonAttacksPlayed() <  this.magicNumber) {
      canUse = false;
      this.cantUseMessage = getCantPlayMessage();
    }
    return canUse;
  }

  @Override
  public AbstractCard makeCopy() {
    return new WindUpKick();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
    }
  }

  @Override
  public String getCantPlayMessage() {
    final int numSkillsRemaining = this.magicNumber - getNumNonAttacksPlayed();
    return EXTENDED_DESCRIPTION[0] + numSkillsRemaining + EXTENDED_DESCRIPTION[1]
        + (numSkillsRemaining == 1 ? EXTENDED_DESCRIPTION[2] : EXTENDED_DESCRIPTION[3]);
  }

  // TODO: move to helper/util class
  private static int getNumSkillsPlayed() {
    return (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
        .filter(c -> c.type == CardType.SKILL).count();
  }


  // TODO: move to helper/util class
  private static int getNumNonAttacksPlayed() {
    return (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
        .filter(c -> c.type != CardType.ATTACK).count();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}