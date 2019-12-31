package thewrestler.util;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thewrestler.enums.WrestlerCharEnum;

public class BasicUtils {
  public static float percentageIntToFloat(int percentage) {
    return percentage / 100.0f;
  }

  public static boolean isPlayingAsWrestler() {
    return  AbstractDungeon.player != null && AbstractDungeon.player.chosenClass == WrestlerCharEnum.THE_WRESTLER;
  }

  public static boolean isPlayerInCombat() {
    return AbstractDungeon.isPlayerInDungeon()
        && AbstractDungeon.currMapNode != null
        && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
  }
}
