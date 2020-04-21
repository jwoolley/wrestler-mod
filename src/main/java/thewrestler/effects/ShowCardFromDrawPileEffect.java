package thewrestler.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import javafx.util.Pair;

public class ShowCardFromDrawPileEffect extends AbstractGameEffect {
  private static final float CARD_TARGET_X =  Settings.WIDTH / 2.0f;
  private static final float CARD_TARGET_Y = Settings.HEIGHT / 2.0f;

  public static final float EFFECT_DUR = 1.0F;
  private AbstractCard card;

  private final float MIN_DRAW_SCALE =  0.01f;
  private final float MAX_DRAW_SCALE =  1.00f;

  final Pair<Float, Float> spawnLocation;

  public ShowCardFromDrawPileEffect(AbstractCard srcCard) {
    this.card = srcCard.makeStatEquivalentCopy();
    this.duration = EFFECT_DUR;

    this.spawnLocation = getSpawnLocation();

    this.card.current_x = spawnLocation.getKey();
    this.card.current_y = spawnLocation.getValue();
    this.card.drawScale = MIN_DRAW_SCALE;

    this.card.target_x = CARD_TARGET_X;
    this.card.target_y = CARD_TARGET_Y;
    this.card.targetDrawScale = MAX_DRAW_SCALE;
  }

  public void update() {
    this.duration -= Gdx.graphics.getDeltaTime();
    this.card.update();
    if (this.duration < 0.0f) {
      this.isDone = true;
    }
  }

  public void render(SpriteBatch sb) {
    if (!this.isDone) {
      this.card.render(sb);
    }
  }

  public void dispose() {}

  private static Pair<Float, Float> getSpawnLocation() {
    return new Pair<>(Settings.WIDTH * 0.1F, Settings.HEIGHT * 0.1F);
  }
}