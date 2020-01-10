package thewrestler.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import thewrestler.WrestlerMod;

@SpirePatch(
    clz = CardGroup.class,
    method = "moveToExhaustPile"
)

public class OnCardExhaustedPatch {
  public static void Postfix(CardGroup __instance, AbstractCard card) {
    WrestlerMod.logger.info("OnCardExhaustedPatch::Postfix called for card: " + card.name);
    WrestlerMod.onExhaustCardHook(card);
  }
}