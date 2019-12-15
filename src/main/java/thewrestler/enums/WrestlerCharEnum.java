package thewrestler.enums;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import thewrestler.WrestlerMod;

import java.util.Arrays;
import java.util.List;

public class WrestlerCharEnum {
  @SpireEnum
  public static AbstractPlayer.PlayerClass THE_WRESTLER;

  public enum Theme {
    BASE_THEME(WrestlerMod.makeID("BaseTheme"));

    private final String themeId;

    Theme(String themeId) {
      this.themeId = themeId;
    }

    // Construct each cards here instead of having them in each enum's constructor, due to performance.
    public List<AbstractCard> getStartingDeck() throws Exception {
      throw new Exception("Unknown Theme enum");
    }

    public String getStartingRelicId() throws Exception {
      throw new Exception("Unknown Theme enum");
    }

    @Override
    public String toString() {
      return themeId;
    }

    public static Theme fromString(String id) {
      return Arrays.stream(Theme.values())
          .filter(theme -> theme.themeId == id)
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("Unknown theme ID: " + id));
    }
  }
}