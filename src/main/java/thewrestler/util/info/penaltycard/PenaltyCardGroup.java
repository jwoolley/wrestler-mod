package thewrestler.util.info.penaltycard;

import thewrestler.relics.PenaltyCard;

import java.util.ArrayList;

public class PenaltyCardGroup extends ArrayList<AbstractPenaltyCard> {

  public void remove() {
    if (!this.isEmpty()) {
      AbstractPenaltyCard card = this.remove(this.size() - 1);
      card.onRemoved();
    }
  }

  public void addPenaltyCard() {

  }

  protected AbstractPenaltyCard getNextPenatlyCard() {
    return new RedPenaltyCard();
  }
}
