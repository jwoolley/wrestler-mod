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
import thewrestler.cards.colorless.attack.Elbow;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class ElbowGrease extends CustomCard {
  public static final String ID = WrestlerMod.makeID("ElbowGrease");
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "elbowgrease.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.SPECIAL;
  private static final CardTarget TARGET = CardTarget.NONE;
  private static AbstractCard PREVIEW_CARD;

  private static final int COST = -2;

  public ElbowGrease() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(Elbow.NAME), TYPE,
        CardColor.COLORLESS, RARITY, TARGET);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    onChoseThisOption();
  }

  public void onChoseThisOption() {
    AbstractCard elbowCard = getBonusCard().makeCopy();
    if (this.upgraded) {
      elbowCard.upgrade();
    }
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(elbowCard));
  }

  @Override
  public AbstractCard makeCopy() {
    return new ElbowGrease();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      AbstractCard bonusCard = getBonusCard().makeStatEquivalentCopy();
      bonusCard.upgrade();
      this.rawDescription = getDescription(bonusCard.name);
      initializeDescription();
    }
  }

  private static String getDescription(String bonusCardName) {
    return DESCRIPTION + bonusCardName + EXTENDED_DESCRIPTION[0];
  }

  private static AbstractCard getBonusCard() {
    if (PREVIEW_CARD == null) {
      PREVIEW_CARD = new Elbow();
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