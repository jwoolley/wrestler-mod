package thewrestler.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import thewrestler.WrestlerMod;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.BasicUtils;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.CombatInfo;

import java.util.ArrayList;

public class WrestlerSignatureMovePanel implements CustomInfoPanel {
  private static final String[] TEXT;

  private static final float WIDTH = 290;
  private static final float HEIGHT = 160;
  private static final float X_OFFSET = 24;
  private static final float Y_OFFSET = 441 + HEIGHT;
  private static final float Y_OFFSET_WIDESCREEN = 207 + HEIGHT;
  private static final float X_TEXT_OFFSET = 10;
  private static final float Y_TEXT_OFFSET =  HEIGHT - 20;
  private static final float Y_TEXT_OFFSET_WIDESCREEN = Y_TEXT_OFFSET + 0;
  private static final float TOOLTIP_X_OFFSET = 16.0F;
  private static final float TOOLTIP_Y_OFFSET = -32.0F;

  private static final String UI_NAME = WrestlerMod.makeID("SignatureMovePanel");
  private static final String BACKGROUND_TEXURE_PATH = UiHelper.getUiImageResourcePath("signaturemovepanel/background.png");

  private static final BitmapFont INFO_HEADER_FONT = FontHelper.charDescFont;
  private static final BitmapFont INFO_FONT = FontHelper.tipBodyFont;
  private static final Color INFO_HEADER_COLOR = Color.valueOf("992200ff");
  private static final Color INFO_COLOR = Color.valueOf("e9e9e0cc");
  private final String uiName;
  private final String backgroundImgPath;
  private Texture panelBackgroundImage;
  private final int width;
  private final int height;
  private final int xOffset;
  private final int yOffset;
  private final int xTextOffset;
  private final int yTextOffset;
  private Hitbox hb;

  private CombatInfo.CardsPlayedCounts cardCounts;

  // TODO: define imgName as static named BACKGROUND_IMAGE_PATH;
  // TODO: for SignatureMoveInfoPanel, take uiName argument and load labels from there
  public WrestlerSignatureMovePanel() {
    this.uiName = UI_NAME;
    this.backgroundImgPath = BACKGROUND_TEXURE_PATH;

    this.width = Math.round(WIDTH * SettingsHelper.getScaleX());
    this.height = Math.round(HEIGHT * SettingsHelper.getScaleY());

    this.xOffset = Math.round(X_OFFSET * SettingsHelper.getScaleX());
    this.xTextOffset = Math.round(X_TEXT_OFFSET * SettingsHelper.getScaleX());

    this.yOffset =  Math.round(
        (Settings.HEIGHT - (Settings.isSixteenByTen ? Y_OFFSET : Y_OFFSET_WIDESCREEN) * SettingsHelper.getScaleY()));
    this.yTextOffset =  Math.round(
        (Settings.isSixteenByTen ? Y_TEXT_OFFSET : Y_TEXT_OFFSET_WIDESCREEN) * SettingsHelper.getScaleY());

    this.hb = new Hitbox(this.width, this.height);
    hb.translate(xOffset, yOffset);
    this.cardCounts = CombatInfo.UNINITIALIZED_CARDS_PLAYED_COUNTS;
  }

  boolean updateCardCountsFlag = false;
  public void updateCardCounts() {
    updateCardCountsFlag = true;
  }

  @Override
  public void update() {
    if (updateCardCountsFlag) {
      this.cardCounts = CombatInfo.getCardsPlayedCounts();

      WrestlerMod.logger.info("WrestlerCombatInfoPanel::updateCardCounts called. updated counts: "
          + "attacks:  " + this.cardCounts.attacks
          + "skills:   " + this.cardCounts.skills
          + "powers:   " + this.cardCounts.powers);
      updateCardCountsFlag = false;
    }
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

      renderInfoText(sb);

      hb.render(sb);
    }
  }

  private void renderInfoText(SpriteBatch sb) {
    final BitmapFont headerFont = INFO_HEADER_FONT;
    final BitmapFont font = INFO_FONT;
    final Color headerColor = INFO_HEADER_COLOR;
    final Color color = INFO_COLOR;

    final int yLineOffset = (int)(INFO_FONT.getLineHeight() * (Settings.isSixteenByTen ? 1.05f : 0.95f));

    FontHelper.renderFontLeft(
        sb,
        headerFont,
        TEXT[0],
        this.xOffset + this.width * 0.04f,
        this.yOffset + this.yTextOffset,
        headerColor);

    final String infoMessage = WrestlerCharacter.getSignatureMoveInfo().getConditionText();
    GlyphLayout layout = new GlyphLayout(font, WrestlerCharacter.getSignatureMoveInfo().getConditionText(), color,
        this.width -  this.xTextOffset, Align.left, true);

    font.setColor(color);
    font.draw(sb, infoMessage,
        this.xOffset + this.xTextOffset,
        (this.yOffset + this.yTextOffset) - (yLineOffset * 1.0f),
        layout.width, Align.left, true);
  }

  public boolean shouldRenderPanel() {
    return shouldRender();
  }

  public static boolean shouldRender() {
    return BasicUtils.isPlayingAsWrestler() && BasicUtils.isPlayerInCombat()
        && WrestlerCharacter.getSignatureMoveInfo() != null;
  }

  @Override
  public void atStartOfTurn() {
    this.cardCounts = CombatInfo.RESET_CARDS_PLAYED_COUNTS;
  }

  @Override
  public void atEndOfTurn() {
    this.cardCounts = CombatInfo.RESET_CARDS_PLAYED_COUNTS;
  }

  @Override
  public void atStartOfCombat() {
    this.cardCounts = CombatInfo.RESET_CARDS_PLAYED_COUNTS;
  }

  @Override
  public void atEndOfCombat() {
    this.cardCounts = CombatInfo.UNINITIALIZED_CARDS_PLAYED_COUNTS;
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

  public void onCardUsed() {
    // TODO: call SignatureMoveInfo.onCardUsed
  }
}