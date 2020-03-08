package thewrestler.cards.skill;

public interface AbstractPenaltyCardListener {
//  void onUnsportingChanged(int changeAmount, int newValue, boolean isEndOfTurnChange);
  void onGainedWarningCard();
  void onGainedPenaltyCard();
}
