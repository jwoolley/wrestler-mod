package thewrestler.cards.attack;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;

public class WrestlerDirtyStrike extends WrestlerStrike {
  public static final String ID = WrestlerMod.makeID("WrestlerDirtyStrike");
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "dirtystrike.png";

  private static final int ADDITIONAL_BASE_DAMAGE = 1;

  private static final CardStrings cardStrings;

  public WrestlerDirtyStrike() {
    super(ID, NAME, DESCRIPTION, IMG_PATH);
    this.baseDamage = this.damage  = this.baseDamage + ADDITIONAL_BASE_DAMAGE;
    tags.add(WrestlerCardTags.DIRTY);
  }

  public AbstractCard makeCopy() {
    return new WrestlerDirtyStrike();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME =  cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}