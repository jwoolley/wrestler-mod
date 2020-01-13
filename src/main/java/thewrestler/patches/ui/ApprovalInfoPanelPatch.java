package thewrestler.patches.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewrestler.WrestlerMod;
import thewrestler.ui.WrestlerApprovalInfoPanel;
import thewrestler.util.BasicUtils;

public class ApprovalInfoPanelPatch {

  @SpirePatch(clz = EnergyPanel.class, method = "update")
  public static class ApprovalInfoPanelUpdatePatch  {
    @SpirePrefixPatch
    public static void Prefix(EnergyPanel __instance) {
      if (BasicUtils.isPlayingAsWrestler()) {
        if (WrestlerApprovalInfoPanel.shouldRender()) {
          WrestlerMod.approvalInfoPanel.update();
        }
      }
    }
  }

  // for rendering in combat
  @SpirePatch(clz = EnergyPanel.class, method = "renderOrb")
  public static class ApprovalInfoPanelRenderPatch {
    @SpirePostfixPatch
    public static void Postfix(EnergyPanel __instance, SpriteBatch sb) {
      if (BasicUtils.isPlayingAsWrestler()) {
        if (WrestlerApprovalInfoPanel.shouldRender()) {
          WrestlerMod.approvalInfoPanel.render(sb);
        }
      }
    }
  }
}