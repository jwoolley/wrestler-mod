package thewrestler.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import thewrestler.WrestlerMod;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.BasicUtils;


public class SignatureMoveInfoInitializePatch {

  @SpirePatch(clz = DungeonMapScreen.class, method = "open", paramtypez = { boolean.class } )
  public static class SignatureMoveInfoInitializeOnRunStartPatch {
    @SpirePrefixPatch
    public static void Postfix(DungeonMapScreen __instance, boolean doScrollingAnimation) {
      if (BasicUtils.isPlayingAsWrestler() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMPLETE &&
          WrestlerCharacter.getSignatureMoveInfo() == null) {
        WrestlerMod.logger.info("SignatureMoveInfoInitializePatch::Postfix initializing signatureMoveInfo");
        WrestlerCharacter.setSignatureMoveInfo(WrestlerCharacter.initializeSignatureMoveInfo());
      }
    }
  }
}
