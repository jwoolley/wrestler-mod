package thewrestler.signaturemoves.moveinfos;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.signaturemoves.cards.Piledriver;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeType;

public class PiledriverMoveInfo extends AbstractSignatureMoveInfo {
  private static final int TURNS_REQUIRED = 2;
  private static final int MAX_CARDS = 3;
  private int numTurns = 0;
  private boolean alreadyGainedThisCombat;

  public PiledriverMoveInfo() {
    this(SignatureMoveUpgradeList.NO_UPGRADES, true);
  }

  public PiledriverMoveInfo(SignatureMoveUpgradeList upgradeList, boolean isFirstInstance) {
    super(new Piledriver(), upgradeList, isFirstInstance);
  }

  @Override
  public boolean gainedCardThisCombat() {
    return alreadyGainedThisCombat;
  }

  @Override
  public void flagCardAsGained() {
    alreadyGainedThisCombat = true;
  }

  @Override
  public void resetForNewCombat() {
    alreadyGainedThisCombat = false;
  }

  @Override
  public void onCardPlayed(AbstractCard card) { }

  @Override
  public void onCardExhausted(AbstractCard card) {

  }

  @Override
  public void _atStartOfTurn() {
  }

  @Override
  public void _atEndOfTurn() {
    if (AbstractDungeon.player.drawPile.size() <= MAX_CARDS) {
      numTurns++;
      if (numTurns == TURNS_REQUIRED) {
        triggerGainCard();
      }
    }
  }

  @Override
  public void _atStartOfCombat() {
    this.numTurns = 0;
  }

  @Override
  public void _atEndOfCombat() {
    this.numTurns = 0;
  }

  @Override
  public void upgradeMove(UpgradeType type) {

  }

  @Override
  public void onEnemyGrappled() {  }

  // TODO: move this text to UiStrings.json
  @Override
  public String getStaticConditionText() {
    return "End your turn with "
        + (MAX_CARDS == 0 ? "an empty" : MAX_CARDS + " or less cards in your" ) + " draw pile"
        + (TURNS_REQUIRED > 1 ? " on " + TURNS_REQUIRED + " separate turns" : "") + " to";
  }

  @Override
  public boolean _canStillTriggerCardGain() {
    return numTurns <= TURNS_REQUIRED;
  }

  private int getTurnsRemaining() {
    return TURNS_REQUIRED - this.numTurns;
  }

  // TODO: move this text to UiStrings.json (or similar new file)
  @Override
  public String getDynamicConditionText() {
    final String prefixText = "End your turn with "
        + (MAX_CARDS == 0 ? "an empty" : MAX_CARDS + " or less cards in your" ) + " draw pile";

    return prefixText
        + (TURNS_REQUIRED > 1 ? " for " + getTurnsRemaining() + " more turn" + (getTurnsRemaining() > 1 ? "s" : "") : "")
        + " to";
  }
}