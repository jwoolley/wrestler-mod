package thewrestler.patches.rewards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import thewrestler.relics.FightCard;

@SpirePatch(
    clz = CombatRewardScreen.class,
    method = "setupItemReward"
)

public class CombatRewardSpecificRelicPatch {
  public static void Postfix(CombatRewardScreen __instance) {
    if (AbstractDungeon.player.hasRelic(FightCard.ID)) {
      FightCard fightCardRelic = (FightCard) AbstractDungeon.player.getRelic(FightCard.ID);

      if (fightCardRelic.shouldRewardRelic()) {
        final RewardItem rewardItem = new RewardItem(fightCardRelic.redeemRelicReward());
        __instance.rewards.add(rewardItem);
        final int offsetIndex = __instance.rewards.size() - 1;
        rewardItem.move(Settings.HEIGHT / 2.0F + 124.0F * Settings.scale - offsetIndex * 100.0F * Settings.scale);
        __instance.hasTakenAll = false;
      }
    }
  }
}
