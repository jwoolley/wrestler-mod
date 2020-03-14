package thewrestler.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import thewrestler.WrestlerMod;
import thewrestler.cards.EndOfCombatListener;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.util.BasicUtils;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.CombatInfo;

import java.util.ArrayList;

public class WrestlerCombatInfoPanel implements CustomInfoPanel, StartOfCombatListener, EndOfCombatListener {
  private static final String[] TEXT;

  private static final float WIDTH = 290;
  private static final float HEIGHT = 160;
  private static final float X_OFFSET = 24;
  private static final float Y_OFFSET = 626 + HEIGHT;
  private static final float Y_OFFSET_WIDESCREEN = 626 + HEIGHT;
  private static final float X_TEXT_OFFSET = 10;
  private static final float Y_TEXT_OFFSET_WIDESCREEN = HEIGHT - 20;
  private static final float Y_TEXT_OFFSET =  Y_TEXT_OFFSET_WIDESCREEN + 0;
  private static final float TOOLTIP_X_OFFSET = 16.0F;
  private static final float TOOLTIP_Y_OFFSET = -32.0F;

  private static final String UI_NAME = WrestlerMod.makeID("CombatInfoPanel");
  private static final String BACKGROUND_TEXURE_PATH = UiHelper.getUiImageResourcePath("combatinfopanel/background.png");

  private static final BitmapFont INFO_FONT = FontHelper.tipBodyFont;
  private static final Color INFO_HEADER_COLOR = Color.valueOf("992200ff");
  private static final Color INFO_COLOR = Color.valueOf("e9e9e0cc");
  private final String uiName;
  private final String backgroundImgPath;
  private Texture panelBackgroundImage;
  private final int xOffset;
  private final int yOffset;
  private final int xTextOffset;
  private final int yTextOffset;
  private Hitbox hb;

  private CombatInfo.CardsPlayedCounts cardCounts;
  private boolean isFirstTurn = true;

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

//      WrestlerMod.logger.info("WrestlerCombatInfoPanel::updateCardCounts called. updated counts: "
//          + "attacks:  " + this.cardCounts.attacks
//          + "skills:   " + this.cardCounts.skills
//          + "powers:   " + this.cardCounts.powers
//          + "debuffs:  " + this.cardCounts.debuffs);
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
    final BitmapFont font = INFO_FONT;
    final Color headerColor = INFO_HEADER_COLOR;
    final Color color = INFO_COLOR;

    final int yLineOffset = (int)(INFO_FONT.getLineHeight() * (Settings.isSixteenByTen ? 1.05f : 0.95f));

    FontHelper.renderFontLeft(
        sb,
        font,
        TEXT[0],
        this.xOffset + WIDTH * 0.04f,
        this.yOffset + this.yTextOffset,
        headerColor);

    FontHelper.renderFontLeft(
        sb,
        font,
        TEXT[1] + (this.cardCounts.attacks >= 0 ? this.cardCounts.attacks : ""),
        this.xOffset + this.xTextOffset,
        this.yOffset + this.yTextOffset - (yLineOffset * 1.075f),
        color);

    FontHelper.renderFontLeft(
        sb,
        font,
        TEXT[7] + (this.cardCounts.nonAttacks >= 0 ? this.cardCounts.nonAttacks : ""),
        this.xOffset + this.xTextOffset,
        this.yOffset + this.yTextOffset - (yLineOffset * 2.075f),
        color);
  }

  public boolean shouldRenderPanel() {
    return shouldRender();
  }

  public static boolean shouldRender() {
    return BasicUtils.isPlayingAsWrestler() &&
        (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP || BasicUtils.isPlayerInCombat());
  }

  @Override
  public void atStartOfTurn() {
    this.cardCounts = CombatInfo.RESET_CARDS_PLAYED_COUNTS;

    if (!isFirstTurn) {
      this.cardCounts.dirtyCardsThisCombat = CombatInfo.getCardsPlayedCounts().dirtyCardsThisCombat;
    }
  }

  @Override
  public void atEndOfTurn() {
    this.cardCounts = CombatInfo.RESET_CARDS_PLAYED_COUNTS;
    this.cardCounts.dirtyCardsThisCombat = CombatInfo.getCardsPlayedCounts().dirtyCardsThisCombat;
    isFirstTurn = false;
  }

  @Override
  public void atStartOfCombat() {
    this.cardCounts = CombatInfo.RESET_CARDS_PLAYED_COUNTS;
    this.isFirstTurn = true;
  }

  @Override
  public void onCardUsed(AbstractCard card) {

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
}
