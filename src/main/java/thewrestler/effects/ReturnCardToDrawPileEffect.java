package thewrestler.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import javafx.util.Pair;

public class ReturnCardToDrawPileEffect extends AbstractGameEffect {
  public static final float EFFECT_DUR = 1.0F;
  private AbstractCard card;
  private final float MIN_DRAW_SCALE =  0.01f;

  public ReturnCardToDrawPileEffect(AbstractCard srcCard) {
    this.card = srcCard.makeStatEquivalentCopy();
    this.duration = EFFECT_DUR;

    final Pair<Float, Float> targetLocation = getTargetLocation();

    this.card.current_x = Settings.WIDTH / 2.0f;
    this.card.current_y = Settings.HEIGHT / 2.0f;
    this.card.drawScale = 1.0f;

    this.card.target_x = targetLocation.getKey();
    this.card.target_y = targetLocation.getValue();
    this.card.targetDrawScale = MIN_DRAW_SCALE;
  }

  public void update() {
    this.duration -= Gdx.graphics.getDeltaTime();
    this.card.update();
    if (this.duration < 0.0f) {
      AbstractDungeon.getCurrRoom().souls.onToDeck(this.card, true, true);
      this.isDone = true;
    }
  }

  public void render(SpriteBatch sb) {
    if (!this.isDone) {
      this.card.render(sb);
    }
  }

  public void dispose() {}

  private static Pair<Float, Float> getTargetLocation() {
    return new Pair<>(Settings.WIDTH * 0.05F, Settings.HEIGHT * 0.05F);
  }
}