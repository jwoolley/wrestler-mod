package thewrestler.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import thewrestler.WrestlerMod;
import thewrestler.util.BasicUtils;
import thewrestler.util.TextureLoader;

import java.util.ArrayList;

public class WrestlerCombatInfoPanel implements CustomInfoPanel {
  private static final String[] TEXT;

  private static final float WIDTH = 290;
  private static final float HEIGHT = 194;
  private static final float X_OFFSET = 24;
  private static final float Y_OFFSET = 191 + HEIGHT;
  private static final float Y_OFFSET_WIDESCREEN = 207;
  private static final float X_TEXT_OFFSET = 10;
  private static final float Y_TEXT_OFFSET = 28 + HEIGHT;
  private static final float Y_TEXT_OFFSET_WIDESCREEN = Y_TEXT_OFFSET;
  private static final float TOOLTIP_X_OFFSET = 16.0F;
  private static final float TOOLTIP_Y_OFFSET = -32.0F;

  private static final String UI_NAME = WrestlerMod.makeID("CombatInfoPanel");
  private static final String BACKGROUND_TEXURE_PATH = UiHelper.getUiImageResourcePath("combatinfopanel/background.png");

  private static final BitmapFont LABEL_FONT = FontHelper.panelNameFont;
  private static final Color LABEL_COLOR = Color.valueOf("f0d26cCff");
  private static final boolean IS_CLICKABLE = false;
  private final String uiName;
  private final String backgroundImgPath;
  private Texture panelBackgroundImage;
  private final int xOffset;
  private final int yOffset;
  private final int xTextOffset;
  private final int yTextOffset;
  private Hitbox hb;

  // TODO: define imgName as static named BACKGROUND_IMAGE_PATH;
  // TODO: for SignatureMoveInfoPanel, take uiName argument and load labels from there
  public WrestlerCombatInfoPanel() {
    this.uiName = UI_NAME;
    this.backgroundImgPath = BACKGROUND_TEXURE_PATH;

    this.xOffset = Math.round(X_OFFSET * SettingsHelper.getScaleX());
    this.xTextOffset = Math.round(X_TEXT_OFFSET * SettingsHelper.getScaleX());

    this.yOffset =  Math.round(
        (Settings.HEIGHT - (Settings.isSixteenByTen ? Y_OFFSET : Y_OFFSET_WIDESCREEN) * SettingsHelper.getScaleY()));
    this.yTextOffset =  Math.round(
        (Settings.isSixteenByTen ? Y_TEXT_OFFSET : Y_TEXT_OFFSET_WIDESCREEN) * SettingsHelper.getScaleY());

    this.hb = new Hitbox(WIDTH * SettingsHelper.getScaleX(), HEIGHT * SettingsHelper.getScaleY());
    hb.translate(xOffset, yOffset);
  }

  @Override
  public void update() {

  }

  @Override
  public void render(SpriteBatch sb) {
    if (shouldRender()) {

      if (this.hb.hovered) {
        TipHelper.queuePowerTips(
            hb.x + TOOLTIP_X_OFFSET * SettingsHelper.getScaleX(),
            hb.y - TOOLTIP_Y_OFFSET * SettingsHelper.getScaleY(), getPowerTips());
      }

      Texture backgroundImage = getPanelBackgroundImage();
      sb.draw(backgroundImage,
          this.xOffset, this.yOffset,
          backgroundImage.getWidth() * SettingsHelper.getScaleX(),  backgroundImage.getHeight() * SettingsHelper.getScaleY(),
          0, 0,
          backgroundImage.getWidth(),  backgroundImage.getHeight(),
          false, false);

      FontHelper.renderFontLeft(
          sb,
          LABEL_FONT,
          this.getInfoText(),
          this.xOffset + this.xTextOffset,
          this.yOffset + this.yTextOffset,
          LABEL_COLOR);

      hb.render(sb);
    }
  }

  public boolean shouldRenderPanel() {
    return shouldRender();
  }

  public static boolean shouldRender() {
    return BasicUtils.isPlayingAsWrestler() &&  BasicUtils.isPlayerInCombat();
  }

  @Override
  public void atStartOfTurn() {
    // reset values
  }

  @Override
  public void atStartOfCombat() {
    // reset values
  }

  @Override
  public void atEndOfCombat() {
    // reset values
  }

  private ArrayList<PowerTip> getPowerTips() {
    return new ArrayList<>();
  }

  // TODO: this probably needs to do some more sophisticated text layout work
  private String getInfoText() {
    return "Info goes here";
  }

  private Texture getPanelBackgroundImage() {
    if (panelBackgroundImage == null) {
      panelBackgroundImage = TextureLoader.getTexture(this.backgroundImgPath);
    }
    return panelBackgroundImage;
  }

  static {
    UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_NAME);
    TEXT = uiStrings.TEXT;
  }
}
