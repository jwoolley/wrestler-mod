package thewrestler.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface CustomInfoPanel {
  void update();
  void render(SpriteBatch sb);
  boolean shouldRenderPanel();
  void atStartOfTurn();
  void atEndOfTurn();
  void atStartOfCombat();
  void atEndOfCombat();
}
