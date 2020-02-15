package thewrestler.cards.colorless.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import thewrestler.WrestlerMod;

public class PowderBufferUpgraded extends PowderBuffer {
  public static final String ID = WrestlerMod.makeID("PowderBuffer");
  public static final String NAME;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "powderbuffer2.png";

  private static final CardStrings cardStrings;

  private static final int BUFFER_AMOUNT = 2;

  public PowderBufferUpgraded() {
    super(NAME, IMG_PATH, BUFFER_AMOUNT);
  }

  public AbstractCard makeCopy() {
    return new PowderBufferUpgraded();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    NAME = EXTENDED_DESCRIPTION[0];
  }
}