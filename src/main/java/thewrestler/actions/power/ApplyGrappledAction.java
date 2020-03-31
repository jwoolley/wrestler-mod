package thewrestler.actions.power;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thewrestler.WrestlerMod;
import thewrestler.powers.GrappledPower;

public class ApplyGrappledAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  private final boolean useFastMode;

  private static int getHpThreshold(AbstractCreature grappledSource) {
      // return (int) Math.floor(grappledSource.maxHealth * GrappledPower.MAX_HP_PCT/100.0f);
      return GrappledPower.HP_THRESHOLD;
  }

  public ApplyGrappledAction(AbstractCreature target, AbstractCreature source) {
    this(target, source, getHpThreshold(source),false);
  }

  private ApplyGrappledAction(AbstractCreature target, AbstractCreature source, int amount, boolean useFastMode) {
    this.actionType = ActionType.DEBUFF;
    this.target = target;
    this.source = source;
    this.amount = amount;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
    this.useFastMode = useFastMode;
  }

  @Override
  public void update() {
    Logger logger = WrestlerMod.logger;
    if (this.duration == this.startDuration) {
      logger.debug("ApplyGrappledAction.update :: Applying Grapple with HP threshold: " +  this.amount);

      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(
              this.target, this.source, new GrappledPower(this.target, this.source, this.amount), this.amount,
              this.useFastMode));

      this.tickDuration();
      this.isDone = true;
    }
    this.tickDuration();
  }
}
