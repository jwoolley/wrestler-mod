package thewrestler.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.Keyword;
import thewrestler.WrestlerMod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class CustomWrestlerRelic extends CustomRelic {
  public CustomWrestlerRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
    super(id, texture, outline, tier, sfx);

    getKeywords().entrySet().forEach(entry -> {
      this.tips.add(new PowerTip(entry.getKey(), entry.getValue()));
    });
  }

  abstract protected List<String> getKeywordList();
  abstract protected List<Keyword> getBaseGameKeywordList();

  protected Map<String, String> getKeywords() {
    Map<String, String> keywords = new HashMap<>();

    final List<String> keywordList = getKeywordList();
    if (keywordList != null && !keywordList.isEmpty()) {
      keywords.putAll(
          getKeywordList().stream()
          .map(k -> WrestlerMod.getKeyword(k))
          .filter(Objects::nonNull)
          .collect(Collectors.toMap(kw -> kw.PROPER_NAME, kw -> kw.DESCRIPTION))
      );
    }

    final List<Keyword> baseGameKeywords = getBaseGameKeywordList();
    if (baseGameKeywords != null && !baseGameKeywords.isEmpty())
    keywords.putAll(getBaseGameKeywordList().stream()
        .collect(Collectors.toMap(kw -> kw.NAMES[0], kw -> kw.DESCRIPTION)));

    return keywords;
  }
}
