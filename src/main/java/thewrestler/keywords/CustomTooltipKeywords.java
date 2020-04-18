package thewrestler.keywords;

import thewrestler.WrestlerMod;
import thewrestler.glyphs.custom.CustomGlyph;

import java.util.HashMap;
import java.util.Map;

public class CustomTooltipKeywords {
  static Map<String, CustomTooltipKeyword> customKeywords = new HashMap<>();

  public static final String PENALTY_CARD = WrestlerMod.makeID("PenaltyCard");
  public static final String PENALTY_CARD_BLUE = WrestlerMod.makeID("PenaltyCardBlue");
  public static final String PENALTY_CARD_GREEN = WrestlerMod.makeID("PenaltyCardGreen");
  public static final String PENALTY_CARD_ORANGE = WrestlerMod.makeID("PenaltyCardOrange");
  public static final String PENALTY_CARD_RED = WrestlerMod.makeID("PenaltyCardRed");
  public static final String PENALTY_CARD_YELLOW = WrestlerMod.makeID("PenaltyCardYellow");
  public static final String PENALTY_CARDS_NO_GLYPH = WrestlerMod.makeID("PenaltyCardsNoGlyph");
  public static final String SPORTSMANSHIP = WrestlerMod.makeID("Sportsmanship");
  public static final String DIRTY = WrestlerMod.makeID("Dirty");

  public static final String INJURY = WrestlerMod.makeID("Injury");
  public static final String PERSISTENT_INJURY = WrestlerMod.makeID("PersistentInjury");

  static {
    CustomTooltipKeywords.customKeywords.put(PENALTY_CARD,
        getCustomTooltipKeyword(PENALTY_CARD, CustomGlyph.CustomGlyphEnum.PENALTY_CARD));

    CustomTooltipKeywords.customKeywords.put(PENALTY_CARD_BLUE,
        getCustomTooltipKeyword(PENALTY_CARD_BLUE, CustomGlyph.CustomGlyphEnum.PENALTY_CARD_BLUE));

    CustomTooltipKeywords.customKeywords.put(PENALTY_CARD_GREEN,
        getCustomTooltipKeyword(PENALTY_CARD_GREEN, CustomGlyph.CustomGlyphEnum.PENALTY_CARD_GREEN));

    CustomTooltipKeywords.customKeywords.put(PENALTY_CARD_ORANGE,
        getCustomTooltipKeyword(PENALTY_CARD_ORANGE, CustomGlyph.CustomGlyphEnum.PENALTY_CARD_ORANGE));

    CustomTooltipKeywords.customKeywords.put(PENALTY_CARD_RED,
        getCustomTooltipKeyword(PENALTY_CARD_RED, CustomGlyph.CustomGlyphEnum.PENALTY_CARD_RED));

    CustomTooltipKeywords.customKeywords.put(PENALTY_CARD_YELLOW,
        getCustomTooltipKeyword(PENALTY_CARD_YELLOW, CustomGlyph.CustomGlyphEnum.PENALTY_CARD_YELLOW));

    CustomTooltipKeywords.customKeywords.put(PENALTY_CARDS_NO_GLYPH,
        getCustomTooltipKeyword(PENALTY_CARDS_NO_GLYPH));

    CustomTooltipKeywords.customKeywords.put(SPORTSMANSHIP, getCustomTooltipKeyword(SPORTSMANSHIP));

    CustomTooltipKeywords.customKeywords.put(DIRTY, getCustomTooltipKeyword(DIRTY));

    CustomTooltipKeywords.customKeywords.put(INJURY, getCustomTooltipKeyword(INJURY));

    CustomTooltipKeywords.customKeywords.put(PERSISTENT_INJURY, getCustomTooltipKeyword(PERSISTENT_INJURY));
  }

  public static CustomTooltipKeyword getTooltipKeyword(String id) {
    return customKeywords.get(id);
  }

  private static CustomTooltipKeyword getCustomTooltipKeyword(String id) {
    return getCustomTooltipKeyword(id, null);
  }

  private static CustomTooltipKeyword getCustomTooltipKeyword(String id, CustomGlyph.CustomGlyphEnum glyphEnum) {
    if (glyphEnum != null) {
      return new CustomTooltipKeyword(id,  CustomGlyph.getGlyph(glyphEnum));
    } else {
      return new CustomTooltipKeyword(id);
    }
  }
}
