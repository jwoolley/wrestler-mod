package thewrestler.cards.colorless.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import thewrestler.WrestlerMod;

public class PowderPotionUpgraded extends PowderPotion {
  public static final String ID = WrestlerMod.makeID("PowderPotion");
  public static final String NAME;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "powderpotion2.png";

  private static final CardStrings cardStrings;

  private static final int POTION_AMOUNT = 2;

  public PowderPotionUpgraded() {
    super(NAME, IMG_PATH, POTION_AMOUNT);
  }

  public AbstractCard makeCopy() {
    return new PowderPotionUpgraded();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    NAME = EXTENDED_DESCRIPTION[0];
  }
}