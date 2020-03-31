package thewrestler.patches.powers;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BufferPower;
import thewrestler.powers.TemporaryBufferPower;


@SpirePatch(
    clz = BufferPower.class,
    method = "onAttackedToChangeDamage",
    paramtypez = {
      DamageInfo.class,
      int.class
    }
)

public class TemporaryBufferPreemptBufferPatch {
  public static SpireReturn Prefix (BufferPower __instance, DamageInfo __damageInfo, int damageAmount) {
    // if the player has Temporary Buffer, expend that before expending normal Buffer
    if (AbstractDungeon.player.hasPower(TemporaryBufferPower.POWER_ID)) {
      return SpireReturn.Return(damageAmount);
    }
    return SpireReturn.Continue();
  }
}
