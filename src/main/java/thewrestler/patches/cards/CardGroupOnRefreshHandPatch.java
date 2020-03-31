package thewrestler.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.powers.NearFallPower;

@SpirePatch(
    clz = CardGroup.class,
    method = "refreshHandLayout"
)
public class CardGroupOnRefreshHandPatch {
  public static void Postfix(CardGroup __instance) {
    if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(NearFallPower.POWER_ID)) {
      ((NearFallPower)AbstractDungeon.player.getPower(NearFallPower.POWER_ID)).onRefreshHand();
    }
  }
}