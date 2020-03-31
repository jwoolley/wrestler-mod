package thewrestler.keywords;

import com.megacrit.cardcrawl.localization.Keyword;
import thewrestler.glyphs.custom.CustomGlyph;

import java.util.HashMap;
import java.util.Map;

public class BaseGameTooltipKeyword extends AbstractTooltipKeyword {
  private static Map<Keyword, BaseGameTooltipKeyword> baseGameKeywords = new HashMap<>();
  private Keyword keyword;

  static BaseGameTooltipKeyword getTooltipKeyword(Keyword keyword) {
    if (!baseGameKeywords.containsKey(keyword)) {
      baseGameKeywords.put(keyword, new BaseGameTooltipKeyword(keyword));
    }
    return baseGameKeywords.get(keyword);
  }

  BaseGameTooltipKeyword(Keyword keyword) {
    this.keyword = keyword;
  }

  @Override
  public String getProperName() {
    return keyword.NAMES[0];
  }

  @Override
  public String getDescription() {
    return keyword.DESCRIPTION;
  }

  @Override
  public boolean hasGlyph() {
    return false;
  }

  @Override
  public CustomGlyph getGlyph() {
    return null;
  }
}
