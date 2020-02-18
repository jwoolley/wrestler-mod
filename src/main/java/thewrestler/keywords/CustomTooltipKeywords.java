package thewrestler.keywords;

import thewrestler.WrestlerMod;
import thewrestler.glyphs.custom.CustomGlyph;

import java.util.HashMap;
import java.util.Map;

public class CustomTooltipKeywords {
  static Map<String, CustomTooltipKeyword> customKeywords = new HashMap<>();

  public static final String PENALTY_CARD = WrestlerMod.makeID("PenaltyCard");
  public static final String SPORTSMANSHIP = WrestlerMod.makeID("Sportsmanship");
  public static final String DIRTY = WrestlerMod.makeID("Dirty");

  static {
    CustomTooltipKeywords.customKeywords.put(PENALTY_CARD, getCustomTooltipKeyword(PENALTY_CARD, CustomGlyph.CustomGlyphEnum.PENALTY_CARD));
    CustomTooltipKeywords.customKeywords.put(SPORTSMANSHIP, getCustomTooltipKeyword(SPORTSMANSHIP));
    CustomTooltipKeywords.customKeywords.put(DIRTY, getCustomTooltipKeyword(DIRTY));
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
