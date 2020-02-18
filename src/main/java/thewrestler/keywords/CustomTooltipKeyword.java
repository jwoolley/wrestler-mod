package thewrestler.keywords;

import com.megacrit.cardcrawl.localization.Keyword;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import thewrestler.WrestlerMod;
import thewrestler.glyphs.custom.CustomGlyph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomKeyword {
  public static final String PENALTYCARD_KEYWORD_ID = WrestlerMod.makeID("PenalrtyCard");
  public static final String SPORTSMANSHIP_KEYWORD_ID = WrestlerMod.makeID("Sportsmanship");
  public static final String DIRTY_KEYWORD_ID = WrestlerMod.makeID("Dirty");

  private static Map<String, CustomKeyword> customKeywords = new HashMap<>();

  static {
    customKeywords.put(PENALTYCARD_KEYWORD_ID, new CustomKeyword(PENALTYCARD_KEYWORD_ID));
    customKeywords.put(SPORTSMANSHIP_KEYWORD_ID, new CustomKeyword(SPORTSMANSHIP_KEYWORD_ID));
    customKeywords.put(DIRTY_KEYWORD_ID, new CustomKeyword(DIRTY_KEYWORD_ID));
  }

  private final String id;
  private final CustomGlyph glyph;
  private final boolean isBaseGameKeyword;

  public CustomKeyword(String id) {
    this(id, null);
  }

  public CustomKeyword(String id, CustomGlyph glyph) {
    this(id, glyph, false);
  }

  public CustomKeyword(String id, boolean isBaseGameKeyword) {
    this(id, null, isBaseGameKeyword);
  }

  public CustomKeyword(String id, CustomGlyph glyph, boolean isBaseGameKeyword) {
    this.id = id;
    this.glyph = glyph;
    this.isBaseGameKeyword = isBaseGameKeyword;
  }


  // TODO: populate customKeywords with the keywords


  public static CustomKeyword getCustomKeyword(String keywordId) {
    if (!customKeywords.containsKey(keywordId)) {
      CustomKeyword kw = new CustomKeyword(keywordId, false);
      customKeywords.put(keywordId, kw);
    }
    return customKeywords.get(keywordId);
  }

  public static CustomKeyword getWrappedBaseGameKeyword(String keywordId) {
    if (!customKeywords.containsKey(keywordId)) {
      CustomKeyword kw = new CustomKeyword(keywordId, true);
      customKeywords.put(keywordId, kw);
    }
    return customKeywords.get(keywordId);
  }

  private static CustomKeyword getKeywordFromMap(String keywordId, boolean isBaseGameKeyword) {
    if (!customKeywords.containsKey(keywordId)) {
      CustomKeyword kw = new CustomKeyword(keywordId, isBaseGameKeyword);
      customKeywords.put(keywordId, kw);
    }
    return customKeywords.get(keywordId);
  }

  public String getProperName() {
    if (this.isBaseGameKeyword) {
      return
    }
    return WrestlerMod.getKeyword(this.id).PROPER_NAME;
  }

  public String getDescription() {
    return WrestlerMod.getKeyword(this.id).DESCRIPTION;
  }

  public static Map<String, Keywords> getKeywords(List<String> keywordList, List<Keyword> baseGameKeywordList) {
    Map<String, Keywords> keywords = keywordList.stream()
        .collect(Collectors.toMap(k -> k, CustomKeyword::getCustomKeyword))
        .entrySet().stream()
        .filter(e -> e.getValue() != null)
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

    keywords.putAll(baseGameKeywordList.stream()
        .collect(Collectors.toMap(kw -> kw.NAMES[0], kw -> kw.DESCRIPTION)));

    return keywords;
  }


}
