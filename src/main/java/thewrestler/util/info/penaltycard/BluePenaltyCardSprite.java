package thewrestler.util.info.penaltycard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import thewrestler.WrestlerMod;
import thewrestler.glyphs.custom.CustomGlyph;

import java.util.Arrays;

public class BluePenaltyCardSprite extends AbstractPenaltyCardSprite {
  private static final UIStrings uiStrings;

  public static final String ID = "WrestlerMod:BluePenaltyCard";
  public static final String IMG_FILENAME = "blue.png";
  public static final String GLYPH_IMG_FILENAME = "blue.png";
  public static CustomGlyph.CustomGlyphEnum GLYPH_ENUM = CustomGlyph.CustomGlyphEnum.PENALTY_CARD_BLUE;
  public static final String NAME;
  public static final String[] TEXT;

  public BluePenaltyCardSprite() {
    super(ID, NAME, getDescription(), IMG_FILENAME);
  }

  public static CustomGlyph.CustomGlyphEnum getGlyphEnum() {
    return GLYPH_ENUM;
  }

  @Override
  public CustomGlyph getTooltipGlyph() {
    return CustomGlyph.getGlyph(getGlyphEnum());
  }

  @Override
  public AbstractPenaltyCardSprite makeCopy() {
    return new BluePenaltyCardSprite();
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
    this.flash();
    playTriggerSfx();
    WrestlerMod.logger.info("Triggered penalty card [" + getDebugDescription() + "]");
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
    return TEXT[0];
  }

  static {
    uiStrings =  CardCrawlGame.languagePack.getUIString(ID);
    NAME = uiStrings.TEXT[0];
    TEXT = Arrays.copyOfRange(uiStrings.TEXT, 1, uiStrings.TEXT.length);
  }
}