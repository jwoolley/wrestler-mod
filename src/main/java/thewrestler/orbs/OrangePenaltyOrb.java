package thewrestler.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.OrangePenaltyStatusCard;

public class OrangePenaltyOrb extends  BasePenaltyOrb {
  public static final String ORB_ID = WrestlerMod.makeID("OrangePenaltyOrb");
  private static final OrbStrings ORB_STRINGS = CardCrawlGame.languagePack.getOrbString(ORB_ID);
  private static final String IMG_NAME = "orange.png";
  private static final AbstractPenaltyStatusCard PENALTY_CARD = new OrangePenaltyStatusCard();

  public OrangePenaltyOrb() {
    super(ORB_ID, ORB_STRINGS.NAME, ORB_STRINGS.DESCRIPTION, IMG_NAME, (AbstractPenaltyStatusCard) PENALTY_CARD.makeCopy());
  }
  @Override
  public AbstractOrb makeCopy() {
    return new OrangePenaltyOrb();
  }
}