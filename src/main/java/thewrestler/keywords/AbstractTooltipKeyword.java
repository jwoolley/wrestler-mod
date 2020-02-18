package thewrestler.keywords;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.helpers.PowerTip;
import thewrestler.glyphs.custom.CustomGlyph;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTooltipKeyword {
  public abstract String getProperName();
  public abstract String getDescription();
  public abstract boolean hasGlyph();
  public abstract CustomGlyph getGlyph();

  private static Map<String, TooltipInfo> tooltipInfos = new HashMap<>();
  private static Map<String, PowerTip> powerTips = new HashMap<>();


  public TooltipInfo getTooltipInfo() {
    if (!tooltipInfos.containsKey(this.getProperName())) {
      tooltipInfos.put(this.getProperName(), new TooltipInfo(this.getProperName(), this.getDescription()));
    }
    return tooltipInfos.get(this.getProperName());
  }

  public PowerTip getPowerTip() {
    if (!powerTips.containsKey(this.getProperName())) {
      PowerTip powerTip;
      if (this.hasGlyph()) {
        powerTip = new PowerTip(this.getProperName(), this.getDescription(), this.getGlyph().getGlyphAtlasRegion());
      } else {
        powerTip = new PowerTip(this.getProperName(), this.getDescription());
      }
      powerTips.put(this.getProperName(), powerTip);
    }

    return powerTips.get(this.getProperName());
  }
}
