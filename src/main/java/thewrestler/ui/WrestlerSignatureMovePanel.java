package thewrestler.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import thewrestler.WrestlerMod;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.moveinfos.AbstractSignatureMoveInfo;
import thewrestler.util.BasicUtils;
import thewrestler.util.TextureLoader;

import java.util.ArrayList;

// TODO: subheader w/ Signature Move name (in yellow)

public class WrestlerSignatureMovePanel implements CustomInfoPanel, CardPreviewElement {
  private static final String[] TEXT;

  private static final float WIDTH = 290;
  private static final float HEIGHT = 185;
  private static final float X_OFFSET = 24;
  private static final float Y_OFFSET = 441 + HEIGHT;
  private static final float Y_OFFSET_WIDESCREEN = 207 + HEIGHT;
  private static final float X_TEXT_OFFSET = 10;
  private static final float Y_TEXT_OFFSET =  HEIGHT - 20;
  private static final float Y_TEXT_OFFSET_WIDESCREEN = Y_TEXT_OFFSET + 0;
  private static final float TOOLTIP_X_OFFSET = 108.0F;
  private static final float TOOLTIP_Y_OFFSET = -8.0F;

  private static final String UI_NAME = WrestlerMod.makeID("SignatureMovePanel");
  private static final String BACKGROUND_TEXURE_PATH = UiHelper.getUiImageResourcePath("signaturemovepanel/background-2.png");

  private static final BitmapFont INFO_HEADER_FONT = FontHelper.charDescFont;
  private static final BitmapFont CARD_NAME_FONT = FontHelper.healthInfoFont;
  private static final Color CARD_NAME_COLOR = Color.GOLDENROD.cpy();
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

  // TODO: show preview of signature move card
  // TODO: smarter text/tooltips to explain what happens
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
  }

  @Override
  public void update() {
    this.hb.update();
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

      if (this.hb.hovered) {
        renderPreviewCardTip(sb);
      }
      // printPreviewTipDebugLog();
    }
  }

  static int debugFrame = 0;
  static final int FRAMES_PER_DEBUG_LOG = 24;
  void printPreviewTipDebugLog() {
    if (debugFrame == 0) {
      WrestlerMod.logger.info("WrestlerSignatureMovePanel::render"
          + "\n\thb.x: " + this.hb.x
          + "\n\thb.width: " + this.hb.width
          + "\n\thb.xRightEdge: " + (this.hb.x + this.hb.width)
          + "\n\tInputHelper.mX: " + InputHelper.mX
          + "\n\thb.y: " + this.hb.y
          + "\n\thb.height: " + this.hb.height
          + "\n\thb.yTopEdge: " + (this.hb.y + this.hb.height)
          + "\n\tInputHelper.mY: " + InputHelper.mY
          + "\n\thb.hovered: " + this.hb.hovered
      );
    } else {
      debugFrame++;
      if (debugFrame >= FRAMES_PER_DEBUG_LOG) {
        debugFrame = 0;
      }
    }
  }

  private void renderInfoText(SpriteBatch sb) {
    final BitmapFont headerFont = INFO_HEADER_FONT;
    final BitmapFont cardNameFont = CARD_NAME_FONT;
    final BitmapFont font = INFO_FONT;
    final Color headerColor = INFO_HEADER_COLOR;
    final Color cardNameColor = CARD_NAME_COLOR;
    final Color color = INFO_COLOR;

    final int yLineOffset = (int)(INFO_FONT.getLineHeight() * (Settings.isSixteenByTen ? 1.05f : 0.95f));

    FontHelper.renderFontLeft(
        sb,
        headerFont,
        TEXT[0],
        this.xOffset + this.width * 0.04f,
        this.yOffset + this.yTextOffset,
        headerColor);

    FontHelper.renderFontLeft(
        sb,
        cardNameFont,
        getPreviewCard().name,
        this.xOffset + this.width * 0.04f,
        this.yOffset + this.yTextOffset - (yLineOffset * 1.0f),
        cardNameColor);

    final String conditionText = getMoveGainConditionText();

    GlyphLayout layout = new GlyphLayout(font, conditionText, color,this.width -  this.xTextOffset,
        Align.left, true);

    font.setColor(color);
    font.draw(sb, conditionText,
        this.xOffset + this.xTextOffset,
        (this.yOffset + this.yTextOffset) - (yLineOffset * 2.0f),
        layout.width, Align.left, true);
  }

  // if not in combat, show static condition description;
  // if in combat and signature move card can still be gained, show the dynamic description;
  // if in combat and signature move card can no longer be gained, show the "already gained" text
  private String getMoveGainConditionText() {
    final AbstractSignatureMoveInfo moveInfo = WrestlerCharacter.getSignatureMoveInfo();
    return !BasicUtils.isPlayerInCombat()
        ? getStaticConditionText()
        : ( moveInfo.canStillTriggerCardGain() ? getDynamicConditionText() : TEXT[3]);
  }

  public boolean shouldRenderPanel() {
    return shouldRender();
  }

  public static boolean shouldRender() {
    return BasicUtils.isPlayingAsWrestler() && BasicUtils.isPlayerInCombat()
        && WrestlerCharacter.getSignatureMoveInfo() != null;
  }

  @Override
  public void atStartOfTurn() {}

  @Override
  public void atEndOfTurn() {}

  @Override
  public void atStartOfCombat() {}

  @Override
  public void atEndOfCombat() {}

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

  @Override
  public void onCardUsed(AbstractCard card) {
    // TODO: call SignatureMoveInfo.onCardUsed
    getMoveInfo().onCardPlayed(card);
  }

  @Override
  public AbstractSignatureMoveCard getPreviewCard() {
    return getMoveInfo().getSignatureMoveCard();
  }

  @Override
  public void renderPreviewCardTip(SpriteBatch sb) {
    if (shouldRenderPreviewCard()) {
      final AbstractCard _previewCard = getPreviewCard();
      if (_previewCard != null) {
        _previewCard.current_x = _previewCard.hb.x = this.hb.x + this.hb.width + getPreviewXOffset();
        _previewCard.current_y = _previewCard.hb.y = this.hb.y + this.hb.height + getPreviewYOffset();
        _previewCard.render(sb);
      }
    }
  }

  @Override
  public float getPreviewXOffset() {
    return TOOLTIP_X_OFFSET;
  }

  @Override
  public float getPreviewYOffset() {
    return TOOLTIP_Y_OFFSET;
  }

  @Override
  public boolean shouldRenderPreviewCard() {
    return true;
  }

  private static String getStaticConditionText() {
    return getMoveInfo().getStaticConditionText() + TEXT[1]
        + getMoveInfo().getSignatureMoveCard().getIndefiniteCardName() + TEXT[2];
  }

  private static String getDynamicConditionText() {
    return getMoveInfo().getDynamicConditionText() + TEXT[1]
        + getMoveInfo().getSignatureMoveCard().getIndefiniteCardName()+ TEXT[2];
  }

  private static AbstractSignatureMoveInfo getMoveInfo() {
    return WrestlerCharacter.getSignatureMoveInfo();
  }
}