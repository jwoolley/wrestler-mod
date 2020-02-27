package thewrestler.util.info.penaltycard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import thewrestler.WrestlerMod;
import thewrestler.glyphs.custom.CustomGlyph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrangePenaltyCard extends AbstractPenaltyCard {

  private static final UIStrings uiStrings;

  public static final String ID = "WrestlerMod:OrangePenaltyCard";
  public static final String IMG_FILENAME = "orange.png";
  public static final String GLYPH_IMG_FILENAME = "orange.png";
  public static CustomGlyph.CustomGlyphEnum GLYPH_ENUM = CustomGlyph.CustomGlyphEnum.PENALTY_CARD_ORANGE;
  public static final String NAME;
  public static final String[] TEXT;

  private static final int NUM_AFFECTED_CARDS = 1;
  private static final int COST_INCREASE = 1;

  public OrangePenaltyCard() {
    super(ID, NAME, getDescription(), IMG_FILENAME);
  }

  @Override
  public AbstractPenaltyCard makeCopy() {
    return new OrangePenaltyCard();
  }

  public static CustomGlyph.CustomGlyphEnum getGlyphEnum() {
    return GLYPH_ENUM;
  }

  @Override
  public CustomGlyph getTooltipGlyph() {
    return CustomGlyph.getGlyph(getGlyphEnum());
  }

  @Override
  public void onGained() {

  }

  @Override
  public void onRemoved() {

  }

  @Override
  public void atStartOfTurn() {
    this.flash();
    playTriggerSfx();
    WrestlerMod.logger.info("Triggered penalty card [" + getDebugDescription() + "]");
    AbstractDungeon.actionManager.addToBottom(new OrangePenaltyCardAction(NUM_AFFECTED_CARDS));
  }

  private static class OrangePenaltyCardAction extends AbstractGameAction {
    private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

    public OrangePenaltyCardAction(int amount) {
      this.actionType = ActionType.SPECIAL;
      this.source = AbstractDungeon.player;
      this.amount = amount;
      this.duration = ACTION_DURATION;
      this.startDuration = ACTION_DURATION;
    }

    @Override
    public void update() {
      if (this.duration <= ACTION_DURATION) {
        AbstractPlayer player =  AbstractDungeon.player;

        List<AbstractCard> eligibleCards =  player.hand.group.stream()
            .filter(c -> c.cost >= 0)
            .collect(Collectors.toList());

        if (amount > 0 && eligibleCards.size() > 0) {
          List<AbstractCard> cards = new ArrayList<>();

          if (amount >= eligibleCards.size()) {
            cards.addAll(eligibleCards);
          } else {
            cards.addAll(eligibleCards.stream()
              .collect( Collectors.collectingAndThen(Collectors.toList(),
                  list -> { Collections.shuffle(list); return list.subList(0, amount); })
            ));
          }

          for (AbstractCard card : cards) {
            card.setCostForTurn(card.costForTurn + COST_INCREASE);
            card.flash();
          }
        }

        this.isDone = true;
      }
      tickDuration();
    }
  }

  @Override
  public void atEndOfTurn() {


  }

  @Override
  public void onCardUsed(AbstractCard card) {

  }

  @Override
  public void onCardExhausted(AbstractCard card) {

  }

  private static String getDescription() {
    return TEXT[0] + COST_INCREASE + TEXT[1];
  }


  static {
    uiStrings =  CardCrawlGame.languagePack.getUIString(ID);
    NAME = uiStrings.TEXT[0];
    TEXT = Arrays.copyOfRange(uiStrings.TEXT, 1, uiStrings.TEXT.length);
  }
}
