package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class HairPull extends CustomCard {
  public static final String ID = "WrestlerMod:HairPull";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "hairpull.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 8;
  private static final int DAMAGE_UPGRADE = 3;

  private static final int MIN_DEBUFFS = 2;
  private static final int ENERGY_AND_CARDS_PER_TRIGGER = 1;

  public HairPull() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(MIN_DEBUFFS), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    tags.add(WrestlerCardTags.DIRTY);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.BLUNT_LIGHT));

    if (m.powers.stream().filter(mo -> mo.type == AbstractPower.PowerType.DEBUFF).count() >= MIN_DEBUFFS) {
      AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_AND_CARDS_PER_TRIGGER));
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, ENERGY_AND_CARDS_PER_TRIGGER));
      AbstractDungeon.actionManager.addToTop(new SFXAction("YELL_PAIN_1", 0.25f));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new HairPull();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.rawDescription = getDescription(MIN_DEBUFFS);
      initializeDescription();
    }
  }

  private static String getDescription(int minDebuffs) {
    return DESCRIPTION + minDebuffs + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}