package thewrestler.screens.trademarkmove.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.screens.trademarkmove.TrademarkMoveSelectScreen;
import thewrestler.screens.trademarkmove.TrademarkMoveSelectScreen.TrademarkMoveScreenSelection;
import thewrestler.WrestlerMod;

import java.util.Collections;
import java.util.List;

public class ChooseTrademarkMoveAction extends AbstractGameAction {
  private static final UIStrings uiStrings =
      CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("ComeCleanAction"));
  public static final String[] TEXT = uiStrings.TEXT;
  private static final float DURATION = Settings.ACTION_DUR_XLONG;

  private final AbstractPenaltyStatusCard selectedCard;
  private final List<AbstractPenaltyStatusCard> allPenaltyCards;
  private boolean cardSelectionFinished;

  public ChooseTrademarkMoveAction(AbstractPenaltyStatusCard selectedCard, List<AbstractPenaltyStatusCard> allPenaltyCards) {
    this.selectedCard = selectedCard;
    this.allPenaltyCards = Collections.unmodifiableList(allPenaltyCards);
    this.actionType = ActionType.CARD_MANIPULATION;
    this.duration = this.startDuration = DURATION;
    cardSelectionFinished = false;
  }

  public void update() {
    if (this.duration == this.startDuration) {
      // TODO: also perform cost check from UsePenaltyCardPatch (extract out of that class)
      if (AbstractDungeon.getMonsters().areMonstersBasicallyDead() || allPenaltyCards.size() < 2) {
        this.isDone = true;
      } else {
        final TrademarkMoveSelectScreen screen = WrestlerMod.getTrademarkMoveSelectScreen();
        screen.reset();
        screen.setCards(this.selectedCard, this.allPenaltyCards);
        screen.open();
      }
      tickDuration();
      return;
    }

    TrademarkMoveScreenSelection selection = WrestlerMod.getTrademarkMoveSelectScreen().getSelection();
    if (selection !=  TrademarkMoveScreenSelection.UNSELECTED) {
      if (selection == TrademarkMoveScreenSelection.PLAY) {
        this.selectedCard.triggeredFromSelectScreen = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this.selectedCard, false));
      } else if (selection == TrademarkMoveScreenSelection.COMBINE) {
        final TrademarkMoveSelectScreen screen = WrestlerMod.getTrademarkMoveSelectScreen();
        AbstractDungeon.actionManager.addToTop(
            new LoseEnergyAction(selectedCard.costForTurn + screen.getSecondSelectedCard().costForTurn));
        AbstractDungeon.player.hand.moveToExhaustPile(this.selectedCard);
        AbstractDungeon.player.hand.moveToExhaustPile(screen.getSecondSelectedCard());
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(screen.getTrademarkMove()));
      } else if (selection == TrademarkMoveScreenSelection.CANCEL) {
        this.selectedCard.triggeredFromSelectScreen = false;
      }
      final TrademarkMoveSelectScreen screen = WrestlerMod.getTrademarkMoveSelectScreen();
      if (screen.isOpen) {
        screen.close();
      }
      this.isDone = true;
    }
    tickDuration();
  }
}
