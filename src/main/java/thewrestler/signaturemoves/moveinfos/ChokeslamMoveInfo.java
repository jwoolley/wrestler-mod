package thewrestler.signaturemoves.moveinfos;

import thewrestler.signaturemoves.cards.Chokeslam;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.UpgradeType;

import java.util.Map;

public class ChokeslamMoveInfo extends AbstractSignatureMoveInfo {
  private static final int GRAPPLES_REQUIRED = 3;
  private int grappledCount = 0;

  public ChokeslamMoveInfo() {
    this(AbstractSignatureMoveUpgrade.NO_UPGRADES);
  }

  public ChokeslamMoveInfo(Map<AbstractSignatureMoveUpgrade, Integer> upgrades) {
    super(new Chokeslam(), upgrades);
  }

  @Override
  public void onCardPlayed() {

  }

  @Override
  public void atStartOfTurn() {

  }

  @Override
  public void atStartOfCombat() {
    this.grappledCount = 0;
  }

  @Override
  public void atEndOfCombat() {
    this.grappledCount = 0;
  }

  @Override
  public void upgradeMove(UpgradeType type) {

  }

  @Override
  public void onEnemyGrappled() {
    grappledCount++;
    if (grappledCount == GRAPPLES_REQUIRED) {
      triggerGainCard();
    }
  }

  @Override
  public String getStaticConditionText() {
    return "Apply Grappled 3 times in the same battle.";
  }

  @Override
  public String getDynamicConditionText() {
    final int grapplesRemaining = GRAPPLES_REQUIRED - grappledCount;

    if (grapplesRemaining > 0) {
      return "Apply Grappled " + grapplesRemaining + " more " +
          (grapplesRemaining == 1 ? "time" : "times") + " this combat.";
    } else {
      return "You have acquired your signature move already this combat.";
    }
  }
}
