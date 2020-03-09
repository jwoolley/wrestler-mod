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
import thewrestler.cards.skill.AbstractPenaltyCardListener;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.powers.NoPenaltyPower;
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
  }

  public void gainPenaltyCard() {
    // TODO: gainPenaltyCardAction
    if (!AbstractDungeon.player.hasPower(NoPenaltyPower.POWER_ID)) {
      AbstractDungeon.actionManager.addToBottom(new GainPenaltyCardsAction(1));
      List<AbstractPenaltyCardListener> listeners = new ArrayList<>();
      listeners.addAll(getPenaltyCardListenerCards());
      listeners.addAll(getPenaltyCardListenerPowers());
      listeners.forEach(AbstractPenaltyCardListener::onGainedPenaltyCard);
    } else {
      AbstractDungeon.player.getPower(NoPenaltyPower.POWER_ID).flashWithoutSound();
    }
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

  static class GainPenaltyCardsAction extends AbstractGameAction {
    private final static float ACTION_DURATION =  Settings.ACTION_DUR_XFAST;
    private boolean gainedCard;
    public GainPenaltyCardsAction(int amount) {
      this.duration = ACTION_DURATION;
      this.amount = amount;
      this.actionType = AbstractGameAction.ActionType.SPECIAL;
      this.gainedCard = false;
    }
    @Override
    public void update() {
      if (this.duration < ACTION_DURATION) {
        if (!this.gainedCard) {
          CardCrawlGame.sound.play(this.amount == 1 ? "WHISTLE_BLOW_1" : "WHISTLE_BLOW_SHORT_1");
          AbstractDungeon.actionManager.addToBottom(
              new MakeTempCardInHandAction(getPenaltyCardStrategy().getNextCard()));
          this.gainedCard = true;
        }
        if (this.amount <= 1) {
          this.isDone = true;
        } else if (this.duration < 0.1f) {
          AbstractDungeon.actionManager.addToBottom(new GainPenaltyCardsAction(--this.amount));
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
      this.hasWarningCard = false;
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