package thewrestler.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.characters.WrestlerCharacter;

public class GainApprovalAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  public GainApprovalAction(int amount) {
    this.actionType = ActionType.SPECIAL;
    this.source = AbstractDungeon.player;
    this.amount = amount;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
  }

  @Override
  public void update() {
    if (this.duration <= ACTION_DURATION) {
      if (WrestlerCharacter.hasApprovalInfo()) {
        WrestlerCharacter.getApprovalInfo().increaseApproval(this.amount, false);
      }
      this.isDone = true;
    }
    tickDuration();
  }
}
