package thewrestler.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.cards.AbstractCardWithPreviewCard;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class TagIn extends AbstractCardWithPreviewCard {
  public static final String ID = "WrestlerMod:TagIn";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "tagin.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;
  private static AbstractCard PREVIEW_CARD;

  private static final int COST = 1;
  private static final int STRENGTH_GAIN = 2;
  private static final int STRENGTH_GAIN_UPGRADE = 1;

  public TagIn() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(TagOut.NAME), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = STRENGTH_GAIN;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(
        new MakeTempCardInDiscardAction(this.getPreviewCard().makeStatEquivalentCopy(), 1));
  }

  @Override
  public AbstractCard getPreviewCard() {
    return getPreviewCard(this.upgraded);
  }

  @Override
  public AbstractCard makeCopy() {
    return new TagIn();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(STRENGTH_GAIN_UPGRADE);
      this.rawDescription = getDescription(this.getPreviewCard().name);
      initializeDescription();
    }
  }

  private static AbstractCard getPreviewCard(boolean upgraded) {
    if (PREVIEW_CARD == null) {
      PREVIEW_CARD = new TagOut();
    }
    return PREVIEW_CARD;
  }

  public static String getDescription(String otherCardName) {
    return DESCRIPTION + otherCardName + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    PREVIEW_CARD = new TagOut();
  }
}