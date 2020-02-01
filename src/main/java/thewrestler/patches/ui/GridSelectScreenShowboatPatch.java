package thewrestler.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import thewrestler.actions.cards.skill.ShowboatAction;

public class GridSelectScreenShowboatPatch {

  private static boolean shouldDisplayShowboatUpgrade = false;

  private static AbstractCard originalPreviewCard;

  public static void enableShowboatGrid() {
    shouldDisplayShowboatUpgrade = true;
  }

  public static void disableShowboatGrid() {
    shouldDisplayShowboatUpgrade = false;
  }


  public static void resetGridSelectScreenChanges() {
    shouldDisplayShowboatUpgrade = false;
    originalPreviewCard = null;
  }

  // clear hacked-in data
  @SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
  public static class AbstractDungeonCloseScreenResetShowboatPatch {
    @SpirePrefixPatch
    public static void Prefix() {
      if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
        resetGridSelectScreenChanges();
      }
    }
  }

  // clear hacked-in data
  @SpirePatch(clz = AbstractCard.class, method = "unhover")
  public static class AbstractCardHoverShowboatScreenPatch {
    @SpirePrefixPatch
    public static void Prefix(AbstractCard __instance) {
      if (__instance.hb.hovered) {
        originalPreviewCard = null;
      }
    }
  }


  @SpirePatch(clz = GridCardSelectScreen.class, method = "render")
  public static class GridSelectScreenRenderBeforePatch {
    @SpirePrefixPatch
    public static void Prefix(GridCardSelectScreen __instance) {
      if (shouldDisplayShowboatUpgrade && __instance.upgradePreviewCard != null && originalPreviewCard == null) {
        originalPreviewCard = __instance.upgradePreviewCard;
        __instance.upgradePreviewCard = __instance.upgradePreviewCard.makeStatEquivalentCopy();
        ShowboatAction.applyShowboatModifier(__instance.upgradePreviewCard);
      }
    }
  }

  @SpirePatch(clz = GridCardSelectScreen.class, method = "render")
  public static class GridSelectScreenRenderAfterPatch {
    @SpirePostfixPatch
    public static void Postfix(GridCardSelectScreen __instance) {
      if (originalPreviewCard != null) {
        __instance.upgradePreviewCard = originalPreviewCard;
        originalPreviewCard = null;
      }
    }
  }
}
