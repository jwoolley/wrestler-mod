package thewrestler.patches.rewards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rewards.RewardItem;
import thewrestler.ui.WrestlerApprovalInfoPanel;
import thewrestler.util.BasicUtils;

@SpirePatch(
    clz = RewardItem.class,
    method = "applyGoldBonus",
    paramtypez = {
       boolean.class
    }
)

public class RewardItemApplyGoldBonusPatch {
  public static void Postfix(RewardItem __instance, boolean theft) {
    if (BasicUtils.isPlayingAsWrestler()) {
      // TODO: if has approval relic, update bonus amount & update text string (__instance.text) accordingly
    }
  }
}