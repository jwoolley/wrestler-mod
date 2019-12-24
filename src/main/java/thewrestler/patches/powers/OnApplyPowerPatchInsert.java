package thewrestler.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.WrestlerMod;
import thewrestler.relics.Headgear;

import java.util.ArrayList;

@SpirePatch(
    clz = ApplyPowerAction.class,
    method = "update"
)

public class OnApplyPowerPatchInsert {
  @SpireInsertPatch(
      rloc = 0,
      localvars = {"source", "target", "powerToApply"}
  )
  public static void Insert(ApplyPowerAction __instance, AbstractCreature source, AbstractCreature target,
                            AbstractPower powerToApply) {

    final boolean alreadyHandled = powerActionList.contains(__instance);
    WrestlerMod.logger.info("__instance: " + __instance + "; alreadyHandled: " + alreadyHandled);

    if (!alreadyHandled) {
      powerActionList.add(__instance);
      final AbstractPlayer player = AbstractDungeon.player;
      boolean hasBuffAlready = target != null && target.hasPower(powerToApply.ID);

      if (player.hasRelic(Headgear.ID)
          && source == player
          && (target instanceof AbstractMonster)
          && !target.isDeadOrEscaped()
          && !target.hasPower(ArtifactPower.POWER_ID)
          && powerToApply.type == AbstractPower.PowerType.DEBUFF
          && (!hasBuffAlready || StrengthPower.POWER_ID.equals(powerToApply.ID) &&
            target.getPower(StrengthPower.POWER_ID).amount >= 0 && __instance.amount < 0)) {
        ((Headgear) AbstractDungeon.player.getRelic(Headgear.ID)).onApplyPower(powerToApply);
      }
    }
  }
  public static final ArrayList<ApplyPowerAction> powerActionList = new ArrayList<ApplyPowerAction>();
}