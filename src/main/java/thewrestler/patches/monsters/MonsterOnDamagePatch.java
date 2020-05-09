package thewrestler.patches.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;
import thewrestler.WrestlerMod;
import thewrestler.glyphs.custom.CustomGlyph;
import thewrestler.powers.InjuredPower;
import thewrestler.ui.WrestlerCombatInfoPanel;
import thewrestler.util.BasicUtils;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MonsterOnDamagePatch {
//  private static int savedBlock = 0;
//  @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock")
//  public static class MonsterTestProtectedCallBeforePatch {
//    @SpirePrefixPatch
//    public static SpireReturn Prefix(AbstractCreature __instance, DamageInfo info, int damageAmount) {
//      final int originalBlock_debug = __instance.currentBlock;
//      savedBlock =  Math.min(InjuredPower.getTotalInjuryAmount(__instance), __instance.currentBlock);
//      __instance.currentBlock = __instance.currentBlock - savedBlock;
//
//      WrestlerMod.logger.info(
//          "MonsterTestProtectedCallBeforePatch::decrementBlock prefix patch called. damageAmount: "+ damageAmount
//              + "; original block: " + originalBlock_debug
//              + "; injury amount: " + InjuredPower.getTotalInjuryAmount(__instance)
//              + "; adjusted block: " + __instance.currentBlock
//              + "; savedBlock: " + savedBlock);
//
//      return SpireReturn.Continue();
//    }
//  }
//
//  @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock")
//  public static class MonsterTestProtectedCallAfterPatch {
//    @SpirePostfixPatch
//    public static void Postfix(AbstractCreature __instance, DamageInfo info, int damageAmount) {
//      final int postDecrementBlockUnadjusted_debug = __instance.currentBlock;
//      __instance.currentBlock = __instance.currentBlock + savedBlock;
//      savedBlock = 0;
//
//      WrestlerMod.logger.info(
//          "MonsterTestProtectedCallAfterPatch::decrementBlock postfix patch called. damageAmount: "+ damageAmount
//              + "; original block: " + postDecrementBlockUnadjusted_debug
//              + "; injury amount: " + InjuredPower.getTotalInjuryAmount(__instance)
//              + "; final adjusted block: " + __instance.currentBlock
//              + "; savedBlock: " + savedBlock);
//    }
//  }
}
