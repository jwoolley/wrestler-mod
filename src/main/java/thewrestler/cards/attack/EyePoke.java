package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.CardUtil;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class EyePoke extends CustomCard {
  public static final String ID = "WrestlerMod:EyePoke";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "eyepoke.png";


  private static final int DAMAGE = 3;
  private static final int BLOCK = 3;
  private static final int DAMAGE_UPGRADE = 2;
  private static final int BLOCK_UPGRADE = 2;
  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.BASIC;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 0;

  public EyePoke() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseBlock = this.block = BLOCK_UPGRADE;
    CardUtil.makeCardDirty(this);
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));

    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, this.block));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));

    if (this.upgraded) {
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(1));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new EyePoke();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.upgradeBlock(BLOCK_UPGRADE);
      this.rawDescription = getDescription();
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