package thewrestler.patches.rewards;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import thewrestler.rewards.TrademarkMoveReward;

public class TrademarkMoveRewardPatch {

  @SpireEnum
  public static RewardItem.RewardType WRESTLER_TRADEMARK_MOVE_REWARD;

  @SpirePatch(clz = MonsterRoomElite.class, method = "dropReward")
  public static class RewardScreenTrademarkMovePatch {


    @SpirePostfixPatch
    public static void Postfix(MonsterRoomElite __instance) {
      // TODO: rolling percentage chance (a la potion / relic / rare card chance)
      __instance.rewards.add(new TrademarkMoveReward());
    }
  }
}
