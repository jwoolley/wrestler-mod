package thewrestler.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.relics.Headgear;

@SpirePatch(
    clz = ApplyPowerAction.class,
    method = "update"
)

public class OnApplyPowerPatchInsert {

  @SpireInsertPatch(
      rloc=0,
      localvars={"source", "target", "powerToApply"}
  )
  public static void Insert(ApplyPowerAction __instance, AbstractCreature source, AbstractCreature target,
                             AbstractPower powerToApply) {

//    System.out.println("OnApplyPowerPatchBefore::Insert called.");
//    System.out.println("OnApplyPowerPatchBefore::Insert source: " + source);
//    System.out.println("OnApplyPowerPatchBefore::Insert target: " + target);
//    System.out.println("OnApplyPowerPatchBefore::Insert powerToApply: " + powerToApply);

    boolean hasBuffAlready = target.hasPower(powerToApply.ID);

//    System.out.println("OnApplyPowerPatchBefore::Insert hasBuffAlready: " + hasBuffAlready);
//    System.out.println("OnApplyPowerPatchBefore::Insert isDeadOrEscaped: " + target.isDeadOrEscaped());

    // TODO: check for artifact power

    final AbstractPlayer player = AbstractDungeon.player;
    boolean playerHasHeadgear = player.hasRelic(Headgear.ID);
    boolean isDebuff = powerToApply.type == AbstractPower.PowerType.DEBUFF;
    boolean sourceIsPlayer = source == player;
    boolean targetIsEnemy = target instanceof AbstractMonster;

    boolean shouldTrigger = playerHasHeadgear && isDebuff && sourceIsPlayer && targetIsEnemy
        && !hasBuffAlready && targetIsEnemy && !target.isDeadOrEscaped();

    if (player.hasRelic(Headgear.ID)
        && powerToApply.type == AbstractPower.PowerType.DEBUFF
        && source == player && (target instanceof AbstractMonster)
        && !target.isDeadOrEscaped()
        && !hasBuffAlready) {
      ((Headgear)AbstractDungeon.player.getRelic(Headgear.ID)).onApplyPower(powerToApply);
    }
  }
}