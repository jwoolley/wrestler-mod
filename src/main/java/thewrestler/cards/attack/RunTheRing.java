package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.cards.attack.RunTheRingAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class RunTheRing extends CustomCard {
  public static final String ID = "WrestlerMod:RunTheRing";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "runthering.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE_PER_TRIGGER = 5;

  private static final int NUM_DISCARDED_AND_ATTACKS = 2;
  private static final int NUM_DISCARDED_AND_ATTACKS_UPGRADE = 1;

  public RunTheRing() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(NUM_DISCARDED_AND_ATTACKS), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE_PER_TRIGGER;
    this.baseMagicNumber = this.magicNumber = NUM_DISCARDED_AND_ATTACKS;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber, true));

      for (int i = 0; i < this.magicNumber; i++) {
        AbstractDungeon.actionManager.addToBottom(new AttackDamageRandomEnemyAction(this,
            AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new RunTheRing();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_DISCARDED_AND_ATTACKS_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber);
      initializeDescription();
    }
  }

  private static String getDescription(int numTriggers) {
    return DESCRIPTION + (numTriggers == -1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1]);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}