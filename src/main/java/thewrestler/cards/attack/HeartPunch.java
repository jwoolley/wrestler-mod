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
import com.megacrit.cardcrawl.powers.ChokePower;
import thewrestler.actions.cards.attack.HeartPunchAction;
import thewrestler.actions.cards.attack.SharpshooterAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class HeartPunch extends CustomCard {
  public static final String ID = "WrestlerMod:HeartPunch";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "heartpunch.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 12;
  private static final int DAMAGE_UPGRADE = 1;
  private static final int HP_PER_DEBUFF = 3;
  private static final int HP_PER_DEBUFF_UPGRADE = 1;

  // TODO: Add Keyword for Choked

  public HeartPunch() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(true), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);

    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = HP_PER_DEBUFF;
    this.exhaust = true;

    this.tags.add(AbstractCard.CardTags.HEALING);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.BLUNT_HEAVY));

    AbstractDungeon.actionManager.addToBottom(new HeartPunchAction(m, p, this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new HeartPunch();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.upgradeMagicNumber(HP_PER_DEBUFF_UPGRADE);
      initializeDescription();
    }
  }

  public static String getDescription(boolean isEthereal) {
    return DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}