package thewrestler.actions.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.util.CardUtil;

import java.util.List;
import java.util.stream.Collectors;

public class RopewalkAction extends AbstractGameAction  {
  private static final float DURATION = Settings.ACTION_DUR_XFAST;
  private boolean forEntireCombat;

  public RopewalkAction(boolean forEntireCombat) {
    final AbstractPlayer player = AbstractDungeon.player;
    setValues(player, player);
    this.actionType = ActionType.CARD_MANIPULATION;
    this.duration = DURATION;
    this.forEntireCombat = forEntireCombat;
  }

  public void update() {
    if (this.duration == DURATION) {
      List<AbstractCard> attackCards = AbstractDungeon.player.hand.group.stream()
          .filter(c -> c.type == AbstractCard.CardType.ATTACK && c.costForTurn > 0).collect(Collectors.toList());

      if (!attackCards.isEmpty()) {
        CardUtil.discountRandomCardCost(attackCards, 1, this.forEntireCombat);
      }

      this.isDone = true;
      tickDuration();
      return;
    }
    this.tickDuration();
  }
}