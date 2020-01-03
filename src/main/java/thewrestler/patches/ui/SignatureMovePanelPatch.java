package thewrestler.patches.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewrestler.WrestlerMod;
import thewrestler.ui.WrestlerCombatInfoPanel;
import thewrestler.util.BasicUtils;

public class SignatureMovePanelPatch {
  @SpirePatch(clz = EnergyPanel.class, method = "update")
  public static class SignatureMovePanelUpdatePatch {
    @SpirePrefixPatch
    public static void Prefix(EnergyPanel __instance) {
      if (BasicUtils.isPlayingAsWrestler()) {
        if (WrestlerCombatInfoPanel.shouldRender()) {
          WrestlerMod.signatureMovePanel.update();
        }
      }
    }
  }

  // for rendering in combat
  @SpirePatch(clz = EnergyPanel.class, method = "renderOrb")
  public static class SignatureMovePanelRenderPatch {
    @SpirePostfixPatch
    public static void Postfix(EnergyPanel __instance, SpriteBatch sb) {
      if (BasicUtils.isPlayingAsWrestler()) {
        if (WrestlerCombatInfoPanel.shouldRender()) {
          WrestlerMod.signatureMovePanel.render(sb);
        }
      }
    }
  }
}