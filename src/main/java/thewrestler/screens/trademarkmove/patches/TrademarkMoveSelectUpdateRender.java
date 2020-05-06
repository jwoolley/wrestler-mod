package thewrestler.screens.trademarkmove.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import thewrestler.WrestlerMod;
import thewrestler.screens.trademarkmove.TrademarkMoveSelectScreen;

import java.util.ArrayList;

public class TrademarkMoveSelectUpdateRender
{
  @SpirePatch(cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon", method="update")
  public static class Update
  {
    @SpireInsertPatch(locator=Locator.class)
    public static void Insert(AbstractDungeon __instance)
    {
      if (AbstractDungeon.screen == TrademarkMoveSelectScreen.Enum.TRADEMARK_MOVE_SELECT) {
        WrestlerMod.getTrademarkMoveSelectScreen().update();
      }
    }

    private static class Locator
        extends SpireInsertLocator
    {
      public int[] Locate(CtBehavior ctBehavior)
          throws Exception
      {
        Matcher finalMatcher = new Matcher.FieldAccessMatcher("com.megacrit.cardcrawl.dungeons.AbstractDungeon", "screen");

        return LineFinder.findInOrder(ctBehavior, new ArrayList(), finalMatcher);
      }
    }
  }

  @SpirePatch(cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon", method="render")
  public static class Render
  {
    @SpireInsertPatch(locator=Locator.class)
    public static void Insert(AbstractDungeon __instance, SpriteBatch sb)
    {
      if (AbstractDungeon.screen == TrademarkMoveSelectScreen.Enum.TRADEMARK_MOVE_SELECT) {
        WrestlerMod.getTrademarkMoveSelectScreen().render(sb);
      }
    }

    private static class Locator
        extends SpireInsertLocator
    {
      public int[] Locate(CtBehavior ctBehavior)
          throws Exception
      {
        Matcher finalMatcher = new Matcher.FieldAccessMatcher("com.megacrit.cardcrawl.dungeons.AbstractDungeon", "screen");

        return LineFinder.findInOrder(ctBehavior, new ArrayList(), finalMatcher);
      }
    }
  }

  @SpirePatch(cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon", method="openPreviousScreen")
  public static class OpenPreviousScreen
  {
    public static void Postfix(AbstractDungeon.CurrentScreen s)
    {
      if (s == TrademarkMoveSelectScreen.Enum.TRADEMARK_MOVE_SELECT) {
        WrestlerMod.getTrademarkMoveSelectScreen().reopen();
      }
    }
  }
}
