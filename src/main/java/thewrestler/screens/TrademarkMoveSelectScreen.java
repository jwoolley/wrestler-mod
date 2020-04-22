package thewrestler.screens;

import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import org.apache.logging.log4j.Logger;
import thewrestler.WrestlerMod;

public class TrademarkMoveSelectScreen {
  private boolean isOpen = false;

  public TrademarkMoveSelectScreen() {

  }

  public void reset() {
    isOpen = false;
  }

  public void open() {
    this.isOpen  = true;
    // show buttons

    // from GridCardSelectScreen
    // hide buttons? peek/confirm


    AbstractDungeon.isScreenUp = true;
    AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
    AbstractDungeon.overlayMenu.showBlackScreen(0.75f);

    AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
  }

  private void close() {
    AbstractDungeon.overlayMenu.hideBlackScreen();
    AbstractDungeon.isScreenUp = false;
    this.isOpen = false;
  }

  public boolean isOpen() {
    return isOpen;
  }

  static int renderCounter = 0;
  public void render(SpriteBatch sb) {
    if (this.isOpen) {
      if (renderCounter >= 64) {
        WrestlerMod.logger.info(this.getClass().getSimpleName() + "::render called");
        renderCounter = 0;
      } else {
        renderCounter++;
      }
    }
  }

  static int updateCounter = 0;
  public void update() {
    if (this.isOpen) {
      if (updateCounter >= 64) {
        WrestlerMod.logger.info(this.getClass().getSimpleName() + "::update called");
        updateCounter = 0;
      } else {
        updateCounter++;
      }

      if (AbstractDungeon.overlayMenu.cancelButton.hb.clickStarted) {
        AbstractDungeon.closeCurrentScreen();
        this.close();
      }
    }
  }
}