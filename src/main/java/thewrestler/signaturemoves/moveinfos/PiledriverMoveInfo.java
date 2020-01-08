package thewrestler.signaturemoves.moveinfos;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thewrestler.signaturemoves.cards.Piledriver;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeType;
import thewrestler.util.info.CombatInfo;

public class PiledriverMoveInfo extends AbstractSignatureMoveInfo {
  private static final int SKILLS_REQUIRED = 3;
  private static final int TURNS_REQUIRED = 2;
  private int numTurns = 0;

  public PiledriverMoveInfo() {
    this(SignatureMoveUpgradeList.NO_UPGRADES, true);
  }

  public PiledriverMoveInfo(SignatureMoveUpgradeList upgradeList, boolean isFirstInstance) {
    super(new Piledriver(), upgradeList, isFirstInstance);
  }

  @Override
  public void onCardPlayed(AbstractCard card) {
    if (card.type == AbstractCard.CardType.SKILL && CombatInfo.getNumSkillsPlayed() + 1 == SKILLS_REQUIRED) {
      numTurns++;
      if (numTurns == TURNS_REQUIRED) {
        triggerGainCard();
      }
    }
  }

  @Override
  public void atStartOfTurn() {
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
    return "Play " + SKILLS_REQUIRED + " in one turn"
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
    final String prefixText = "Play " + SKILLS_REQUIRED + " Skills in one turn";

    return prefixText
        + (TURNS_REQUIRED > 1 ? " for " + getTurnsRemaining() + " more turn" + (getTurnsRemaining() > 1 ? "s" : "") : "")
        + " to";
  }
}