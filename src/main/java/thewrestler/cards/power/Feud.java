package thewrestler.cards.power;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.cards.AbstractCardWithPreviewCard;
import thewrestler.cards.colorless.attack.Elbow;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.FeudPower;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Feud extends AbstractCardWithPreviewCard {
  public static final String ID = WrestlerMod.makeID("Feud");

  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "feud.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.NONE;
  private static AbstractCard PREVIEW_CARD;

  private static final int COST = 0;
  private static final int CARDS_PER_TURN = 1;

  public Feud() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(false), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new FeudPower(CARDS_PER_TURN), CARDS_PER_TURN));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Feud();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.isInnate = true;
      this.rawDescription = getDescription(true);
      initializeDescription();
    }
  }

  @Override
  public AbstractCard getPreviewCard() {
    return getBonusCard();
  }

  private static AbstractCard getBonusCard() {
    if (PREVIEW_CARD == null) {
      PREVIEW_CARD = new Elbow();
    }
    return PREVIEW_CARD;
  }

  public static String getDescription(boolean isInnate) {
    return (isInnate ? EXTENDED_DESCRIPTION[0] : "") + DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}