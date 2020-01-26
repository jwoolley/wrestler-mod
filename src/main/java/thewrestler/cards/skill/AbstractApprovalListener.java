package thewrestler.cards.skill;

public interface AbstractApprovalListener {
  void onApprovalChanged(int changeAmount, int newValue, boolean isEndOfTurnChange);
  void onBecomeLiked();
  void onBecomeDisliked();
}
