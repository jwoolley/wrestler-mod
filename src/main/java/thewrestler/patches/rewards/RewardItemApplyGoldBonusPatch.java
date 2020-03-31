package thewrestler.patches.rewards;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import thewrestler.relics.RingCard;

@SpirePatch(
    clz = RewardItem.class,
    method = "applyGoldBonus",
    paramtypez = {
       boolean.class
    }
)

public class RewardItemApplyGoldBonusPatch {
  public static void Postfix(RewardItem __instance, boolean theft) {
    if (AbstractDungeon.player.hasRelic(RingCard.ID)) {
      if (!(AbstractDungeon.getCurrRoom() instanceof TreasureRoom) &&
          ((RingCard)AbstractDungeon.player.getRelic(RingCard.ID)).shouldRewardGold()) {

        final int goldBonus = MathUtils.round(__instance.goldAmt * RingCard.REWARD_PERCENTAGE_BONUS / 100.0f);

        if (goldBonus > 0) {
          __instance.bonusGold += goldBonus;
          __instance.text = (__instance.goldAmt + RewardItem.TEXT[1] + " (" + __instance.bonusGold + ")");
        }
      }
    }
  }
}