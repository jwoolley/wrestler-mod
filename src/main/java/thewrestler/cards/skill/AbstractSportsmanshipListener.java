package thewrestler.cards.skill;

public interface AbstractSportsmanshipListener {
  void onUnsportingChanged(int changeAmount, int newValue, boolean isEndOfTurnChange);
  void onBecomeSporting();
  void onBecomeUnsporting();
}
