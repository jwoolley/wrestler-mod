package thewrestler.screens.trademarkmove.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.screens.trademarkmove.actions.ChooseTrademarkMoveAction;

import java.util.List;
import java.util.stream.Collectors;

public class UsePenaltyCardPatch {
  @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
  public static class UsePenaltyCardPrefixPatch {
    @SpirePrefixPatch
    public static SpireReturn Prefix(AbstractPlayer __instance, AbstractCard card, AbstractMonster monster, int energyOnUse) {
      if (card instanceof AbstractPenaltyStatusCard && !((AbstractPenaltyStatusCard)card).triggeredFromSelectScreen) {
        List<AbstractPenaltyStatusCard> penaltyCardsInHand = AbstractDungeon.player.hand.group.stream()
          .filter(c -> c instanceof  AbstractPenaltyStatusCard)
          .map(c -> (AbstractPenaltyStatusCard)c)
          .collect(Collectors.toList());

        if (penaltyCardsInHand.size() > 1) {
          AbstractDungeon.actionManager.addToBottom(
              new ChooseTrademarkMoveAction((AbstractPenaltyStatusCard) card, penaltyCardsInHand));
          return SpireReturn.Return(null);
        }
      }
      return SpireReturn.Continue();
    }
  }
}