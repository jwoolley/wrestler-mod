package thewrestler.util;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;

public class CardUtil {
  public static void discountRandomCardCost(List<AbstractCard> decreasableCards, int discountAmount,
                                            boolean forEntireCombat) {
    AbstractCard card = decreasableCards.get(AbstractDungeon.cardRng.random(decreasableCards.size() - 1));

    if (forEntireCombat) {
      card.modifyCostForCombat(-discountAmount);
    } else {
      card.modifyCostForTurn(-discountAmount);
    }

    card.superFlash(Color.GOLD.cpy());
  }
}
