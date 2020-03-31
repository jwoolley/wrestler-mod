package thewrestler.util;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.lwjgl.openal.AL;
import thewrestler.enums.WrestlerCharEnum;

import java.util.UUID;

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

  public static final int MAX_GENSTRING_LENGTH = 256;
  private static final String ALPHANUMERICS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  public static String generateRandomAlphanumeric(int length) {
      StringBuilder sb = new StringBuilder();
      if (length > 0 && length < MAX_GENSTRING_LENGTH) {
        for (int i = 0; i < length; i++) {
          sb.append(ALPHANUMERICS.charAt((int)(Math.random() * ALPHANUMERICS.length())));
        }
      }
      return sb.toString();
  }
}
