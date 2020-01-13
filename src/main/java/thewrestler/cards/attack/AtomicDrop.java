package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.IrradiatedPower;
import thewrestler.util.CreatureUtils;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class AtomicDrop extends CustomCard {
  public static final String ID = "WrestlerMod:AtomicDrop";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "atomicdrop.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 2;
  private static final int DAMAGE = 10;
  private static final int POWER_STACKS = 1;
  private static final int POWER_STACKS_UPGRADE = 1;

  public AtomicDrop() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(false), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = POWER_STACKS;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToTop(new SFXAction("BOOM_LOWFREQ_1"));

    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.BLUNT_HEAVY));

    if (this.upgraded) {
      CreatureUtils.getLivingMonsters().forEach(mo ->  AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(mo, p, new IrradiatedPower(m, this.magicNumber), this.magicNumber)));
    } else {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(m, p, new IrradiatedPower(m, this.magicNumber), this.magicNumber));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new AtomicDrop();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.rawDescription = getDescription(true);
      this.initializeDescription();
    }
  }

  private static String getDescription(boolean upgraded) {
    return DESCRIPTION
        + (!upgraded ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2]
        + (!upgraded ? EXTENDED_DESCRIPTION[3] : EXTENDED_DESCRIPTION[4])
        + EXTENDED_DESCRIPTION[5];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}