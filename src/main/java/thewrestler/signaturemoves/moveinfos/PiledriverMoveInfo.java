package thewrestler.signaturemoves.moveinfos;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thewrestler.signaturemoves.cards.Piledriver;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeType;

public class PiledriverMoveInfo extends AbstractSignatureMoveInfo {
  private static final int BLOCK_REQUIRED = 15;
  private int blockCount = 0;

  public PiledriverMoveInfo() {
    this(SignatureMoveUpgradeList.NO_UPGRADES, true);
  }

  public PiledriverMoveInfo(SignatureMoveUpgradeList upgradeList, boolean isFirstInstance) {
    super(new Piledriver(), upgradeList, isFirstInstance);
  }

  @Override
  public void onCardPlayed(AbstractCard card) {

  }

  @Override
  public void atStartOfTurn() {
    this.blockCount = 0;
  }

  @Override
  public void atStartOfCombat() {
    this.blockCount = 0;
  }

  @Override
  public void atEndOfCombat() {
    this.blockCount = 0;
  }

  @Override
  public void upgradeMove(UpgradeType type) {

  }

  @Override
  public void onEnemyGrappled() {  }

  // TODO: move this text to UiStrings.json
  @Override
  public String getStaticConditionText() {
    return "Block " + BLOCK_REQUIRED + " damage in one turn to";
  }

  private boolean triggeredGain = false;

  @Override
  public boolean canStillTriggerCardGain() {
    return !triggeredGain;
  }

  private int getBlockRemaining() {
    return BLOCK_REQUIRED - this.blockCount;
  }

  // TODO: move this text to UiStrings.json (or similar new file)
  @Override
  public String getDynamicConditionText() {
    return "Block " + getBlockRemaining()  + " more damage this turn to";
  }
}