package thewrestler.signaturemoves.moveinfos;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.signaturemoves.cards.Piledriver;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeType;
import thewrestler.util.info.CombatInfo;

public class PiledriverMoveInfo extends AbstractSignatureMoveInfo {
  private static final int TURNS_REQUIRED = 1;
  private static final int MAX_CARDS = 1;
  private int numTurns = 0;

  public PiledriverMoveInfo() {
    this(SignatureMoveUpgradeList.NO_UPGRADES, true);
  }

  public PiledriverMoveInfo(SignatureMoveUpgradeList upgradeList, boolean isFirstInstance) {
    super(new Piledriver(), upgradeList, isFirstInstance);
  }

  @Override
  public void onCardPlayed(AbstractCard card) { }

  @Override
  public void onCardExhausted(AbstractCard card) {

  }

  @Override
  public void atStartOfTurn() {
  }

  @Override
  public void atEndOfTurn() {
    if (AbstractDungeon.player.drawPile.size() <= MAX_CARDS) {
      numTurns++;
      if (numTurns == TURNS_REQUIRED) {
        triggerGainCard();
      }
    }
  }

  @Override
  public void atStartOfCombat() {
    this.numTurns = 0;
  }

  @Override
  public void atEndOfCombat() {
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
  public boolean canStillTriggerCardGain() {
    return numTurns < TURNS_REQUIRED;
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