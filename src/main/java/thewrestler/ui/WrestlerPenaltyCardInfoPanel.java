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
import thewrestler.util.info.penaltycard.AbstractPenaltyCardSprite;

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
  private static final int PENALTY_CARD_X_OFFSET = 83;
  private static final int PENALTY_CARD_X_DELTA_OFFSET = 15;
  private static final int PENALTY_CARD_Y_OFFSET = -16;
  private static final int PENALTY_CARD_Y_OFFSET_WIDSCREEN = -12;

  private static final String UI_NAME = WrestlerMod.makeID("PenaltyCardInfoPanel");

  private static final String BACKGROUND_IMG_DIR_PATH = "penaltycardinfopanel/";

//  private static final List<String> BACKGROUND_IMG_FILENAMES = Arrays.asList(
//    "background-zero-cards.png", "background-one-card.png", "background-two-cards.png", "background-three-cards.png");


  private static final List<String> BACKGROUND_IMG_FILENAMES = Arrays.asList(
      "background-no-warning.png", "background-with-warning.png", "background-two-cards.png", "background-three-cards.png");

  private static List<String> BACKGROUND_IMG_FILEPATHS = BACKGROUND_IMG_FILENAMES.stream()
      .map(f -> UiHelper.getUiImageResourcePath(BACKGROUND_IMG_DIR_PATH + f)).collect(Collectors.toList());

  private static final BitmapFont INFO_HEADER_FONT = FontHelper.charDescFont;
  private static final BitmapFont INFO_FONT = FontHelper.losePowerFont;
  private static final Color INFO_HEADER_COLOR = Color.valueOf("992200ff");

  private static final Color NEGATIVE_UNSPORTING_COLOR = Settings.GREEN_TEXT_COLOR.cpy();
  private static final Color NEUTRAL_UNSPORTING_COLOR = Color.WHITE.cpy();
  private static final Color POSITIVE_UNSPORTING_COLOR = Settings.RED_TEXT_COLOR.cpy();

  // TODO: only show Penalty Cards tooltip if no specific Penalty Card tooltip is shown (i.e. none are hovered)
  private static final List<String> keywordList = Arrays.asList(
      CustomTooltipKeywords.SPORTSMANSHIP,
      CustomTooltipKeywords.PENALTY_CARDS_NO_GLYPH,
      CustomTooltipKeywords.DIRTY);

  private static final List<Keyword> baseGameKeywordList = new ArrayList<>();
  private ArrayList<PowerTip> keywordPowerTips;
  private ArrayList<PowerTip> currentPowerTips = new ArrayList<>(getPowerTips());

  private final String uiName;
  private Texture[] panelBackgroundImage = new Texture[BACKGROUND_IMG_FILEPATHS.size()];
  private final int xOffset;
  private final int yOffset;
  private final int yTextOffset;
  private final int yPenaltyCardOffset;
  private Hitbox hb;

  private boolean updateWarningCardFlag = false;
  private boolean hasWarningCard;

  public WrestlerPenaltyCardInfoPanel() {
    this.uiName = UI_NAME;

    this.xOffset = Math.round(X_OFFSET * SettingsHelper.getScaleX());

    this.yOffset =  Math.round(
        (Settings.HEIGHT - (Settings.isSixteenByTen ? Y_OFFSET : Y_OFFSET_WIDESCREEN) * SettingsHelper.getScaleY()));
    this.yTextOffset =  Math.round(
        (Settings.isSixteenByTen ? Y_TEXT_OFFSET : Y_TEXT_OFFSET_WIDESCREEN) * SettingsHelper.getScaleY());

    this.yPenaltyCardOffset =  Math.round(
        (Settings.isSixteenByTen ? PENALTY_CARD_Y_OFFSET : PENALTY_CARD_Y_OFFSET_WIDSCREEN) * SettingsHelper.getScaleY());

    this.hb = new Hitbox(WIDTH * SettingsHelper.getScaleX(), HEIGHT * SettingsHelper.getScaleY());
    hb.translate(xOffset, yOffset);
    this.hasWarningCard = false;
  }

  @Override
  public void update() {
    this.hb.update();
    if (updateWarningCardFlag) {
      this.hasWarningCard = WrestlerCharacter.getPenaltyCardInfo().hasWarningCard();
    }

    // TODO: change to update warning card if present
    // WrestlerCharacter.getSportsmanshipInfo().getPenaltyCards().forEach(AbstractPenaltyCard::update);
  }

  @Override
  public void render(SpriteBatch sb) {
    if (shouldRender()) {
      // use the empty panel as background image. remove other bg images once dynamic penatly cards are in place
      Texture backgroundImage = getPanelBackgroundImage(0);
      // Texture backgroundImage = getPanelBackgroundImage(this.hasWarningCard);
      List<PowerTip> powerTips = new ArrayList<>(getPowerTips());

      sb.draw(backgroundImage,
          this.xOffset, this.yOffset,
          backgroundImage.getWidth() * SettingsHelper.getScaleX(),  backgroundImage.getHeight() * SettingsHelper.getScaleY(),
          0, 0,
          backgroundImage.getWidth(),  backgroundImage.getHeight(),
          false, false);

      // TODO: this should render warning card, if present
      List<AbstractPenaltyCardSprite> cards = new ArrayList<>(); //WrestlerCharacter.getSportsmanshipInfo().getPenaltyCards();

      if (!cards.isEmpty()) {
        cards.get(0).setPosition(this.xOffset + PENALTY_CARD_X_OFFSET, this.yOffset - yPenaltyCardOffset);

        for (int i = 1; i < cards.size(); i++) {
          AbstractPenaltyCardSprite card = cards.get(i);
          card.setPosition(this.xOffset
                  + PENALTY_CARD_X_OFFSET + (PENALTY_CARD_X_DELTA_OFFSET + AbstractPenaltyCardSprite.WIDTH) * i,
                  this.yOffset - yPenaltyCardOffset);
        }

        currentPowerTips.clear();
        currentPowerTips.addAll(getPowerTips());

        cards.forEach(c -> {
          c.render(sb);
          if (c.isHovered()) {
            currentPowerTips.add(c.getPowerTip());
          }
        });
      }

      if (this.hb.hovered) {
        TipHelper.queuePowerTips(
            hb.x + TOOLTIP_X_OFFSET * SettingsHelper.getScaleX(),
            hb.y - TOOLTIP_Y_OFFSET * SettingsHelper.getScaleY(), currentPowerTips);
      }

      renderInfoText(sb);

      hb.render(sb);
    }
  }

  private void renderInfoText(SpriteBatch sb) {
    final BitmapFont headerFont = INFO_HEADER_FONT;
    final BitmapFont font = INFO_FONT;
    final Color headerColor = INFO_HEADER_COLOR;
    final Color color = NEUTRAL_UNSPORTING_COLOR;

    final int yLineOffset = (int)(INFO_FONT.getLineHeight() * (Settings.isSixteenByTen ? 1.05f : 0.95f));

    final String separatorString = "/";

    final double amountTextWidth = INFO_FONT.getSpaceWidth() * 0.75f;

    final int xUnsportingTextOffset =
        (int)((this.hb.width - (amountTextWidth))/2.0f);

    FontHelper.renderFontLeft(
        sb,
        headerFont,
        TEXT[0],
        this.xOffset + WIDTH * 0.04f,
        this.yOffset + this.yTextOffset,
        headerColor);

        //    FontHelper.renderFontLeft(
        //        sb,
        //        font, this.hasWarningCard + separatorString + SportsmanshipInfo.MAX_PENALTY_CARDS,
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
    wasGainingPenaltyCard = false;
  }

  private boolean wasGainingPenaltyCard;

  private void refreshUnsportingAmount() {
    this.updateWarningCardFlag = false;
  }

  @Override
  public void atEndOfTurn() { refreshUnsportingAmount(); }

  @Override
  public void atStartOfCombat() { refreshUnsportingAmount(); }

  @Override
  public void onCardUsed(AbstractCard card) {
    refreshUnsportingAmount();
    final boolean willGainPenaltyCard = false; // WrestlerCharacter.getSportsmanshipInfo().willGainPenaltyCard();
    if (!wasGainingPenaltyCard && willGainPenaltyCard
      && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
      triggerWillGainCardAtEndOfTurn();
    }
    this.wasGainingPenaltyCard = willGainPenaltyCard;
  }

  private void triggerWillGainCardAtEndOfTurn() {
      CardCrawlGame.sound.play("WHISTLE_BLOW_1");
      // TODO: flash / pulse next penalty card (move forward/backward or disable as # penalty cards changes)
      //       (also reverse-pulse if will lose card)
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
