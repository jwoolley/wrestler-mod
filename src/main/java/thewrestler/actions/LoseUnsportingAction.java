package thewrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.characters.WrestlerCharacter;

// TODO: this doesn't *do* anything now. should it?

public class LoseUnsportingAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  public LoseUnsportingAction(int amount) {
    this.actionType = ActionType.SPECIAL;
    this.source = AbstractDungeon.player;
    this.amount = amount;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
  }

  @Override
  public void update() {
    if (this.duration <= ACTION_DURATION) {
      if (WrestlerCharacter.hasPenaltyCardInfo()) {
      }
      this.isDone = true;
    }
    tickDuration();
  }
}
