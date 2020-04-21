package thewrestler.util;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.omg.PortableInterceptor.ACTIVE;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

  public static void makeCardDirty(CustomCard card) {
    final AbstractCard.CardType type = card.type;
    if (type == AbstractCard.CardType.ATTACK) {
      card.setBackgroundTexture(WrestlerMod.ATTACK_WRESTLER_DIRTY_ORANGE, WrestlerMod.ATTACK_DEFAULT_DIRTY_ORANGE_PORTRAIT);
    } else if (type == AbstractCard.CardType.POWER) {
      card.setBackgroundTexture(WrestlerMod.POWER_WRESTLER_DIRTY_ORANGE, WrestlerMod.POWER_DEFAULT_DIRTY_ORANGE_PORTRAIT);
    } else {
      card.setBackgroundTexture(WrestlerMod.SKILL_WRESTLER_DIRTY_ORANGE, WrestlerMod.SKILL_DEFAULT_DIRTY_ORANGE_PORTRAIT);
    }

    card.tags.add(WrestlerCardTags.DIRTY);
  }

  public static boolean isCardInHand(AbstractCard card) {
    return BasicUtils.isPlayerInCombat() && AbstractDungeon.player.hand.group.contains(card);
  }

  private static boolean cardHasCardId(AbstractCard card, String targetCardId) {
    return card != null && card.cardID != null && card.cardID.equals(targetCardId);
  }

  public static HashMap<CardGroup, AbstractCard> getAllInBattleInstances(Predicate<AbstractCard> predicate) {
    HashMap<CardGroup, AbstractCard> cards = new HashMap();
    for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
      if (predicate.test(c)) {
        cards.put(AbstractDungeon.player.drawPile, c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
      if (predicate.test(c)) {
        cards.put(AbstractDungeon.player.discardPile, c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
      if (predicate.test(c)) {
        cards.put(AbstractDungeon.player.exhaustPile, c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.limbo.group) {
      if (predicate.test(c)) {
        cards.put(AbstractDungeon.player.limbo, c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.hand.group) {
      if (predicate.test(c)) {
        cards.put(AbstractDungeon.player.hand, c);
      }
    }
    return cards;
  }

  public static HashMap<CardGroup, AbstractCard> getAllInBattleInstances(String cardId) {
    return getAllInBattleInstances(c -> cardHasCardId(c, cardId));
  }

  public static void forAllCardsInCombat(Consumer<AbstractCard> fn) {
    forAllCardsInCombat(fn, c -> true);
  }

  public static void forAllCardsInCombat(Consumer<AbstractCard> fn, Predicate<AbstractCard> predicate) {
    for (AbstractCard c : AbstractDungeon.player.hand.group) {
      if (predicate.test(c)) {
        fn.accept(c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
      if (predicate.test(c)) {
        fn.accept(c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
      if (predicate.test(c)) {
        fn.accept(c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
      if (predicate.test(c)) {
        fn.accept(c);
      }
    }
    for (AbstractCard c : AbstractDungeon.player.limbo.group) {
      if (predicate.test(c)) {
        fn.accept(c);
      }
    }
  }

  public static AbstractCard applyDirtyModifier(AbstractCard card) {
    final String DIRTY_KEYWORD_ID = WrestlerMod.makeID("Dirty");
    if (!card.hasTag(WrestlerCardTags.DIRTY)) {
      card.tags.add(WrestlerCardTags.DIRTY);
      card.rawDescription =  WrestlerMod.getKeyword(DIRTY_KEYWORD_ID).PROPER_NAME + card.rawDescription;
      card.initializeDescription();
    }
    return  card;
  }
}
