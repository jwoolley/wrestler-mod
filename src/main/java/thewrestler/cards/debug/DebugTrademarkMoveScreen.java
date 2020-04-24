package thewrestler.cards.debug;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.BluePenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.RedPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.YellowPenaltyStatusCard;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.screens.trademarkmove.TrademarkMoveSelectScreen;
import thewrestler.screens.trademarkmove.TrademarkMoveSelectScreen.TrademarkMoveScreenSelection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class DebugTrademarkMoveScreen extends CustomCard {
  public static final String ID = "WrestlerMod:DebugTrademarkMoveScreen";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "debug/opentrademarkmovescreen.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  public DebugTrademarkMoveScreen() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST,
        getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    final AbstractPenaltyStatusCard testSelectedCard = new BluePenaltyStatusCard();
    final List<AbstractPenaltyStatusCard> testAllPenaltyCards = Arrays.asList(new RedPenaltyStatusCard(), new YellowPenaltyStatusCard());
    AbstractDungeon.actionManager.addToBottom(new ChooseTrademarkMoveAction(testSelectedCard, testAllPenaltyCards));
  }

  // TODO: don't show Cancel button if triggered through CardQueueItem (e.g. via Havoc); make this configurable
  private static class ChooseTrademarkMoveAction extends AbstractGameAction {
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
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead() || allPenaltyCards.size() < 2) {
          this.isDone = true;
        } else {
          TrademarkMoveSelectScreen screen = WrestlerMod.getTrademarkMoveSelectScreen();
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
          AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this.selectedCard, false));
        } else if (selection == TrademarkMoveScreenSelection.COMBINE) {
          TrademarkMoveSelectScreen screen = WrestlerMod.getTrademarkMoveSelectScreen();
          AbstractDungeon.player.hand.moveToExhaustPile(this.selectedCard);
          AbstractDungeon.player.hand.moveToExhaustPile(screen.getSecondSelectedCard());
          AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(screen.getTrademarkMove()));
        } else if (selection == TrademarkMoveScreenSelection.CANCEL) {
          this.selectedCard.triggeredFromSelectScreen = false;      }
        this.isDone = true;
      }
      tickDuration();
    }
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      initializeDescription();
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new DebugTrademarkMoveScreen();
  }

  public static String getDescription() {
    return DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}