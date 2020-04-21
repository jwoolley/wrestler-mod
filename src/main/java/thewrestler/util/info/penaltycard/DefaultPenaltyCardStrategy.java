package thewrestler.util.info.penaltycard;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.BluePenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.RedPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.YellowPenaltyStatusCard;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;

public class DefaultPenaltyCardStrategy extends AbstractPenaltyCardStrategy {

  final boolean ONLY_YELLOW = false;
  final boolean ONLY_YELLOW_AND_BLUE = false;
  final boolean ONLY_RED = true;

  public AbstractPenaltyStatusCard getNextCard() {
    if (ONLY_RED) {
      return new RedPenaltyStatusCard();
    }
    if (ONLY_YELLOW) {
      return new YellowPenaltyStatusCard();
    } else if (ONLY_YELLOW_AND_BLUE) {
      return AbstractDungeon.miscRng.randomBoolean() ? new BluePenaltyStatusCard() : new YellowPenaltyStatusCard();
    }
    return AbstractDungeon.miscRng.randomBoolean()
        ? AbstractDungeon.miscRng.randomBoolean() ?  new RedPenaltyStatusCard() : new BluePenaltyStatusCard()
        : AbstractDungeon.miscRng.randomBoolean() ? new YellowPenaltyStatusCard() : getNextCard();
  }

  @Override
  public AbstractPenaltyStatusCard previewNextCard() {
    return null;
  }

  @Override
  public List<AbstractPenaltyStatusCard> previewNextCards() {
    return null;
  }

  @Override
  public void resetForCombat() {

  }

  @Override
  protected List<AbstractPenaltyStatusCard> getQueuedCards() {
    return Collections.EMPTY_LIST;
  }
}