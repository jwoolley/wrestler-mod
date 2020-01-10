package thewrestler.signaturemoves.moveinfos;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thewrestler.signaturemoves.cards.DragonGate;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeType;

public class DragonGateMoveInfo extends AbstractSignatureMoveInfo {
  private static final int NUM_CARDS_REQUIRED = 5;
  private int numCardsExhausted = 0;

  public DragonGateMoveInfo() {
    this(SignatureMoveUpgradeList.NO_UPGRADES, true);
  }

  public DragonGateMoveInfo(SignatureMoveUpgradeList upgradeList, boolean isFirstInstance) {
    super(new DragonGate(), upgradeList, isFirstInstance);
  }

  @Override
  public void onCardPlayed(AbstractCard card) { }

  @Override
  public void onCardExhausted(AbstractCard card) {
    this.numCardsExhausted++;

    if (this.numCardsExhausted == NUM_CARDS_REQUIRED) {
      triggerGainCard();
    }
  }

  @Override
  public void atStartOfTurn() {
  }

  @Override
  public void atEndOfTurn() {

  }

  @Override
  public void atStartOfCombat() {
    this.numCardsExhausted = 0;
  }

  @Override
  public void atEndOfCombat() {
    this.numCardsExhausted = 0;
  }

  @Override
  public void upgradeMove(UpgradeType type) {

  }

  @Override
  public void onEnemyGrappled() {  }

  // TODO: move this text to UiStrings.json
  @Override
  public String getStaticConditionText() {
    return "Exhaust " + NUM_CARDS_REQUIRED + " "
      + (NUM_CARDS_REQUIRED == 1 ? "card" : "cards")
        + " in one battle to";
  }

  @Override
  public boolean canStillTriggerCardGain() {
    return this.numCardsExhausted < NUM_CARDS_REQUIRED;
  }

  private int getCardsRemaining() {
    return NUM_CARDS_REQUIRED - this.numCardsExhausted;
  }

  // TODO: move this text to UiStrings.json (or similar new file)
  @Override
  public String getDynamicConditionText() {
    return "Exhaust " + getCardsRemaining()
        + (NUM_CARDS_REQUIRED > 1 ? " more " + (getCardsRemaining() > 1 ? "cards" : "card") : "card")
        + " this combat to";
  }
}