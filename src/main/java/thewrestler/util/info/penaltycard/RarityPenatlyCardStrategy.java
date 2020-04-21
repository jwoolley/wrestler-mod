package thewrestler.util.info.penaltycard;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.cards.EndOfCombatListener;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.BluePenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.RedPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.YellowPenaltyStatusCard;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.List;

public class RarityPenatlyCardStrategy extends AbstractPenaltyCardStrategy
    implements StartOfCombatListener, EndOfCombatListener {

  private static AbstractPenaltyStatusCard DEFAULT_COMMON = new BluePenaltyStatusCard();
  private static final AbstractPenaltyStatusCard DEFAULT_UNCOMMON = new YellowPenaltyStatusCard();
  private static final AbstractPenaltyStatusCard DEFAULT_RARE = new RedPenaltyStatusCard();
  private AbstractPenaltyStatusCard commonCard;
  private AbstractPenaltyStatusCard uncommonCard;
  private AbstractPenaltyStatusCard rareCard;
  private float commonProbability;
  private float uncommonProbability;
  private float rareProbability;

  private final List<AbstractPenaltyStatusCard> cardQueue = new ArrayList<>();

  private static final float DEFAULT_COMMON_PROBABILITY = 0.9f;
  private static final float DEFAULT_UNCOMMON_PROBABILITY = 0.6f;
  private static final float DEFAULT_RARE_PROBABILITY = 0.4f;

  public RarityPenatlyCardStrategy() {
    this(DEFAULT_COMMON, DEFAULT_UNCOMMON, DEFAULT_RARE);
  }

  public RarityPenatlyCardStrategy(AbstractPenaltyStatusCard commonCard, AbstractPenaltyStatusCard uncommonCard,
                                   AbstractPenaltyStatusCard rareCard) {
    this.commonCard = commonCard;
    this.uncommonCard = uncommonCard;
    this.rareCard = rareCard;
    this.commonProbability = DEFAULT_COMMON_PROBABILITY;
    this.uncommonProbability = DEFAULT_UNCOMMON_PROBABILITY;
    this.rareProbability = DEFAULT_RARE_PROBABILITY;
  }

  public AbstractPenaltyStatusCard getNextCard() {
    AbstractPenaltyStatusCard card = cardQueue.isEmpty() ? rollCard() : cardQueue.remove(0);

    if (cardQueue.isEmpty()) {
      addCardToQueue();
    }

    return card;
  }

  private void addCardToQueue() {
    cardQueue.add(rollCard());
  }

  public AbstractPenaltyStatusCard getCommon() {
    return (AbstractPenaltyStatusCard)commonCard.makeCopy();
  }

  public AbstractPenaltyStatusCard getUncommon() {
    return (AbstractPenaltyStatusCard)uncommonCard.makeCopy();
  }

  public AbstractPenaltyStatusCard getRare() {
    return (AbstractPenaltyStatusCard)rareCard.makeCopy();
  }

  private AbstractPenaltyStatusCard rollCard() {
    final float sample = AbstractDungeon.miscRng.random(commonProbability + uncommonProbability + rareProbability);
    if (sample > commonProbability + uncommonProbability) {
      return getRare();
    } else if (sample > commonProbability) {
      return getUncommon();
    } else {
      return getCommon();
    }
  }

  // TODO: introduce queue and preview next card (refreshing when probabilities change/reroll happens)

  @Override
  public AbstractPenaltyStatusCard previewNextCard() {
    if (cardQueue.isEmpty()) {
      addCardToQueue();
    }
    return (AbstractPenaltyStatusCard)cardQueue.get(0).makeCopy();
  }

  @Override
  public List<AbstractPenaltyStatusCard> previewNextCards() {
    return new ArrayList<>(this.cardQueue);
  }

  @Override
  public void resetForCombat() {
    cardQueue.clear();
  }

  @Override
  protected List<AbstractPenaltyStatusCard> getQueuedCards() {
    return cardQueue;
  }

  @Override
  public void addPenaltyCardToQueue(AbstractPenaltyStatusCard card, boolean toFront) {
    cardQueue.add(toFront ? 0 : cardQueue.size(), (AbstractPenaltyStatusCard)card.makeCopy());
  }

  @Override
  public void atStartOfCombat() {
    cardQueue.clear();
    addCardToQueue();
  }

  @Override
  public void atEndOfCombat() {
    cardQueue.clear();
  }
}