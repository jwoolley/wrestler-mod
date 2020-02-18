package thewrestler.patches.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CtBehavior;
import thewrestler.glyphs.custom.CustomGlyph;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RenderCustomGlyphsPatch {
  @SpirePatches({@SpirePatch(clz = AbstractCard.class, method = "renderDescription"), @SpirePatch(clz = AbstractCard.class, method = "renderDescriptionCN")})
  public static class RenderCustomGlyphInsertPatch {
    private static final float CARD_ENERGY_IMG_WIDTH = 24.0F * Settings.scale;

      @SpireInsertPatch(locator = Locator.class, localvars = {"spacing", "i", "start_x", "draw_y", "font", "textColor", "tmp", "gl"})
      public static void Insert(AbstractCard __instance, SpriteBatch sb, float spacing, int i, @ByRef float[] start_x, float draw_y, BitmapFont font, Color textColor, @ByRef String[] tmp, GlyphLayout gl) {
        for (CustomGlyph glyph :CustomGlyph.getCustomGlyphsList()) {
          java.util.regex.Matcher m = getMatchPattern(glyph.getCodeLetter()).matcher(tmp[0]);
          if ((tmp[0].equals(glyph.getFullCode())) || (m.find())) {
            gl.width = (CARD_ENERGY_IMG_WIDTH * __instance.drawScale);
            float tmp2 = (__instance.description.size() - 4) * spacing;
            __instance.renderSmallEnergy(sb, glyph.getGlyphAtlasRegion(), (start_x[0] - __instance.current_x) / Settings.scale / __instance.drawScale, -100.0F -

                ((__instance.description.size() - 4.0F) / 2.0F - i + 1.0F) * spacing);
            if ((!tmp[0].equals(glyph.getFullCode())) && (m.group(2).equals("."))) {
              FontHelper.renderRotatedText(sb, font, ".", __instance.current_x, __instance.current_y, start_x[0] - __instance.current_x + CARD_ENERGY_IMG_WIDTH * __instance.drawScale, i * 1.45F *

                  -font.getCapHeight() + draw_y - __instance.current_y - 6.0F, __instance.angle, true, textColor);
            }
            start_x[0] += gl.width;
            tmp[0] = "";
          }
        }
      }

    private static final Pattern getMatchPattern(char letterCode) {
      return Pattern.compile("\\[([" + letterCode + "])\\](\\.?) ");
    }

    private static class Locator
        extends SpireInsertLocator {
      public int[] Locate(CtBehavior ctBehavior)
          throws Exception {
        com.evacipated.cardcrawl.modthespire.lib.Matcher matcher = new Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
        int[] lines = LineFinder.findAllInOrder(ctBehavior, new ArrayList(), matcher);
        return new int[]{lines[(lines.length - 1)]};
      }
    }
  }
}
