package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.PenaltyLockedPower;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Butterfly extends CustomCard {
  public static final String ID = "WrestlerMod:Butterfly";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "butterfly.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 3;
  private static final int NUM_TRIGGERS = 2;
  private static final int NUM_TRIGGERS_UPGRADE = 1;

  public Butterfly() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = NUM_TRIGGERS;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    for (int i = 0; i < this.magicNumber; i++) {
      AbstractDungeon.actionManager.addToBottom(
          new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.POISON));
    }
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PenaltyLockedPower(1)));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Butterfly();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_TRIGGERS_UPGRADE);
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