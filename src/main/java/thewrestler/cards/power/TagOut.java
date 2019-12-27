package thewrestler.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import thewrestler.cards.AbstractCardWithPreviewCard;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class TagOut extends AbstractCardWithPreviewCard {
  public static final String ID = "WrestlerMod:TagOut";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "tagout.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER ;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;
  private static AbstractCard PREVIEW_CARD;
  private static AbstractCard UPGRADED_PREVIEW_CARD;

  private static final int COST = 1;
  private static final int DEX_GAIN = 2;
  private static final int DEX_GAIN_UPGRADE = 1;

  public TagOut() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(TagIn.NAME), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = DEX_GAIN;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
    p.hand.moveToBottomOfDeck(this.getPreviewCard().makeStatEquivalentCopy());
  }

  @Override
  public AbstractCard getPreviewCard() {
    return getPreviewCard(this.upgraded);
  }

  @Override
  public AbstractCard makeCopy() {
    return new TagOut();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(DEX_GAIN_UPGRADE);
      this.rawDescription = getDescription(this.getPreviewCard().name);
      initializeDescription();
    }
  }


  private static AbstractCard getPreviewCard(boolean upgraded) {
    if (upgraded) {
      if (UPGRADED_PREVIEW_CARD == null) {
        UPGRADED_PREVIEW_CARD = new TagIn();
        UPGRADED_PREVIEW_CARD.upgrade();
      }
      return UPGRADED_PREVIEW_CARD;
    }
    if (PREVIEW_CARD == null) {
      PREVIEW_CARD = new TagIn();
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

    PREVIEW_CARD = new TagIn();
  }
}