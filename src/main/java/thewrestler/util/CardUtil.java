package thewrestler.util;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.*;

public class CardUtil {
  public static void discountRandomCardCost(List<AbstractCard> decreasableCards, int discountAmount,
                                            boolean forEntireCombat) {
    AbstractCard card = decreasableCards.get(AbstractDungeon.cardRandomRng.random(decreasableCards.size() - 1));

    if (forEntireCombat) {
      card.modifyCostForCombat(-discountAmount);
    } else {
      card.setCostForTurn(card.costForTurn - discountAmount);
    }

    card.superFlash(Color.GOLD.cpy());
  }

  public static boolean isCardInHand(AbstractCard card) {
    return BasicUtils.isPlayerInCombat() && AbstractDungeon.player.hand.group.contains(card);
  }

  private static boolean cardHasCardId(AbstractCard card, String targetCardId) {
    return card != null && card.cardID != null && card.cardID.equals(targetCardId);
  }

  public static HashMap<CardGroup, AbstractCard> getAllInBattleInstances(String cardId) {
    HashMap<CardGroup, AbstractCard> cards = new HashMap();
    for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
      if (cardHasCardId(c, cardId)) {
        cards.put(AbstractDungeon.player.drawPile, c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
      if (cardHasCardId(c, cardId)) {
        cards.put(AbstractDungeon.player.discardPile, c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
      if (cardHasCardId(c, cardId)) {
        cards.put(AbstractDungeon.player.exhaustPile, c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.limbo.group) {
      if (cardHasCardId(c, cardId)) {
        cards.put(AbstractDungeon.player.limbo, c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.hand.group) {
      if (cardHasCardId(c, cardId)) {
        cards.put(AbstractDungeon.player.hand, c);
      }
    }
    return cards;
  }
}
