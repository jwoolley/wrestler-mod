package thewrestler.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class ReduceBlockAction extends AbstractGameAction {
    private static final float ACTION_DURATION = 0.25F;

    public ReduceBlockAction(AbstractCreature target, AbstractCreature source, int amount) {
      setValues(target, source, amount);
      this.actionType = AbstractGameAction.ActionType.BLOCK;
      this.duration = ACTION_DURATION;
    }

    public void update() {
      if ((!this.target.isDying) && (!this.target.isDead) && (this.duration == ACTION_DURATION)
          && (this.target.currentBlock > 0)) {
        if (this.amount >= target.currentBlock) {
          this.target.loseBlock();
        } else {
          this.target.loseBlock(this.amount);
        }
        this.isDone = true;
      }
      tickDuration();
    }
  }
