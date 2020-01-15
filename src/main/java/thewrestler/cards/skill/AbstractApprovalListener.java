package thewrestler.cards.skill;

public interface AbstractApprovalListener {
  void onApprovalChanged(int changeAmount, int newValue);
  void onBecomeLiked();
  void onBecomeDisliked();
}
