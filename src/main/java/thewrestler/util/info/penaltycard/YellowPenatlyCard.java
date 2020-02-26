package thewrestler.util.info.penaltycard;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import thewrestler.WrestlerMod;
import thewrestler.glyphs.custom.CustomGlyph;

import java.util.Arrays;

public class YellowPenatlyCard extends AbstractPenaltyCard {

  // TODO: additional tooltip for Dazed keyword

  private static final UIStrings uiStrings;

  public static final String ID = "WrestlerMod:YellowPenaltyCard";
  public static final String IMG_FILENAME = "yellow.png";
  public static final String GLYPH_IMG_FILENAME = "yellow.png";
  public static CustomGlyph.CustomGlyphEnum GLYPH_ENUM = CustomGlyph.CustomGlyphEnum.PENALTY_CARD_YELLOW;
  public static final String NAME;
  public static final String[] TEXT;

  private static final int NUM_CARDS = 1;

  public YellowPenatlyCard() {
    super(ID, NAME, getDescription(), IMG_FILENAME);
  }

  @Override
  public AbstractPenaltyCard makeCopy() {
    return new YellowPenatlyCard();
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
    WrestlerMod.logger.info("Gained penalty card [" + getDebugDescription() + "]");
  }

  @Override
  public void onRemoved() {
    WrestlerMod.logger.info("Removed penalty card [" + getDebugDescription() + "]");
  }

  @Override
  public void atStartOfTurn() {

  }

  @Override
  public void atEndOfTurn() {
    this.flash();
    WrestlerMod.logger.info("Triggered penalty card [" + getDebugDescription() + "]");
    AbstractPlayer player = AbstractDungeon.player;


    AbstractDungeon.actionManager.addToBottom(
        new MakeTempCardInDiscardAction(new Dazed(), NUM_CARDS));

  }

  @Override
  public void onCardUsed(AbstractCard card) {

  }

  @Override
  public void onCardExhausted(AbstractCard card) {

  }

  private static String getDescription() {
    return TEXT[0];
  }

  static {
    uiStrings =  CardCrawlGame.languagePack.getUIString(ID);
    NAME = uiStrings.TEXT[0];
    TEXT = Arrays.copyOfRange(uiStrings.TEXT, 1, uiStrings.TEXT.length);
  }
}
