package thewrestler.screens.trademarkmove.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
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

        if (canComboCards(card)) {
          AbstractDungeon.actionManager.addToBottom(
              new ChooseTrademarkMoveAction((AbstractPenaltyStatusCard) card, penaltyCardsInHand));
          return SpireReturn.Return(null);
        }
      }
      return SpireReturn.Continue();
    }
  }

  private static boolean canComboCards(AbstractCard card) {
    List<AbstractPenaltyStatusCard> penaltyCardsInHand = AbstractDungeon.player.hand.group.stream()
        .filter(c -> c instanceof  AbstractPenaltyStatusCard)
        .map(c -> (AbstractPenaltyStatusCard)c)
        .collect(Collectors.toList());

      final int currentEnergy = EnergyPanel.getCurrentEnergy();

      return penaltyCardsInHand.size() > 1 && penaltyCardsInHand.stream()
          .anyMatch(c -> c.uuid != card.uuid && c.freeToPlayOnce || card.cost + c.cost <= currentEnergy);
  }
}