package thewrestler.signaturemoves.moveinfos;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thewrestler.signaturemoves.cards.Chokeslam;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeType;

public class ChokeslamMoveInfo extends AbstractSignatureMoveInfo {
  private static final int GRAPPLES_REQUIRED = 2;
  private int grappledCount = 0;

  public ChokeslamMoveInfo() {
    this(SignatureMoveUpgradeList.NO_UPGRADES, true);
  }

  public ChokeslamMoveInfo(SignatureMoveUpgradeList upgradeList, boolean isFirstInstance) {
    super(new Chokeslam(), upgradeList, isFirstInstance);
  }

  @Override
  public void onCardPlayed(AbstractCard card) {

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

  // TODO: move this text to UiStrings.json
  @Override
  public String getStaticConditionText() {
    return "Apply Grappled " + GRAPPLES_REQUIRED + " times in the same battle to";
  }

  @Override
  public boolean canStillTriggerCardGain() {
    return getGrapplesRemaining() > 0;
  }

  private int getGrapplesRemaining() {
    return GRAPPLES_REQUIRED - this.grappledCount;
  }

  // TODO: move this text to UiStrings.json (or similar new file)
  @Override
  public String getDynamicConditionText() {
    return "Apply Grappled " + getGrapplesRemaining()  + " more " +
        (getGrapplesRemaining()  == 1 ? "time" : "times") + " this combat to";
  }
}