package thewrestler.keywords;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.Keyword;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TooltipKeywords {
  public static List<AbstractTooltipKeyword> getTooltipKeywords(List<String> keywordList, List<Keyword> baseGameKeywordList) {
    List<AbstractTooltipKeyword> keywords = keywordList.stream()
        .map(CustomTooltipKeyword::getCustomKeyword)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    keywords.addAll(baseGameKeywordList.stream()
        .map(BaseGameTooltipKeyword::getTooltipKeyword)
        .collect(Collectors.toList()));

    return keywords;
  }

  public static List<TooltipInfo> getTooltipInfos(List<AbstractTooltipKeyword> keywords) {
    return keywords.stream().map(kw -> kw.getTooltipInfo()).collect(Collectors.toList());
  }


  public static List<PowerTip> getPowerTips(List<AbstractTooltipKeyword> keywords) {
    return keywords.stream().map(AbstractTooltipKeyword::getPowerTip).collect(Collectors.toList());
  }
}