package thewrestler.util.info.penaltycard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PenaltyCardGroup extends ArrayList<AbstractPenaltyCard> {

  // TODO: this almost certainly doesn't belong here; probably put on SportsmanshipInfo (which exposes a getStrategy())
  private AbstractPenaltyCardStrategy strategy = new AbstractPenaltyCardStrategy(SportsmanshipInfo.MAX_PENALTY_CARDS) {
    @Override
    public AbstractPenaltyCard getNextCard() {
      return (AbstractDungeon.miscRng.randomBoolean()
          ? AbstractDungeon.miscRng.randomBoolean() ?  new RedPenaltyCard() : new YellowPenatlyCard()
          : AbstractDungeon.miscRng.randomBoolean() ? new BluePenaltyCard() : new OrangePenaltyCard());
    }

    @Override
    public AbstractPenaltyCard previewNextCard() {
      // this is extraordinarily sloppy and is only a placeholder until there's a real strategy (where is can be
      //  backed by a list)
      return new RedPenaltyCard();
    }

    @Override
    public List<AbstractPenaltyCard> previewNextCards() {
      return Stream.generate(RedPenaltyCard::new).limit(this.maxCards - SportsmanshipInfo.getAmount()).collect(Collectors.toList());
    }

    @Override
    public void resetForCombat() {

    }
  };

  public void remove() {
    if (!this.isEmpty()) {
      AbstractPenaltyCard card = this.remove(this.size() - 1);
      card.onRemoved();
    }
  }

  private AbstractPenaltyCard getNextCardToBeGained() {
    return this.strategy.getNextCard();
  }

  public void gainCard() {
    if (this.size() < SportsmanshipInfo.MAX_PENALTY_CARDS) {
      AbstractPenaltyCard card = getNextCardToBeGained();
      this.add(card);
      card.onGained();
    }
  }

  public AbstractPenaltyCardStrategy getStrategy() {
    return this.strategy;
  }

  public void atStartOfTurn() {
    AbstractDungeon.actionManager.addToBottom(new TriggerPenaltyCardsStartOfTurnAction(this));
  }

  public void atEndOfTurn() {
    AbstractDungeon.actionManager.addToBottom(new TriggerPenaltyCardsEndOfTurnAction(this));
  }

  public void onCardUsed(AbstractCard card) {
    this.forEach(c -> c.onCardUsed(card));
  }

  public void onCardExhausted(AbstractCard card) {
    this.forEach(c -> c.onCardExhausted(card));
  }

  public static abstract class AbstractPenaltyCardStrategy {
    protected final int maxCards;

    public AbstractPenaltyCardStrategy(int maxCards) {
      this.maxCards = maxCards;
    }

    public abstract AbstractPenaltyCard getNextCard();
    public abstract AbstractPenaltyCard previewNextCard();
    public abstract List<AbstractPenaltyCard> previewNextCards();
    public abstract void resetForCombat();
  }

  static class TriggerPenaltyCardsStartOfTurnAction extends AbstractGameAction {
    private final static float ACTION_DURATION =  Settings.ACTION_DUR_MED;
    private final List<AbstractPenaltyCard> penaltyCardList;
    private boolean triggeredCard;
    public TriggerPenaltyCardsStartOfTurnAction(List<AbstractPenaltyCard> penaltyCardGroup) {
      this.duration = ACTION_DURATION;
      this.actionType = AbstractGameAction.ActionType.SPECIAL;
      this.penaltyCardList = penaltyCardGroup;
      this.triggeredCard = false;
    }
    @Override
    public void update() {
      if (this.duration < ACTION_DURATION) {
        if (!this.triggeredCard && this.penaltyCardList.size() > 0) {
          penaltyCardList.get(0).atStartOfTurn();
          this.triggeredCard = true;
        }
        if (this.penaltyCardList.size() <= 1) {
          this.isDone = true;
        } else if (this.duration < 0.1f) {
          AbstractDungeon.actionManager.addToBottom(
              new TriggerPenaltyCardsStartOfTurnAction(penaltyCardList.subList(1, penaltyCardList.size())));
          this.isDone = true;
        }
      }
      tickDuration();
    }
  }


  static class TriggerPenaltyCardsEndOfTurnAction extends AbstractGameAction {
    private final static float ACTION_DURATION =  Settings.ACTION_DUR_MED;
    private final List<AbstractPenaltyCard> penaltyCardList;
    private boolean triggeredCard;
    public TriggerPenaltyCardsEndOfTurnAction(List<AbstractPenaltyCard> penaltyCardGroup) {
      this.duration = ACTION_DURATION;
      this.actionType = AbstractGameAction.ActionType.SPECIAL;
      this.penaltyCardList = penaltyCardGroup;
      this.triggeredCard = false;
    }
    @Override
    public void update() {
      if (this.duration < ACTION_DURATION) {
        if (!this.triggeredCard && this.penaltyCardList.size() > 0) {
          penaltyCardList.get(0).atEndOfTurn();
          this.triggeredCard = true;
        }
        if (this.penaltyCardList.size() <= 1) {
          this.isDone = true;
        } else if (this.duration < 0.1f) {
          AbstractDungeon.actionManager.addToBottom(
              new TriggerPenaltyCardsEndOfTurnAction(penaltyCardList.subList(1, penaltyCardList.size())));
          this.isDone = true;
        }
      }
      tickDuration();
    }
  }
}