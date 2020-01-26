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
import thewrestler.util.BasicUtils;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.approval.ApprovalInfo;

import java.util.*;
import java.util.stream.Collectors;

// TODO: VFX/SFX when approval changes (e.g. red or green border flash)

public class WrestlerApprovalInfoPanel implements CustomInfoPanel, StartOfCombatListener, EndOfCombatListener {
  private static final String[] TEXT;

  private static final float WIDTH = 290;
  private static final float HEIGHT = 104;
  private static final float X_OFFSET = 24;
  private static final float Y_OFFSET = 786 + HEIGHT;
  private static final float Y_OFFSET_WIDESCREEN = 207 + HEIGHT;
  private static final float Y_TEXT_OFFSET =  HEIGHT - 20;
  private static final float Y_TEXT_OFFSET_WIDESCREEN = Y_TEXT_OFFSET + 0;
  private static final float TOOLTIP_X_OFFSET = WIDTH + 16.0F;
  private static final float TOOLTIP_Y_OFFSET = -(HEIGHT + 64.0f);

  private static final String UI_NAME = WrestlerMod.makeID("ApprovalInfoPanel");
  private static final String BACKGROUND_TEXTURE_PATH = UiHelper.getUiImageResourcePath("approvalinfopanel/background.png");

  private static final BitmapFont INFO_HEADER_FONT = FontHelper.charDescFont;
  private static final BitmapFont INFO_FONT = FontHelper.losePowerFont;
  private static final Color INFO_HEADER_COLOR = Color.valueOf("992200ff");

  private static final Color NEGATIVE_APPROVAL_COLOR = Settings.RED_TEXT_COLOR.cpy();
  private static final Color NEUTRAL_APPROVAL_COLOR = Color.WHITE.cpy();
  private static final Color POSITIVE_APPROVAL_COLOR = Settings.GREEN_TEXT_COLOR.cpy();

  private static final List<String> keywordList = Arrays.asList(
      ApprovalInfo.APPROVAL_KEYWORD_ID, ApprovalInfo.CLEAN_FIGHTING_KEYWORD_ID, ApprovalInfo.DIRTY_KEYWORD_ID);

  private static final List<Keyword> baseGameKeywordList = new ArrayList<>();
  private ArrayList<PowerTip> keywordPowerTips;

  private final String uiName;
  private final String backgroundImgPath;
  private Texture panelBackgroundImage;
  private final int xOffset;
  private final int yOffset;
  private final int yTextOffset;
  private Hitbox hb;

  private boolean updateApprovalValueFlag = false;
  private int approvalValue;

  public WrestlerApprovalInfoPanel() {
    this.uiName = UI_NAME;
    this.backgroundImgPath = BACKGROUND_TEXTURE_PATH;

    this.xOffset = Math.round(X_OFFSET * SettingsHelper.getScaleX());

    this.yOffset =  Math.round(
        (Settings.HEIGHT - (Settings.isSixteenByTen ? Y_OFFSET : Y_OFFSET_WIDESCREEN) * SettingsHelper.getScaleY()));
    this.yTextOffset =  Math.round(
        (Settings.isSixteenByTen ? Y_TEXT_OFFSET : Y_TEXT_OFFSET_WIDESCREEN) * SettingsHelper.getScaleY());

    this.hb = new Hitbox(WIDTH * SettingsHelper.getScaleX(), HEIGHT * SettingsHelper.getScaleY());
    hb.translate(xOffset, yOffset);
    this.approvalValue = 0;
  }

  @Override
  public void update() {
    this.hb.update();
    if (updateApprovalValueFlag) {
      this.approvalValue = WrestlerCharacter.getApprovalInfo().getApprovalAmount();
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
    final Color color = this.approvalValue > 0
        ? POSITIVE_APPROVAL_COLOR
        : (this.approvalValue < 0 ? NEGATIVE_APPROVAL_COLOR : NEUTRAL_APPROVAL_COLOR);

    final int yLineOffset = (int)(INFO_FONT.getLineHeight() * (Settings.isSixteenByTen ? 1.05f : 0.95f));

    final double amountTextWidth = INFO_FONT.getSpaceWidth()
        * (2.5f * (this.approvalValue == 0 ? 1 : Math.floor(Math.log10(Math.abs(this.approvalValue))) + 1));

    final float negativeSignTextWidth =  INFO_FONT.getSpaceWidth() * (this.approvalValue < 0 ? 1 : 0);

    final int xApprovalTextOffset =
        (int)((this.hb.width - (amountTextWidth + negativeSignTextWidth))/2.0f);

    FontHelper.renderFontLeft(
        sb,
        headerFont,
        TEXT[0],
        this.xOffset + WIDTH * 0.04f,
        this.yOffset + this.yTextOffset,
        headerColor);

    FontHelper.renderFontLeft(
        sb,
        font, this.approvalValue + "",
        this.xOffset + xApprovalTextOffset,
        this.yOffset + this.yTextOffset - (yLineOffset * 1.035f),
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

  }

  private void refreshApprovalAmount() {
    this.updateApprovalValueFlag = true;
  }

  @Override
  public void atEndOfTurn() { refreshApprovalAmount(); }

  @Override
  public void atStartOfCombat() { refreshApprovalAmount(); }

  @Override
  public void onCardUsed(AbstractCard card) {
    refreshApprovalAmount();
  }

                         @Override
  public void atEndOfCombat() { refreshApprovalAmount(); }

  private ArrayList<PowerTip> getPowerTips() {
    if (keywordPowerTips == null) {
      keywordPowerTips = new ArrayList<>();

      // crop keyword tooltips
      for (Map.Entry<String,String> entry : getKeywords().entrySet()) {
        keywordPowerTips.add(new PowerTip(TipHelper.capitalize(entry.getKey()), entry.getValue()));
      }
    }
    return keywordPowerTips;
  }


  private Map<String, String> getKeywords() {
    Map<String, String> keywords = keywordList.stream()
        .map(k -> WrestlerMod.getKeyword(k))
        .filter(Objects::nonNull)
        .collect(Collectors.toMap(kw -> kw.PROPER_NAME, kw -> kw.DESCRIPTION));

    keywords.putAll(baseGameKeywordList.stream()
        .collect(Collectors.toMap(kw -> kw.NAMES[0], kw -> kw.DESCRIPTION)));

    return keywords;
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
