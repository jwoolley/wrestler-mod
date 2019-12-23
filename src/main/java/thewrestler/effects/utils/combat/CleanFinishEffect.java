package thewrestler.effects.utils.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import org.apache.logging.log4j.Logger;

public class CleanFinishEffect extends AbstractGameEffect {
    private static final Color WHITE_CAMERA_FLASH = new Color(0.95f, 0.95f, 1.0f, 0.9f);
    private static float ACTION_DURATION = Settings.ACTION_DUR_FAST;
    private int actionCounter;

    private Logger logger;

    public CleanFinishEffect() {
      this.duration = ACTION_DURATION;
      this.actionCounter = 24;
    }

    @Override
    public void update() {
      if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        AbstractDungeon.actionManager.clearPostCombatActions();
        this.isDone = true;
        return;
      }

      this.duration -= Gdx.graphics.getDeltaTime();

      if (this.duration < 0.0F) {
        if (actionCounter > 0) {
          actionCounter--;
        } else {
          this.isDone = true;
          return;
        }

        if (Settings.FAST_MODE) {
          // do all the stuff quickly
        }

        if (actionCounter == 24) {
          AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.BLACK.cpy(), 0.9F));
        } else if (actionCounter == 20) {
          AbstractDungeon.actionManager.addToTop(new SFXAction("CAMERA_SHUTTER_1"));
        } else if (actionCounter < 16) {
          AbstractDungeon.effectsQueue.add(new RoomTintEffect(WHITE_CAMERA_FLASH, 0.99F));
          AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE.cpy(), false));
          this.isDone = true;
        }
      }
    }

  @Override
  public void render(SpriteBatch spriteBatch) {

  }

  @Override
  public void dispose() {

  }

}
