package thewrestler.cards.colorless.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.attack.Knee;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class FoulPlay extends CustomCard {
  public static final String ID = WrestlerMod.makeID("FoulPlay");
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "foulplay.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.SPECIAL;
  private static final CardTarget TARGET = CardTarget.NONE;
  private static AbstractCard PREVIEW_CARD;

  private static final int COST = -2;

  public FoulPlay() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(Knee.NAME), TYPE,
        CardColor.COLORLESS, RARITY, TARGET);
    this.cardsToPreview = new Knee();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    onChoseThisOption();
  }

  public void onChoseThisOption() {
    AbstractCard kneeCard = getBonusCard().makeCopy();
    if (this.upgraded) {
      kneeCard.upgrade();
    }
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(kneeCard));
  }

  @Override
  public AbstractCard makeCopy() {
    return new FairPlay();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      AbstractCard bonusCard = getBonusCard().makeStatEquivalentCopy();
      bonusCard.upgrade();
      this.rawDescription = getDescription(bonusCard.name);
      initializeDescription();

      this.cardsToPreview.upgrade();
    }
  }

  private static String getDescription(String bonusCardName) {
    return DESCRIPTION + bonusCardName + EXTENDED_DESCRIPTION[0];
  }

  private static AbstractCard getBonusCard() {
    if (PREVIEW_CARD == null) {
      PREVIEW_CARD = new Knee();
    }
    return PREVIEW_CARD;
  }


  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}