package thewrestler.util.info.penaltycard;

import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;

import java.util.List;

public abstract class AbstractPenaltyCardStrategy {
  public abstract AbstractPenaltyStatusCard getNextCard();
  public abstract AbstractPenaltyStatusCard previewNextCard();
  public abstract List<AbstractPenaltyStatusCard> previewNextCards();
  public abstract void resetForCombat();
}