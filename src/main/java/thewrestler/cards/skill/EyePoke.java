package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.powers.InjuredPower;
import thewrestler.util.CardUtil;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class EyePoke extends CustomCard {
  public static final String ID = "WrestlerMod:EyePoke";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "eyepoke.png";

  private static final int WEAK_AMOUNT = 1;
  private static final int WEAK_AMOUNT_UPGRADE  = 1;
  private static final int INJURED_AMOUNT = 2;
  private static final int INJURED_AMOUNT_UPGRADE = 1;

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.BASIC;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 0;

  public EyePoke() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);

    // Using baseBlock (and overriding applyPowersToBlock) as a hack so value is highlighted in upgrade UI (a la Wish)
    this.misc = this.baseBlock = INJURED_AMOUNT;
    this.baseMagicNumber = this.magicNumber = WEAK_AMOUNT;
    this.exhaust = true;
    CardUtil.makeCardDirty(this, this.type);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new InjuredPower(m, this.misc), this.misc));
  }

  @Override
  public AbstractCard makeCopy() {
    return new EyePoke();
  }

  @Override
  public void applyPowersToBlock() {
    this.baseBlock = this.block = this.misc = INJURED_AMOUNT + (this.upgraded ? INJURED_AMOUNT_UPGRADE : 0);
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(WEAK_AMOUNT_UPGRADE);
      this.misc = this.baseBlock = INJURED_AMOUNT + INJURED_AMOUNT_UPGRADE;
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