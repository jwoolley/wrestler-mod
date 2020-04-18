package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
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
import com.megacrit.cardcrawl.powers.*;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.powers.GrappledPower;
import thewrestler.powers.InjuredPower;
import thewrestler.enums.AbstractCardEnum;

import java.util.Arrays;
import java.util.List;

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
  private static final int DAMAGE = 9;
  private static final int DAMAGE_UPGRADE = 3;
  private static final int DEBUFF_AMOUNT = 1;
  private static final int INJURED_AMOUNT = 6;

  public Neckbreaker() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(INJURED_AMOUNT), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = DEBUFF_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.BLUNT_LIGHT));

    if (GrappledPower.enemyWillHaveGrappleAfterAttack(m)) {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));

      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));

      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(m, p, new InjuredPower(m, p, INJURED_AMOUNT), INJURED_AMOUNT));
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
    }
  }

  private static String getDescription(int injuredAmount) {
    return DESCRIPTION + injuredAmount + EXTENDED_DESCRIPTION[0];
  }

  private static List<AbstractTooltipKeyword> EXTRA_KEYWORDS = Arrays.asList(
      CustomTooltipKeywords.getTooltipKeyword(CustomTooltipKeywords.PERSISTENT_INJURY)
  );

  @Override
  public List<TooltipInfo> getCustomTooltips() {
    return TooltipKeywords.getTooltipInfos(EXTRA_KEYWORDS);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}