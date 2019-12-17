package thewrestler.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GainGoldAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  public GainGoldAction(int goldAmount) {
    this.actionType = ActionType.SPECIAL;
    this.source = AbstractDungeon.player;
    this.amount = goldAmount;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
  }

  @Override
  public void update() {
    AbstractDungeon.player.gainGold(this.amount);
    for (int i = 0; i < this.amount; i++) {
      if (MathUtils.randomBoolean()) {
        CardCrawlGame.sound.play("GOLD_GAIN", 0.1F);
      }
    }
    this.isDone = true;
  }
}
