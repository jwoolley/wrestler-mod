package thewrestler.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface CustomInfoPanel {
  void update();
  void render(SpriteBatch sb);
  boolean shouldRenderPanel();
  void atStartOfTurn();
  void atEndOfTurn();
  void atStartOfCombat();
  void onCardUsed(AbstractCard card);
  void atEndOfCombat();
}
