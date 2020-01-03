package thewrestler.cards.signaturemoves.abstractions.moveinfos;

import thewrestler.cards.signaturemoves.abstractions.cards.AbstractSignatureMoveCard;
import thewrestler.cards.signaturemoves.abstractions.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.cards.signaturemoves.abstractions.upgrades.UpgradeType;

import java.util.Map;

public class ChokeslamMoveInfo extends AbstractSignatureMoveInfo {
  private static final int GRAPPLES_REQUIRED = 3;
  private int grappledCount = 0;

  public ChokeslamMoveInfo(AbstractSignatureMoveCard signatureMoveCard, Map<AbstractSignatureMoveUpgrade, Integer> upgrades) {
    super(signatureMoveCard, upgrades);
  }

  @Override
  public void onCardPlayed() {

  }

  @Override
  public void atStartOfTurn() {

  }

  @Override
  public void atStartOfCombat() {

  }

  @Override
  public void upgradeMove(UpgradeType type) {

  }

  @Override
  public void onEnemyGrappled() {
    grappledCount++;
    if (grappledCount >= GRAPPLES_REQUIRED) {
      triggerGainCard();
      this.grappledCount = 0;
    }
  }

  @Override
  public String getConditionText() {
    return "Apply Grappled 3 times in the same battle.";
  }

  @Override
  public String getDynamicConditionText() {
    return "Apply Grappled " + (GRAPPLES_REQUIRED - grappledCount) + " more times this combat.";
  }

  @Override
  public void triggerGainCard() {

  }
}
