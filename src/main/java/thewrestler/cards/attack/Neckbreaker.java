package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.actions.ReduceBlockAction;
import thewrestler.powers.GrappledPower;
import thewrestler.powers.SquaringOffPower;
import thewrestler.powers.WrestlerShackled;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Neckbreaker extends CustomCard {
  public static final String ID = "WrestlerMod:Neckbreaker";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "neckbreaker.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 8;
  private static final int DAMAGE_UPGRADE = 3;
  private static final int BLOCK_REDUCE_AMOUNT = 8;
  private static final int BLOCK_REDUCE_AMOUNT_UPGRADE = 3;

  public Neckbreaker() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = BLOCK_REDUCE_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.BLUNT_LIGHT));

    // TODO: check that this interacts as expected with Square Off grappled trigger
    if (m.hasPower(GrappledPower.POWER_ID) ||
        m.hasPower(SquaringOffPower.POWER_ID) && m.getPower(SquaringOffPower.POWER_ID).amount == 1) {
      AbstractDungeon.actionManager.addToTop(new WaitAction(0.5f));
      AbstractDungeon.actionManager.addToBottom(new ReduceBlockAction(m, p, this.magicNumber));
      AbstractDungeon.actionManager.addToTop(new WaitAction(0.3f));
      AbstractDungeon.actionManager.addToBottom(
          new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
              AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Neckbreaker();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.upgradeMagicNumber(BLOCK_REDUCE_AMOUNT_UPGRADE);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}