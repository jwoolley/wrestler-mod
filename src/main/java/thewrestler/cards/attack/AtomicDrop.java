package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.IrradiatedPower;

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
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 2;
  private static final int DAMAGE = 10;
  private static final int DAMAGE_UPGRADE = 5;
  private static final int POWER_STACKS = 1;

  public AtomicDrop() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(POWER_STACKS), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = POWER_STACKS;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToTop(new SFXAction("BOOM_LOWFREQ_1"));

    AbstractDungeon.actionManager.addToBottom(
        new DamageAllEnemiesAction(AbstractDungeon.player,
            DamageInfo.createDamageMatrix(this.damage, true, true),
            DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));

    AbstractDungeon.getCurrRoom().monsters.monsters.stream()
        .filter(mo -> !mo.isDying && !mo.isDeadOrEscaped())
        .forEach(mo -> {
          AbstractDungeon.effectList.add(
              new FlashAtkImgEffect(mo.hb.cX, mo.hb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY));

//          AbstractDungeon.actionManager.addToBottom(
//              new DamageAction(mo, new DamageInfo(p, damage,  DamageInfo.DamageType.NORMAL),
//                  AbstractGameAction.AttackEffect.NONE));

          AbstractDungeon.actionManager.addToBottom(
              new ApplyPowerAction(mo, p, new IrradiatedPower(mo, this.magicNumber), this.magicNumber));
        });
  }

  @Override
  public AbstractCard makeCopy() {
    return new AtomicDrop();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber);
      this.initializeDescription();
    }
  }

  private static String getDescription(int numStacks) {
    return DESCRIPTION
        + (numStacks == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}