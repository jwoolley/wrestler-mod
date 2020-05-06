package thewrestler.screens.trademarkmove;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import org.apache.logging.log4j.Logger;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.screens.trademarkmove.patches.TintCardPatch;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.cards.SignatureMoveCardEnum;
import thewrestler.signaturemoves.cards.old.Chokeslam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO: BUGFIX: cancel button disappears if you switch to another screen (e.g. settings) while this screen is up
//               factors: 1) cancel button is hidden when another screen is opened; no hook is used to restore it
//                        2) game doesn't know what screen this is in order to restore it when overscreen is closed

// TODO: BUGFIX: screen elements currently don't disappear when Settings panel is opened

public class TrademarkMoveSelectScreen {

  private static AbstractPenaltyStatusCard firstCard;
  private static AbstractPenaltyStatusCard secondCard;
  private static AbstractSignatureMoveCard trademarkMoveCard;
  private static final List<AbstractPenaltyStatusCard> allCards = new ArrayList<>();

  private static final Color PRIMARY_GLOW_COLOR = Color.YELLOW;
  private static final Color SECONDARY_GLOW_COLOR = Color.CYAN;

  TrademarkMoveConfirmButton confirmButton1;
  TrademarkMoveConfirmButton confirmButton2;

  public boolean isOpen = false;

  // TODO: read from localization
  private static final String header = "Play Card or Combine";
  private float showTimer;

  private CardGroup savedHand;

  public TrademarkMoveSelectScreen() {
    this.showTimer = 0.25f;
  }

  private static void resetCardAlpha() {
    firstCard.transparency = 1.0f;
    secondCard.transparency = 1.0f;
    trademarkMoveCard.transparency = 1.0f;
  }

  private static void resetCardGlow() {
    firstCard.glowColor = SECONDARY_GLOW_COLOR;
    secondCard.glowColor = SECONDARY_GLOW_COLOR;
    trademarkMoveCard.glowColor = PRIMARY_GLOW_COLOR;
    firstCard.stopGlowing();
    secondCard.stopGlowing();
    trademarkMoveCard.stopGlowing();
  }

  // TODO: localize buttons

  private void initializeButtons() {
    Logger logger  = WrestlerMod.logger;
    confirmButton1 = new TrademarkMoveConfirmButton("Play Card",
        (thisIsNull) -> {
          TintCardPatch.tintCard(secondCard);
          TintCardPatch.tintCard(trademarkMoveCard);
          firstCard.superFlash(SECONDARY_GLOW_COLOR);
          firstCard.beginGlowing();
        },
        (thisIsNull) -> {
          TintCardPatch.untintCard(secondCard);
          TintCardPatch.untintCard(trademarkMoveCard);
          resetCardGlow();
        },
        (thisIsNull) -> {
          this.selection = TrademarkMoveScreenSelection.PLAY;
          logger.info("Play Button Clicked.");
          fakeClose();
        });

    confirmButton2 = new TrademarkMoveConfirmButton("Combine & Exhaust",
        (thisIsNull) -> {
          resetCardAlpha();
          firstCard.beginGlowing();
          secondCard.beginGlowing();
          trademarkMoveCard.beginGlowing();
          firstCard.superFlash(SECONDARY_GLOW_COLOR);
          secondCard.superFlash(SECONDARY_GLOW_COLOR);
          trademarkMoveCard.superFlash(PRIMARY_GLOW_COLOR);
        },
        (thisIsNull) -> { /* resetCardAlpha();  */ resetCardGlow(); },
        (thisIsNull) -> {
          this.selection = TrademarkMoveScreenSelection.COMBINE;

          ExhaustCardEffect effect1 = new ExhaustCardEffect(firstCard);
          effect1.duration = 0.6f;
          ExhaustCardEffect effect2 = new ExhaustCardEffect(secondCard);
          effect2.duration = 0.6f;

          AbstractDungeon.topLevelEffectsQueue.add(effect1);
          AbstractDungeon.topLevelEffectsQueue.add(effect2);
          fakeClose();
        });
    positionButtons();
    this.confirmButton1.show();
    this.confirmButton2.show();
  }

  public void reset() {
    this.selection = TrademarkMoveScreenSelection.UNSELECTED;
    this.confirmButton1 = null;
    this.confirmButton2 = null;
    isOpen = false;
    firstCard = null;
    secondCard = null;
    trademarkMoveCard = null;
    allCards.clear();
  }

  public enum TrademarkMoveScreenSelection {
    UNSELECTED,
    PLAY,
    COMBINE,
    CANCEL
  }

  private TrademarkMoveScreenSelection selection = TrademarkMoveScreenSelection.UNSELECTED;

  public TrademarkMoveScreenSelection getSelection() {
    return selection;
  }

  public AbstractPenaltyStatusCard getSecondSelectedCard() {
    return allCards.stream()
        .filter(c -> c.uuid == this.secondCard.uuid)
        .collect(Collectors.toList())
        .get(0);
  }

  public AbstractSignatureMoveCard getTrademarkMove() {
    return trademarkMoveCard;
  }

  private AbstractSignatureMoveCard getTrademarkMoveCard(AbstractPenaltyStatusCard penaltyCard1,
                                                         AbstractPenaltyStatusCard penaltyCard2) {
    final AbstractSignatureMoveCard DEFAULT_SIGNATURE_MOVE = new Chokeslam();

     return Arrays.asList(SignatureMoveCardEnum.values()).stream()
         .filter(move -> move.getCardCopy().matchCombo(penaltyCard1, penaltyCard2))
         .map(SignatureMoveCardEnum::getCardCopy)
         .findFirst()
         .orElse(DEFAULT_SIGNATURE_MOVE);
  }

  private final float CARD_DRAW_SCALE = 0.8f;
  private void configureCardLayout() {
    // TODO; show preview card (if present) on hover over card (particularly for the trademark moves)

    firstCard.drawScale = secondCard.drawScale = trademarkMoveCard.drawScale = CARD_DRAW_SCALE;
    positionCards(firstCard, secondCard, trademarkMoveCard);
    resetCardGlow();
    generateCardHitboxes(Arrays.asList(firstCard, secondCard, trademarkMoveCard));
  }

  public void setCards(AbstractPenaltyStatusCard playedCard, List<AbstractPenaltyStatusCard> allCardsInHand) {

    // TODO: use makeStatEquivalentCopy copies of the cards; BUT map to references to the originals so the action
    //  knows what cards to act on (reuse UUID?)

    allCards.clear();
    allCards.addAll(allCardsInHand);
    allCards.remove(playedCard);

    firstCard = (AbstractPenaltyStatusCard) playedCard.makeSameInstanceOf();
    secondCard = (AbstractPenaltyStatusCard) allCards.get(0).makeSameInstanceOf();
    trademarkMoveCard = getTrademarkMoveCard(firstCard, secondCard);

    configureCardLayout();
  }

  public void open() {
    AbstractDungeon.topPanel.unhoverHitboxes();

    initializeButtons();

    disableCombatControls();

    AbstractDungeon.isScreenUp = true;
    AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
    AbstractDungeon.overlayMenu.showBlackScreen(0.75f);
    AbstractDungeon.dynamicBanner.appear(header);

    showCancelButton();
    this.showTimer = 0.25F;
    this.isOpen = true;
  }

  private void disableCombatControls() {
    AbstractDungeon.overlayMenu.proceedButton.hide();
    AbstractDungeon.overlayMenu.endTurnButton.disable();
  }

  public void reopen() {
    AbstractDungeon.screen = Enum.TRADEMARK_MOVE_SELECT;

    AbstractDungeon.topPanel.unhoverHitboxes();
    AbstractDungeon.isScreenUp = true;
    AbstractDungeon.dynamicBanner.appear(this.header);
    disableCombatControls();
    showCancelButton();
  }

  private void showCancelButton() {
    AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
  }

  private void updateSelection(AbstractPenaltyStatusCard newlySelectedSecondCard) {
    secondCard = newlySelectedSecondCard;
    trademarkMoveCard = getTrademarkMoveCard(firstCard, secondCard);
    configureCardLayout();
  }

  private void generateCardHitboxes(List<AbstractCard> cards) {
    for (AbstractCard card : cards) {
      card.hb = new Hitbox(card.target_x - card.hb.width / 2, (card.target_y - card.hb.height / 2 ),
          card.hb.width, card.hb.height);
    }
  }

  public void close() {
    AbstractDungeon.overlayMenu.hideBlackScreen();
    this.isOpen = false;

    this.isOpen = false;
    AbstractDungeon.player.hand = this.savedHand;

    AbstractDungeon.overlayMenu.endTurnButton.enable();
    AbstractDungeon.overlayMenu.cancelButton.hide();

    AbstractDungeon.dynamicBanner.hide();
    AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
    AbstractDungeon.isScreenUp = false;
  }

  public boolean isOpen() {
    return isOpen;
  }

  public void render(SpriteBatch sb) {
    // TODO: call render on all sub-elements
    if (this.isOpen && AbstractDungeon.isScreenUp) {
      firstCard.render(sb);
      firstCard.hb.render(sb);
      secondCard.render(sb);
      secondCard.hb.render(sb);
      trademarkMoveCard.render(sb);
      trademarkMoveCard.hb.render(sb);

      confirmButton1.render(sb);
      confirmButton2.render(sb);
    }
  }

  private class PanelCards extends ArrayList<AbstractCard> {
    public PanelCards(AbstractPenaltyStatusCard firstPenaltyCard, AbstractPenaltyStatusCard secondPenaltyCard,
                      AbstractSignatureMoveCard moveCard) {
      this.add(firstPenaltyCard);
      this.add(secondPenaltyCard);
      this.add(moveCard);
    }
  }

  // TODO: create & update hitboxes, render tips as necessary (see SimpletonMod::SeasonScreen)

  private void positionCards(AbstractCard card1, AbstractCard card2, AbstractCard card3) {
   final float CARD_OFFSET_X = Settings.WIDTH * 0.5f;
   final float CARD_OFFSET_Y = Settings.HEIGHT * 0.68f;
   final float CARD_SPACING = AbstractCard.IMG_WIDTH * 1.25f;

    card1.target_x = CARD_OFFSET_X - CARD_SPACING;
    card1.target_y = CARD_OFFSET_Y - AbstractCard.IMG_HEIGHT * 0.5f;

    card2.target_x = CARD_OFFSET_X;
    card2.target_y = CARD_OFFSET_Y - AbstractCard.IMG_HEIGHT * 0.5f;

    card3.target_x = CARD_OFFSET_X + CARD_SPACING;
    card3.target_y = CARD_OFFSET_Y - AbstractCard.IMG_HEIGHT * 0.5f;

    card1.current_x = card1.target_x;
    card2.current_x = card2.target_x;
    card3.current_x = card3.target_x;

    card1.current_y = card1.target_y;
    card2.current_y = card2.target_y;
    card3.current_y = card3.target_y;
  }

  private void positionButtons() {
    final float SHOW_X_OFFSET = 180.0f * Settings.scale;
    confirmButton1.hb.move( confirmButton1.hb.cX - SHOW_X_OFFSET, confirmButton1.hb.cY);
    confirmButton2.hb.move( confirmButton2.hb.cX + SHOW_X_OFFSET, confirmButton2.hb.cY);
  }

  public void update() {
    // TODO: call update on all sub-elements
    if (this.isOpen) {
      if (this.showTimer > 0.0F) {
        this.showTimer -= Gdx.graphics.getDeltaTime();
        if (Settings.FAST_MODE) {
          this.showTimer -= Gdx.graphics.getDeltaTime();
        }
        updateCards();
        return;
      }

      this.savedHand = AbstractDungeon.player.hand;

      if (AbstractDungeon.overlayMenu.cancelButton.hb.clickStarted) {
        this.selection = TrademarkMoveScreenSelection.CANCEL;
        AbstractDungeon.overlayMenu.cancelButton.hb.clickStarted = false;
        fakeClose();
        return;
      }

      updateCards();
      confirmButton1.update();
      confirmButton2.update();

      // TODO: this is experimental
      AbstractDungeon.overlayMenu.cancelButton.update();
    }
  }

  private void updateCards() {
    firstCard.hb.update();
    secondCard.hb.update();
    trademarkMoveCard.hb.update();
  }

  private void fakeClose() {
    AbstractDungeon.overlayMenu.endTurnButton.disable();
    AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
    AbstractDungeon.isScreenUp = false;
    this.confirmButton1.hide();
    this.confirmButton2.hide();
  }

  public static class Enum {
    @SpireEnum
    public static AbstractDungeon.CurrentScreen TRADEMARK_MOVE_SELECT;
  }
}