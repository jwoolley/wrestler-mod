package thewrestler.screens.trademarkmove.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import thewrestler.WrestlerMod;

import java.util.ArrayList;
import java.util.List;

public class TintCardPatch {
  private final static Color TINTED = new Color(0.0f, 0.0f, 0.0f, 0.5f);
  private final static Color UNTINTED = new Color(1.0f, 1.0f, 1.0f, 0.0f);

  private final static List<AbstractCard> tintedCards = new ArrayList<>();

  private static boolean tintedCardsChanged = false;

  public static void tintCard(AbstractCard card) {
    if (!tintedCards.contains(card)) {
      ReflectionHacks.setPrivate(card, AbstractCard.class, "tintColor", TINTED);
        tintedCards.add(card);
        tintedCardsChanged = true;
    }
  }
  public static void untintCard(AbstractCard card) {
    if (tintedCards.contains(card)) {
      ReflectionHacks.setPrivate(card, AbstractCard.class, "tintColor", UNTINTED);
      tintedCards.remove(card);
      tintedCardsChanged = true;
    }
  }

  public static void clearTintedCards() {
    tintedCards.clear();
  }

  @SpirePatch(clz = AbstractCard.class, method = "renderTint")
  public static class TintCardPrefixPatch {
    @SpirePostfixPatch
    public static void Prefix(AbstractCard __instance, SpriteBatch sb) {
      tintedCardsChanged = false;
    }
  }
}
