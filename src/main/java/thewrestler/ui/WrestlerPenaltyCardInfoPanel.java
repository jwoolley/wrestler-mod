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
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.UIStrings;
import thewrestler.WrestlerMod;
import thewrestler.cards.EndOfCombatListener;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.util.BasicUtils;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO: VFX/SFX when sportsmanship changes (e.g. red or green border flash)

public class WrestlerPenaltyCardInfoPanel implements CustomInfoPanel, StartOfCombatListener, EndOfCombatListener {
  private static final String[] TEXT;

  private static final float WIDTH = 290;
  private static final float HEIGHT = 104;
  private static final float X_OFFSET = 24;
  private static final float Y_OFFSET = 786 + HEIGHT;
  private static final float Y_OFFSET_WIDESCREEN = 786 + HEIGHT;
  private static final float Y_TEXT_OFFSET_WIDESCREEN = HEIGHT - 20;
  private static final float Y_TEXT_OFFSET = Y_TEXT_OFFSET_WIDESCREEN + 0;
  private static final float TOOLTIP_X_OFFSET = WIDTH + 16.0F;
  private static final float TOOLTIP_Y_OFFSET = -(HEIGHT + 180.0f);

  private static final String UI_NAME = WrestlerMod.makeID("PenaltyCardInfoPanel");

  private static final String BACKGROUND_IMG_DIR_PATH = "penaltycardinfopanel/";
  private static final List<String> BACKGROUND_IMG_FILENAMES = Arrays.asList(
    "background-zero-cards.png", "background-one-card.png", "background-two-cards.png", "background-three-cards.png");
  private static List<String> BACKGROUND_IMG_FILEPATHS = BACKGROUND_IMG_FILENAMES.stream()
      .map(f -> UiHelper.getUiImageResourcePath(BACKGROUND_IMG_DIR_PATH + f)).collect(Collectors.toList());

  private static final BitmapFont INFO_HEADER_FONT = FontHelper.charDescFont;
  private static final BitmapFont INFO_FONT = FontHelper.losePowerFont;
  private static final Color INFO_HEADER_COLOR = Color.valueOf("992200ff");

  private static final Color NEGATIVE_UNSPORTING_COLOR = Settings.GREEN_TEXT_COLOR.cpy();
  private static final Color NEUTRAL_UNSPORTING_COLOR = Color.WHITE.cpy();
  private static final Color POSITIVE_UNSPORTING_COLOR = Settings.RED_TEXT_COLOR.cpy();

  private static final List<String> keywordList = Arrays.asList(
      CustomTooltipKeywords.PENALTY_CARD, CustomTooltipKeywords.SPORTSMANSHIP);

  private static final List<Keyword> baseGameKeywordList = new ArrayList<>();
  private ArrayList<PowerTip> keywordPowerTips;

  private final String uiName;
  private Texture[] panelBackgroundImage = new Texture[BACKGROUND_IMG_FILEPATHS.size()];
  private final int xOffset;
  private final int yOffset;
  private final int yTextOffset;
  private Hitbox hb;

  private boolean updateUnsportingValueFlag = false;
  private int unsportingValue;

  public WrestlerPenaltyCardInfoPanel() {
    this.uiName = UI_NAME;

    this.xOffset = Math.round(X_OFFSET * SettingsHelper.getScaleX());

    this.yOffset =  Math.round(
        (Settings.HEIGHT - (Settings.isSixteenByTen ? Y_OFFSET : Y_OFFSET_WIDESCREEN) * SettingsHelper.getScaleY()));
    this.yTextOffset =  Math.round(
        (Settings.isSixteenByTen ? Y_TEXT_OFFSET : Y_TEXT_OFFSET_WIDESCREEN) * SettingsHelper.getScaleY());

    this.hb = new Hitbox(WIDTH * SettingsHelper.getScaleX(), HEIGHT * SettingsHelper.getScaleY());
    hb.translate(xOffset, yOffset);
    this.unsportingValue = 0;
  }

  @Override
  public void update() {
    this.hb.update();
    if (updateUnsportingValueFlag) {
      this.unsportingValue = WrestlerCharacter.getSportsmanshipInfo().getUnsportingAmount();
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

      Texture backgroundImage = getPanelBackgroundImage(this.unsportingValue);
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
    final Color color = this.unsportingValue > 0
        ? POSITIVE_UNSPORTING_COLOR
        : (this.unsportingValue < 0 ? NEGATIVE_UNSPORTING_COLOR : NEUTRAL_UNSPORTING_COLOR);

    final int yLineOffset = (int)(INFO_FONT.getLineHeight() * (Settings.isSixteenByTen ? 1.05f : 0.95f));

    final String separatorString = "/";

    final double amountTextWidth = INFO_FONT.getSpaceWidth() * 0.75f;
    final float negativeSignTextWidth =  INFO_FONT.getSpaceWidth() * (float)(this.unsportingValue < 0 ? 1 : 0);

    final int xUnsportingTextOffset =
        (int)((this.hb.width - (amountTextWidth + negativeSignTextWidth))/2.0f);

    FontHelper.renderFontLeft(
        sb,
        headerFont,
        TEXT[0],
        this.xOffset + WIDTH * 0.04f,
        this.yOffset + this.yTextOffset,
        headerColor);

        //    FontHelper.renderFontLeft(
        //        sb,
        //        font, this.unsportingValue + separatorString + SportsmanshipInfo.MAX_UNSPORTING,
        //        this.xOffset + xUnsportingTextOffset,
        //        this.yOffset + this.yTextOffset - (yLineOffset * 1.035f),
        //        color);
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

  }

  private void refreshUnsportingAmount() {
    this.updateUnsportingValueFlag = true;
  }

  @Override
  public void atEndOfTurn() { refreshUnsportingAmount(); }

  @Override
  public void atStartOfCombat() { refreshUnsportingAmount(); }

  @Override
  public void onCardUsed(AbstractCard card) {
    refreshUnsportingAmount();
  }

  @Override
  public void atEndOfCombat() { refreshUnsportingAmount(); }

  private ArrayList<PowerTip> getPowerTips() {
    if (keywordPowerTips == null) {
      keywordPowerTips = new ArrayList<>();

      // crop keyword tooltips
      for (AbstractTooltipKeyword kw : TooltipKeywords.getTooltipKeywords(keywordList, baseGameKeywordList)) {
        final String name = TipHelper.capitalize(kw.getProperName());
        final String description = kw.getDescription();

        if (kw.hasGlyph()) {
          keywordPowerTips.add(new PowerTip(TipHelper.capitalize(name), description, kw.getGlyph().getGlyphAtlasRegion()));
        } else {
          keywordPowerTips.add(new PowerTip(TipHelper.capitalize(name), description));
        }
      }
    }
    return keywordPowerTips;
  }

  private Texture getPanelBackgroundImage(int numPenaltyCards) {
    final int index = Math.max(0, Math.min(numPenaltyCards, BACKGROUND_IMG_FILENAMES.size()));


    if (panelBackgroundImage[index] == null) {
      panelBackgroundImage[index] = TextureLoader.getTexture(BACKGROUND_IMG_FILEPATHS.get(index));
    }
    return panelBackgroundImage[index];
  }

  static {
    UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_NAME);
    TEXT = uiStrings.TEXT;
  }
}
