package thewrestler.patches.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import javassist.CtBehavior;
import thewrestler.WrestlerMod;

import java.util.ArrayList;

public class AbstractDungeonScreenPatch {

  @SpirePatches({@SpirePatch(clz = AbstractDungeon.class, method = "render")})
  public static class RenderDungeonPatch {
    @SpireInsertPatch(locator = AbstractDungeonScreenPatch.RenderDungeonPatch.Locator.class)
    public static void Insert(AbstractDungeon __instance, SpriteBatch sb) {
      WrestlerMod.getTrademarkMoveSelectScreen().render(sb);
    }

    private static class Locator extends SpireInsertLocator {
      public int[] Locate(CtBehavior ctBehavior) throws Exception {
        Matcher matcher = new Matcher.MethodCallMatcher(CancelButton.class, "render");
        int[] lines = LineFinder.findAllInOrder(ctBehavior, new ArrayList(), matcher);
        return new int[]{lines[(lines.length - 1)]};
      }
    }
  }

  @SpirePatches({@SpirePatch(clz = AbstractDungeon.class, method = "update")})
  public static class UpdateDungeonPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractDungeon __instance) {
      WrestlerMod.getTrademarkMoveSelectScreen().update();
    }
  }
}
