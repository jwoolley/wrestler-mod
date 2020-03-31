package thewrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.characters.WrestlerCharacter;

public class GainPenaltyCardAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  public GainPenaltyCardAction() {
    this.actionType = ActionType.SPECIAL;
    this.source = AbstractDungeon.player;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
  }

  @Override
  public void update() {
    if (WrestlerCharacter.hasPenaltyCardInfo()) {
      WrestlerCharacter.getPenaltyCardInfo().gainPenaltyCard();
    }
    this.isDone = true;
  }
}
