package thewrestler.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.GreenPenaltyStatusCard;

public class GreenPenaltyOrb extends  BasePenaltyOrb {
  public static final String ORB_ID = WrestlerMod.makeID("GreenPenaltyOrb");
  private static final OrbStrings ORB_STRINGS = CardCrawlGame.languagePack.getOrbString(ORB_ID);
  private static final String IMG_NAME = "green.png";
  private static final AbstractPenaltyStatusCard PENALTY_CARD = new GreenPenaltyStatusCard();

  public GreenPenaltyOrb() {
    super(ORB_ID, ORB_STRINGS.NAME, ORB_STRINGS.DESCRIPTION, IMG_NAME, (AbstractPenaltyStatusCard) PENALTY_CARD.makeCopy());
  }
  @Override
  public AbstractOrb makeCopy() {
    return new GreenPenaltyOrb();
  }
}
