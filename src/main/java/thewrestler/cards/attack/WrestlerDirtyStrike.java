package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.util.CardUtil;

public class WrestlerDirtyStrike extends WrestlerStrike {
  public static final String ID = WrestlerMod.makeID("WrestlerDirtyStrike");
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "dirtystrike.png";

  private static final int ADDITIONAL_BASE_DAMAGE = 0;

  private static final CardStrings cardStrings;

  public WrestlerDirtyStrike() {
    super(ID, NAME, DESCRIPTION, IMG_PATH);
    this.baseDamage = this.damage  = this.baseDamage + ADDITIONAL_BASE_DAMAGE;
    setDirtyCardAttackFrame(this);
    CardUtil.makeCardDirty(this);
  }

  static void setDirtyCardAttackFrame(CustomCard card) {
    card.setBackgroundTexture(WrestlerMod.ATTACK_WRESTLER_DIRTY_ORANGE, WrestlerMod.ATTACK_DEFAULT_DIRTY_ORANGE_PORTRAIT);
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