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
  private static final int DAMAGE = 13;
  private static final int NUM_SKILLS_REQUIRED = 3;
  private static final int NUM_SKILLS_REQUIRED_UPGRADE = -1;


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
  public boolean canPlay(AbstractCard card) {
    if (card == this) {
      card.cantUseMessage = getCanPlayMessage();
      return getNumSkillsPlayed() >=  this.magicNumber;
    } else {
      return true;
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new WindUpKick();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_SKILLS_REQUIRED_UPGRADE);
    }
  }

  private String getCanPlayMessage() {
    final int numSkillsRemaining = this.magicNumber - getNumSkillsPlayed();
    return EXTENDED_DESCRIPTION[0] + numSkillsRemaining + EXTENDED_DESCRIPTION[1]
        + (numSkillsRemaining == 1 ? EXTENDED_DESCRIPTION[2] : EXTENDED_DESCRIPTION[3]);
  }

  // TODO: move to helper/util class
  private static int getNumSkillsPlayed() {
    return (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
        .filter(c -> c.type == CardType.SKILL).count();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}