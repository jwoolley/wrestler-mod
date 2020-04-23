package thewrestler.screens.trademarkmove;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.function.Consumer;

public class TrademarkMoveConfirmButton {
//  private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CardRewardScreen");
//  public static final String[] TEXT = uiStrings.TEXT;
  private static final float WIDTH = 512.0f;
  private static final float HEIGHT = 256.0f;
  public static final float TAKE_Y = Settings.HEIGHT / 2.0F - 340.0F * Settings.scale;
  private boolean isHidden = true;
  private Color textColor = Color.WHITE.cpy();
  private Color btnColor = Color.WHITE.cpy();
  public boolean screenDisabled = false;
  private static final float HITBOX_W = 260.0F * Settings.scale;
  private static final float HITBOX_H = 80.0F * Settings.scale;
  public Hitbox hb = new Hitbox(0.0F, 0.0F, HITBOX_W, HITBOX_H);
  private float controllerImgTextWidth = 0.0F;

  private final String label;
  private final Consumer<AbstractCard> onClickCallback;
  public TrademarkMoveConfirmButton(String label, Consumer<AbstractCard> onClickCallback) {
    this.hb.move(Settings.WIDTH / 2.0F, TAKE_Y);
    this.label = label;
    this.onClickCallback = onClickCallback;
  }

  protected void onClick() {
    this.onClickCallback.accept(null);
  }

  public void update() {
    if (this.isHidden) {
      return;
    }
    this.hb.update();
    if (this.hb.justHovered) {
      CardCrawlGame.sound.play("UI_HOVER");
    }
    if ((this.hb.hovered) && (InputHelper.justClickedLeft)) {
      this.hb.clickStarted = true;
      CardCrawlGame.sound.play("UI_CLICK_1");
    }
    if ((this.hb.clicked || InputActionSet.cancel.isJustPressed() || CInputActionSet.cancel.isJustPressed()) && !this.screenDisabled) {
      this.hb.clicked = false;
      this.onClick();
    }
    this.screenDisabled = false;
    this.textColor.a = MathHelper.fadeLerpSnap(this.textColor.a, 1.0F);
    this.btnColor.a = this.textColor.a;
  }

  public void hide() {
    this.isHidden = true;
  }

  public void show() {
    this.isHidden = false;
    this.textColor.a = 0.0F;
    this.btnColor.a = 0.0F;
  }

  public void render(SpriteBatch sb) {
    if (this.isHidden) {
      return;
    }
    renderButton(sb);
    if (FontHelper.getSmartWidth(FontHelper.buttonLabelFont, label, 9999.0F, 0.0F) > 200.0F * Settings.scale) {
      FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, label, this.hb.cX, TAKE_Y, this.textColor, 0.8F);
    } else {
      FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, label, this.hb.cX, TAKE_Y, this.textColor);
    }
  }

  private void renderButton(SpriteBatch sb) {
    sb.setColor(this.btnColor);
    sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, this.hb.cX - WIDTH * 0.5f, TAKE_Y - HEIGHT * 0.5f, HEIGHT, HEIGHT * 0.5f, WIDTH, HEIGHT, Settings.scale, Settings.scale, 0.0F, 0, 0, (int)WIDTH, (int)HEIGHT, false, false);
    if (this.hb.hovered && !this.hb.clickStarted) {
      sb.setBlendFunction(770, 1);
      sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.3F));
      sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, this.hb.cX - WIDTH * 0.5f, TAKE_Y - HEIGHT * 0.5f, HEIGHT, HEIGHT * 0.5f, WIDTH, HEIGHT, Settings.scale, Settings.scale, 0.0F, 0, 0, (int)WIDTH, (int)HEIGHT, false, false);

      sb.setBlendFunction(770, 771);
    }
    if (Settings.isControllerMode) {
      if (this.controllerImgTextWidth == 0.0F) {
        this.controllerImgTextWidth = (FontHelper.getSmartWidth(FontHelper.buttonLabelFont, label, 99999.0F, 0.0F) / 2.0F);
      }
      sb.setColor(Color.WHITE);
      sb.draw(CInputActionSet.cancel
          .getKeyImg(), this.hb.cX - 32.0F - this.controllerImgTextWidth - 38.0F * Settings.scale, TAKE_Y - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
    }
    this.hb.render(sb);
  }
}
