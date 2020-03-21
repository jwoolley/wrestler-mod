package thewrestler.cards.skill;

import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;

public interface AbstractPenaltyCardListener {
//  void onUnsportingChanged(int changeAmount, int newValue, boolean isEndOfTurnChange);
  void onGainedWarningCard();
  void onGainedPenaltyCard(AbstractPenaltyStatusCard card);
}
