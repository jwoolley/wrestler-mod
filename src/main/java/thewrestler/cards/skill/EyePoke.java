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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.powers.InjuredPower;

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
  private static final int VULNERABLE_AMOUNT = 1;

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.BASIC;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 0;

  public EyePoke() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);

    // Using baseBlock (and overriding applyPowersToBlock) as a hack so value is highlighted in upgrade UI (a la Wish)
    this.misc = this.baseBlock = VULNERABLE_AMOUNT;
    this.baseMagicNumber = this.magicNumber = WEAK_AMOUNT;
    this.exhaust = true;
    tags.add(WrestlerCardTags.DIRTY);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new VulnerablePower(m, this.misc, false), this.misc));
  }

  @Override
  public AbstractCard makeCopy() {
    return new EyePoke();
  }

  @Override
  public void applyPowersToBlock() {
    this.baseBlock = this.block = this.misc = VULNERABLE_AMOUNT;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(WEAK_AMOUNT_UPGRADE);
      this.misc = this.baseBlock = VULNERABLE_AMOUNT;
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

  private static List<AbstractTooltipKeyword> EXTRA_KEYWORDS = Arrays.asList(
      CustomTooltipKeywords.getTooltipKeyword(CustomTooltipKeywords.PENALTY_CARD_BLUE)
  );

  @Override
  public List<TooltipInfo> getCustomTooltips() {
    return TooltipKeywords.getTooltipInfos(EXTRA_KEYWORDS);
  }
}