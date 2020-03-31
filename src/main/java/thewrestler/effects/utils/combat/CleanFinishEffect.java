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
    private static final String CAMERA_SFX_KEY = "CAMERA_SHUTTER_1";

    private static float ACTION_DURATION = Settings.ACTION_DUR_FAST;

    private static float DEFAULT_FLASH_DURATION = 1.0f;

    private int actionCounter;

    private Logger logger;

    private final String sfxKey;
    private final Color flashColor;
    private final float flashDuration;
    private final boolean renderBehind;

    public CleanFinishEffect() {
      this(WHITE_CAMERA_FLASH, CAMERA_SFX_KEY, ACTION_DURATION, DEFAULT_FLASH_DURATION, false);
    }

  public CleanFinishEffect(Color color, String sfxKey, float duration) {
      this(color, sfxKey, duration, DEFAULT_FLASH_DURATION, false);
  }

    public CleanFinishEffect(Color color, String sfxKey, float duration, float flashDuration, boolean renderBehind) {
      this.duration = duration;
      this.flashDuration = flashDuration;
      this.actionCounter = 24;
      this.flashColor = color.cpy();
      this.sfxKey = sfxKey;
      this.renderBehind = renderBehind;
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

        if (actionCounter == 24) {
          AbstractDungeon.effectsQueue.add(
              new RoomTintEffect(Color.BLACK.cpy(), 0.9F, this.flashDuration, true));
        } else if ((actionCounter == 20 || Settings.FAST_MODE && actionCounter == 22) && sfxKey != null) {
          AbstractDungeon.actionManager.addToTop(new SFXAction(this.sfxKey));
        } else if (actionCounter < 16 || Settings.FAST_MODE && actionCounter == 20) {
          AbstractDungeon.effectsQueue.add(
              new RoomTintEffect(this.flashColor, 0.99F, this.flashDuration, true));
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
