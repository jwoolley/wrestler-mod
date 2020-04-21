package thewrestler.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ShowCardEffect extends AbstractGameEffect {
  private static final float CARD_TARGET_X = Settings.WIDTH / 2.0f;
  private static final float CARD_TARGET_Y = Settings.HEIGHT / 2.0f;

  public static final float EFFECT_DUR = 0.75F;
  private AbstractCard card;

  public ShowCardEffect(AbstractCard srcCard) {
    this.card = srcCard.makeStatEquivalentCopy();
    this.duration = EFFECT_DUR;

    this.card.current_x = CARD_TARGET_X;
    this.card.current_y = CARD_TARGET_Y;

    this.card = srcCard.makeStatEquivalentCopy();
  }

  public void update() {
    this.duration -= Gdx.graphics.getDeltaTime();
    this.card.update();
    this.card.superFlash();
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
}