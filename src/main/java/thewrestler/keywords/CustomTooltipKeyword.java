package thewrestler.keywords;

import thewrestler.WrestlerMod;
import thewrestler.glyphs.custom.CustomGlyph;

public class CustomTooltipKeyword extends AbstractTooltipKeyword {

  private final String id;
  private final CustomGlyph glyph;

  CustomTooltipKeyword(String id) {
    this(id, null);
  }

  CustomTooltipKeyword(String id, CustomGlyph glyph) {
    this(id, glyph, false);
  }

  private CustomTooltipKeyword(String id, CustomGlyph glyph, boolean isBaseGameKeyword) {
    this.id = id;
    this.glyph = glyph;
  }

  public String getProperName() {
    return WrestlerMod.getKeyword(this.id).PROPER_NAME;
  }

  public String getDescription() {
    return WrestlerMod.getKeyword(this.id).DESCRIPTION;
  }

  public boolean hasGlyph() {
    return this.glyph != null;
  }

  public CustomGlyph getGlyph() {
    return this.glyph;
  }

  static CustomTooltipKeyword getCustomKeyword(String keywordId) {
    if (!CustomTooltipKeywords.customKeywords.containsKey(keywordId)) {
      CustomTooltipKeyword kw = new CustomTooltipKeyword(keywordId);
      CustomTooltipKeywords.customKeywords.put(keywordId, kw);
    }
    return CustomTooltipKeywords.customKeywords.get(keywordId);
  }
}
