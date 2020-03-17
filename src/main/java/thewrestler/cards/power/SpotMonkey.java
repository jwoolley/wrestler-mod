package thewrestler.cards.power;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.powers.OpportunistPower;
import thewrestler.powers.ProvenTacticsPower;
import thewrestler.powers.SpotMonkeyPower;
import thewrestler.signaturemoves.upgrades.UpgradeType;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class SpotMonkey extends CustomCard {
  public static final String ID = WrestlerMod.makeID("SpotMonkey");

  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "spotmonkey.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = 1;
  private static final int STRENGTH_AMOUNT = 1;
  private static final int STRENGTH_AMOUNT_UPGRADE = 1;

  public SpotMonkey() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = STRENGTH_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new SpotMonkeyPower(p, this.magicNumber), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new SpotMonkey();
  }

  private static List<AbstractTooltipKeyword> EXTRA_KEYWORDS = Arrays.asList(
      CustomTooltipKeywords.getTooltipKeyword(CustomTooltipKeywords.PENALTY_CARD)
  );

  @Override
  public List<TooltipInfo> getCustomTooltips() {
    return TooltipKeywords.getTooltipInfos(EXTRA_KEYWORDS);
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      initializeDescription();
      this.upgradeMagicNumber(STRENGTH_AMOUNT_UPGRADE);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}