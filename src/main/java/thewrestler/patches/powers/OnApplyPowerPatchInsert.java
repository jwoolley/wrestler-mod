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
import thewrestler.powers.CloverleafPower;
import thewrestler.relics.Headgear;
import thewrestler.relics.ImprovedHeadgear;
import thewrestler.util.CreatureUtils;
import thewrestler.util.info.CombatInfo;

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
//    WrestlerMod.logger.info("__instance: " + __instance + "; alreadyHandled: " + alreadyHandled);

    final int amountBeingApplied = __instance.amount;

    if (!alreadyHandled) {
      powerActionList.add(__instance);
      final AbstractPlayer player = AbstractDungeon.player;

      if (source == player && target != player &&  player.hasRelic(Headgear.ID)
          && !target.hasPower(ArtifactPower.POWER_ID) &&
          doesntAlreadyHaveAnyDebuffs(target, powerToApply, amountBeingApplied)) {
        ((Headgear) AbstractDungeon.player.getRelic(Headgear.ID)).onApplyPower(powerToApply);
      }

      if (source == player && target != player && player.hasRelic(ImprovedHeadgear.ID) && !target.hasPower(ArtifactPower.POWER_ID)) {
        ((ImprovedHeadgear) AbstractDungeon.player.getRelic(ImprovedHeadgear.ID)).onApplyPower(powerToApply);
      }

      if (source == player && target != player && target.hasPower(CloverleafPower.POWER_ID) && !target.hasPower(ArtifactPower.POWER_ID)) {
        ((CloverleafPower) target.getPower(CloverleafPower.POWER_ID)).onApplyPower(powerToApply);
      }

      incrementDebuffsApplied(target, source, powerToApply);
    }
  }

  private static boolean isApplyingStrengthDebuff(AbstractCreature target, AbstractPower powerToApply, int amountBeingApplied) {
    return StrengthPower.POWER_ID.equals(powerToApply.ID) &&
        target.getPower(StrengthPower.POWER_ID).amount >= 0 && amountBeingApplied < 0;
  }

  private static boolean doesntAlreadyHaveAnyDebuffs(AbstractCreature target,
                                                     AbstractPower powerToApply, int amountBeingApplied) {
    boolean hasBuffAlready = target != null && target.hasPower(powerToApply.ID);
    return (target instanceof AbstractMonster)
        && !target.isDeadOrEscaped()
        && CreatureUtils.getDebuffs(target).isEmpty()
        && powerToApply.type == AbstractPower.PowerType.DEBUFF
        && (!hasBuffAlready || isApplyingStrengthDebuff(target, powerToApply, amountBeingApplied));
  }

  private static boolean doesntAlreadyHaveDebuff(AbstractCreature target,
                                                 AbstractPower powerToApply, int amountBeingApplied) {
    boolean hasBuffAlready = target != null && target.hasPower(powerToApply.ID);

    return (target instanceof AbstractMonster)
        && !target.isDeadOrEscaped()
        && !target.hasPower(ArtifactPower.POWER_ID)
        && powerToApply.type == AbstractPower.PowerType.DEBUFF
        && (!hasBuffAlready || isApplyingStrengthDebuff(target, powerToApply, amountBeingApplied));
  }

  private static void incrementDebuffsApplied(AbstractCreature target, AbstractCreature source,
                                                 AbstractPower powerToApply) {
    if (source == AbstractDungeon.player && !target.hasPower(ArtifactPower.POWER_ID)
        && powerToApply.type == AbstractPower.PowerType.DEBUFF
        && (target instanceof AbstractMonster)
        && !target.isDeadOrEscaped()) {
      CombatInfo.incrementDebuffsAppliedCount();
      WrestlerMod.combatInfoPanel.updateCardCounts();
    }
  }

  public static final ArrayList<ApplyPowerAction> powerActionList = new ArrayList<ApplyPowerAction>();
}