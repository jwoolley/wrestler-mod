package thewrestler.patches.unused.ui.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import thewrestler.WrestlerMod;

@SpirePatch(
    clz = AbstractCard.class,
    method = "makeStatEquivalentCopy"
)

public class OnMakeStatEquivalentCopyPatch {
//  public static void Postfix(AbstractCard __instance) {
//    WrestlerMod.logger.info("OnMakeStatEquivalentCopyPatch::Postfix called for card: " + __instance.name);
//  }
}