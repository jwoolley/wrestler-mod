package thewrestler.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.UnceasingTop;
import thewrestler.powers.NearFallPower;

@SpirePatch(
    clz = GameActionManager.class,
    method = "update"
)

public class GameActionManagerGetNextActionListener {
  /*
  DISABLING TO TRY NEAR-FALL REWORK;
  TODO: REMOVE THIS CLASS IF NOT USED
  public static void Prefix(GameActionManager __instance) {
    // player has the Near-Fall power
    // logically this check should be in the center of the conditions,
    // but doing it this way preempts all the following checks if player doesn't have the power
    if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(NearFallPower.POWER_ID)) {

      // getNextAction() expected to be called
      if (__instance.phase == GameActionManager.Phase.WAITING_ON_USER
          || __instance.phase == GameActionManager.Phase.EXECUTING_ACTIONS &&
          (__instance.currentAction == null || __instance.currentAction.isDone)) {

        // UnceasingTop.disableUntilTurnEnds() expected to be called;
        if (__instance.actions.isEmpty() && __instance.preTurnActions.isEmpty() &&
            __instance.cardQueue.size() == 1 && (__instance.cardQueue.get(0)).isEndTurnAutoPlay) {
          ((NearFallPower)AbstractDungeon.player.getPower(NearFallPower.POWER_ID)).disableForTurn();
        }
      }
    }
  }
  */
}
