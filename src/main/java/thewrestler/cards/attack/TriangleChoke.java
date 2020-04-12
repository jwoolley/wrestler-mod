package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import thewrestler.actions.power.ApplyGrappledAction;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.BravadoPower;
import thewrestler.util.info.CombatInfo;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class TriangleChoke extends CustomCard {
  public static final String ID = "WrestlerMod:TriangleChoke";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "trianglechoke.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 7;
  private static final int BLOCK = 7;
  private static final int DAMAGE__AND_BLOCK_UPGRADE = 3;
  private static final int NUM_BRAVADO = 1;
  private static final int MIN_NONATTACK_CARDS = 2;

  public TriangleChoke() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(MIN_NONATTACK_CARDS), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseBlock = this.block = BLOCK;
    this.baseMagicNumber = this.magicNumber = NUM_BRAVADO;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));

    if (willTrigger()) {
      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(p, p, new BravadoPower(p, this.magicNumber), this.magicNumber));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new TriangleChoke();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE__AND_BLOCK_UPGRADE);
      this.upgradeBlock(DAMAGE__AND_BLOCK_UPGRADE);
      this.rawDescription = getDescription(MIN_NONATTACK_CARDS);
      this.initializeDescription();
    }
  }

  private boolean willTrigger() {
    return CombatInfo.getNumNonAttacksPlayed() >= MIN_NONATTACK_CARDS;
  }

  public void triggerOnGlowCheck() {
    if (willTrigger()) {
      this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
    } else {
      this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }
  }

  private static String getDescription(int minNonAttacks) {
    return DESCRIPTION + minNonAttacks + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}