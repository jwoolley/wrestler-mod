package thewrestler.util.info.penaltycard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractPenaltyCardStrategy {
  public abstract AbstractPenaltyStatusCard getNextCard();
  public abstract AbstractPenaltyStatusCard previewNextCard();
  public abstract List<AbstractPenaltyStatusCard> previewNextCards();
  public abstract void resetForCombat();
  protected abstract List<AbstractPenaltyStatusCard> getQueuedCards();

  public void addPenaltyCardToQueue(AbstractPenaltyStatusCard card, boolean toFront) {}
  public void forEachQueuedCard(Consumer<AbstractPenaltyStatusCard> fn, Predicate<AbstractPenaltyStatusCard> predicate) {
    for (AbstractPenaltyStatusCard c : getQueuedCards()) {
      if (predicate.test(c)) {
        fn.accept(c);
      }
    }
  }
}