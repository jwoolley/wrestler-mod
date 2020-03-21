package thewrestler.util.info.penaltycard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.WrestlerMod;
import thewrestler.cards.EndOfCombatListener;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.skill.AbstractPenaltyCardListener;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.powers.NoPenaltyPower;
import thewrestler.powers.enqueuedpenaltycard.EnqueuedPenaltyCardPower;
import thewrestler.util.BasicUtils;
import thewrestler.util.info.CombatInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PenaltyCardInfo implements StartOfCombatListener, EndOfCombatListener {
  private boolean hasWarningCard;

  private AbstractPenaltyCardStrategy penaltyCardStrategy;

  public PenaltyCardInfo() {
    hasWarningCard = false;
    penaltyCardStrategy = new DefaultPenaltyCardStrategy();
  }

  public void reset() {
    hasWarningCard = false;
    penaltyCardStrategy.resetForCombat();
  }

  public static void gainPenaltyCard() {
    gainPenaltyCard(null);
  }

  public static void gainPenaltyCards(List<AbstractPenaltyStatusCard> penaltyCards) {
    penaltyCards.forEach(PenaltyCardInfo::gainPenaltyCard);
  }

  public static void gainPenaltyCards(int numCards) {
    for (int i = 0; i < numCards; i++) {
      gainPenaltyCard();
    }
  }

  public static void gainPenaltyCard(AbstractPenaltyStatusCard penaltyCard) {
    if (!AbstractDungeon.player.hasPower(NoPenaltyPower.POWER_ID)) {
      AbstractDungeon.actionManager.addToTop(new GainPenaltyCardsAction(1, penaltyCard));
    } else {
      AbstractDungeon.player.getPower(NoPenaltyPower.POWER_ID).flashWithoutSound();
    }
  }


  private static void onPenaltyCardGained(AbstractPenaltyStatusCard penaltyCard) {
    WrestlerCharacter.getPenaltyCardInfo().reset();
    List<AbstractPenaltyCardListener> listeners = new ArrayList<>();
    penaltyCard.triggerOnCardGained();
    listeners.addAll(getPenaltyCardListenerCards());
    listeners.addAll(getPenaltyCardListenerPowers());
    listeners.forEach(liatener -> liatener.onGainedPenaltyCard(penaltyCard));
    CombatInfo.incrementPenaltyCardsGainedThisCombatCount();
  }

  private final static AbstractPenaltyCardStrategy DEFAULT_STRATEGY = new DefaultPenaltyCardStrategy();

  private AbstractPenaltyCardStrategy getStrategy() {
    return this.penaltyCardStrategy;
  }

  static AbstractPenaltyCardStrategy getPenaltyCardStrategy() {
    return hasPenaltyCardInfo() ? getInfo().getStrategy() : DEFAULT_STRATEGY;
  }

  public void onCardExhausted(AbstractCard card) {
  }

  private static AbstractPenaltyStatusCard getNextPenaltyCard() {
    if  (EnqueuedPenaltyCardPower.playerHasAnyCardsEnqueued()) {
      return EnqueuedPenaltyCardPower.getNextCard();
    } else {
      return getPenaltyCardStrategy().getNextCard();
    }
  }

  public static class GainPenaltyCardsAction extends AbstractGameAction {
    private final static float ACTION_DURATION =  Settings.ACTION_DUR_XFAST;
    final private AbstractPenaltyStatusCard penaltyCard;
    final private boolean addActionToTop;
    private boolean gainedCard;

    public GainPenaltyCardsAction(int amount) {
      this(amount, null, false);
    }

    public GainPenaltyCardsAction(int amount, boolean addActionToTop) {
      this(amount, null, addActionToTop);
    }

    public GainPenaltyCardsAction(int amount, AbstractPenaltyStatusCard penaltyCard) {
      this(amount, penaltyCard, false);
    }

    public GainPenaltyCardsAction(int amount, AbstractPenaltyStatusCard penaltyCard, boolean addActionToTop) {
      this.duration = ACTION_DURATION;
      this.amount = amount;
      this.penaltyCard = penaltyCard;
      this.actionType = AbstractGameAction.ActionType.SPECIAL;
      this.addActionToTop = addActionToTop;
      this.gainedCard = false;
    }

    @Override
    public void update() {
      if (this.duration < ACTION_DURATION) {
        if (!this.gainedCard) {
          CardCrawlGame.sound.play(this.amount == 1 ? "WHISTLE_BLOW_1" : "WHISTLE_BLOW_SHORT_1");

          final AbstractPenaltyStatusCard cardToGain = this.penaltyCard == null ? getNextPenaltyCard() : this.penaltyCard;

          if (addActionToTop) {
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(cardToGain));
          } else {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(cardToGain));
          }
          onPenaltyCardGained(cardToGain);
          this.gainedCard = true;
        }
        if (this.amount <= 1) {
          this.isDone = true;
        } else if (this.duration < 0.1f) {
          if (addActionToTop) {
            AbstractDungeon.actionManager.addToTop(new GainPenaltyCardsAction(--this.amount));
          } else {
            AbstractDungeon.actionManager.addToBottom(new GainPenaltyCardsAction(--this.amount));
          }
          this.isDone = true;
        }
        return;
      }
      tickDuration();
    }
  }

  public static boolean hasPenaltyCardInfo() {
    return BasicUtils.isPlayingAsWrestler() && WrestlerCharacter.hasPenaltyCardInfo();
  }

  public static boolean hasWarningCard() {
    return hasPenaltyCardInfo() && getInfo().hasWarningCard;
  }

  public static void resetForNewCombat() {
    if (WrestlerCharacter.hasPenaltyCardInfo()) {
      getInfo().reset();
      EnqueuedPenaltyCardPower.resetForCombat();
    }
  }

  // TODO: does the decrement logic still belong here?
  public void onCardUsed(AbstractCard card) {
    if (card.hasTag(WrestlerCardTags.DIRTY)) {
      handleDirtyCardPlayed();
      CombatInfo.incrementDirtyCardsPlayedCount();
    }
  }

  private static PenaltyCardInfo getInfo() {
    return hasPenaltyCardInfo() ? WrestlerCharacter.getPenaltyCardInfo() : null;
  }

  private void handleDirtyCardPlayed() {
    if (this.hasWarningCard) {
      gainPenaltyCard();
    } else {
      this.hasWarningCard = true;
    }
  }

  // TODO: add hook to make this call (or use PostBattleSubscriber hook)
  public void onVictory(AbstractCard card) {
    WrestlerMod.logger.info("UnsportingInfo:: onVictory called");
    reset();
  }

  public void atStartOfTurn() {
    //  USE THIS CHECK IF warning card should "carry over" if 1 Dirty card was played
    //    if (CombatInfo.getNumDirtyCardsPlayed() == 0) {
    //      reset();
    //    }
    reset();
  }

  public void atEndOfTurn() {

  }
  public void atStartOfCombat(){
    reset();
  }

  public void atEndOfCombat() {
    reset();
  }

  public static List<AbstractCard> getPlayersDirtyCards() {
    final List<AbstractCard> dirtyCards = new ArrayList<>();

    AbstractPlayer player = AbstractDungeon.player;

    dirtyCards.addAll(player.drawPile.group);
    dirtyCards.addAll(player.hand.group);
    dirtyCards.addAll(player.discardPile.group);
    dirtyCards.addAll(player.exhaustPile.group);

    return dirtyCards;
  }

  public static List<AbstractPenaltyCardListener> getPenaltyCardListenerPowers() {
    return AbstractDungeon.player.powers.stream()
        .filter(p -> p instanceof AbstractPenaltyCardListener)
        .map(p -> (AbstractPenaltyCardListener)p).collect(Collectors.toList());
  }

    public static List<AbstractPenaltyCardListener> getPenaltyCardListenerCards() {
    final List<AbstractPenaltyCardListener> cards = new ArrayList<>();

    AbstractPlayer player = AbstractDungeon.player;
    cards.addAll(player.drawPile.group.stream()
        .filter(c -> c instanceof AbstractPenaltyCardListener).map(c -> (AbstractPenaltyCardListener)c).collect(Collectors.toList()));
    cards.addAll(player.hand.group.stream()
        .filter(c -> c instanceof AbstractPenaltyCardListener).map(c -> (AbstractPenaltyCardListener)c).collect(Collectors.toList()));
    cards.addAll(player.discardPile.group.stream()
        .filter(c -> c instanceof AbstractPenaltyCardListener).map(c -> (AbstractPenaltyCardListener)c).collect(Collectors.toList()));
    cards.addAll(player.exhaustPile.group.stream()
        .filter(c -> c instanceof AbstractPenaltyCardListener).map(c -> (AbstractPenaltyCardListener)c).collect(Collectors.toList()));
    return cards;
  }
}