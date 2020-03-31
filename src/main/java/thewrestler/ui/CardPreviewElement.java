package thewrestler.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface CardPreviewElement {
  enum MultiLock {
    ZERO, ONE, TWO
  }

  AbstractCard getPreviewCard();
  void renderPreviewCardTip(SpriteBatch sb);
  float getPreviewXOffset();
  float getPreviewYOffset();
  boolean shouldRenderPreviewCard();
}
