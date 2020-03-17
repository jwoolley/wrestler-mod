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
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import org.apache.commons.lang3.StringUtils;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class CannedHeat extends CustomCard {
  public static final String ID = "WrestlerMod:CannedHeat";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cannedheat.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  private static final int ENERGY_AMOUNT = 1;
  private static final int CARD_DRAW_AMOUNT = 1;
  private static final int CARD_DRAW_AMOUNT_UPGRADE = 1;

  public CannedHeat() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(ENERGY_AMOUNT, CARD_DRAW_AMOUNT), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber =  CARD_DRAW_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new PenaltyCardInfo.GainPenaltyCardsAction(1));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new EnergizedPower(p, ENERGY_AMOUNT), ENERGY_AMOUNT));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, this.magicNumber), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new CannedHeat();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(CARD_DRAW_AMOUNT_UPGRADE);
      this.rawDescription = getDescription(ENERGY_AMOUNT, this.magicNumber);
      initializeDescription();
    }
  }

  public static String getDescription(int energyAmount, int drawAmount) {
    return DESCRIPTION
        + StringUtils.repeat(EXTENDED_DESCRIPTION[0], energyAmount)
        + EXTENDED_DESCRIPTION[1]
        + (drawAmount == 1 ? EXTENDED_DESCRIPTION[2] : EXTENDED_DESCRIPTION[3]);
  }

  private static List<AbstractTooltipKeyword> EXTRA_KEYWORDS = Arrays.asList(
      CustomTooltipKeywords.getTooltipKeyword(CustomTooltipKeywords.PENALTY_CARD)
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