package thewrestler.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.ui.SettingsHelper;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeOrbPath;

abstract public class BasePenaltyOrb extends AbstractOrb {

  // Standard ID/Description
  public static final String ORB_ID = WrestlerMod.makeID("BasePenaltyOrb");
  private static final OrbStrings ORB_STRINGS = CardCrawlGame.languagePack.getOrbString(ORB_ID);
  private static final String NAME_PREFIX = ORB_STRINGS.NAME;
  public static final String[] DESCRIPTIONS = ORB_STRINGS.DESCRIPTION;

  // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
  private float vfxTimer = 1.0f;
  private float vfxIntervalMin = 0.1f;
  private float vfxIntervalMax = 0.4f;
  private static final float ORB_WAVY_DIST = 0.04f;
  private static final float PI_4 = 12.566371f;

  final private AbstractPenaltyStatusCard penaltyCard;

  private boolean hadWarningCard = false;
  private boolean alphaWaning = false;
  private final float ORB_ALPHA_MAX = 1.5f;
  private final float ORB_ALPHA_MIN = 1.0f;
  private final float ORB_ALPHA_DELTA = 0.01f;
  private final float ORB_ALPHA_NO_WARNING = 2.0f;
  private float orbAlphaModifier = ORB_ALPHA_MAX;

  private static final List<String> keywordList = Arrays.asList(
      CustomTooltipKeywords.SPORTSMANSHIP);

  private static final List<Keyword> baseGameKeywordList = new ArrayList<>();
  private ArrayList<PowerTip> keywordPowerTips;

  private final String PENALTY_ORB_SUBDIR_PATH = "penalty/";

  // TODO: penalty card (like old custom hover)

  private final String[] ORB_SPECIFIC_DESCRIPTIONS;

  private final ArrayList<PowerTip> powerTips = new ArrayList<>();

  private Texture getPenaltyOrbTexture(String imgName) {
    return TextureLoader.getTexture(makeOrbPath(PENALTY_ORB_SUBDIR_PATH + imgName));
  }

  public BasePenaltyOrb(String orbId, String name, String[] description, String imgName, AbstractPenaltyStatusCard penaltyCard) {
    this.ID = orbId;
    this.name = NAME_PREFIX + name;
    this.description = "";
    ORB_SPECIFIC_DESCRIPTIONS = description;
    this.img = getPenaltyOrbTexture(imgName);
    this.penaltyCard = penaltyCard;

    angle = MathUtils.random(0.0f); // More Animation-related Numbers
    channelAnimTimer = 0.5f;

    updateDescription();
    evokeAmount = baseEvokeAmount = 0;
    passiveAmount = basePassiveAmount = 0;
    this.hideEvokeValues();

    resetPowerTips();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + PenaltyCardInfo.numDirtyCardsToTriggerNextGain() + DESCRIPTIONS[1];
    this.resetPowerTips();
  }

  @Override
  public void applyFocus() { }

  @Override
  public void onEvoke() {  }

  @Override
  public void onStartOfTurn() { }

  @Override
  public void updateAnimation() {
    super.updateAnimation();
    vfxTimer -= Gdx.graphics.getDeltaTime();
    if (vfxTimer < 0.0f) {
      vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
    }
  }

  @Override
  public void update() {
    this.hb.update();
    if (this.hb.hovered) {
      TipHelper.queuePowerTips(getXTooltipOffset(), getYTooltipOffset(), powerTips);
      TipHelper.renderGenericTip(this.tX + 96.0F * Settings.scale, this.tY + 64.0F * Settings.scale, this.name, this.description);
    }

    this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
  }

  @Override
  public void render(SpriteBatch sb) {
    if (PenaltyCardInfo.hasWarningCard()) {
      if (!hadWarningCard) {
        orbAlphaModifier = ORB_ALPHA_MAX;
        hadWarningCard = true;
      }
      sb.setColor(new Color(1.0f, 1.0f, 1.0f, c.a /orbAlphaModifier));

      if (alphaWaning) {
        orbAlphaModifier -= ORB_ALPHA_DELTA;
        if (orbAlphaModifier <= ORB_ALPHA_MIN) {
          alphaWaning = false;
        }
      } else {
        orbAlphaModifier += ORB_ALPHA_DELTA;
        if (orbAlphaModifier >= ORB_ALPHA_MAX) {
          alphaWaning = true;
        }
      }
    } else {
      sb.setColor(new Color(0.75f, 0.75f, 0.75f, c.a / ORB_ALPHA_NO_WARNING));
      hadWarningCard = false;
    }

    sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f,
        scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, scale, angle,
        0, 0, 96, 96, false, false);

    // TODO: draw preview card on hover

    hb.render(sb);
  }

  private float getXTooltipOffset() {
    final float TOOLTIP_X_OFFSET = 96.0F;
    return this.hb.x + TOOLTIP_X_OFFSET * SettingsHelper.getScaleX();
  }

  private float getYTooltipOffset() {
    final float TOOLTIP_Y_OFFSET = 8.0F;
    return this.hb.cY + TOOLTIP_Y_OFFSET * SettingsHelper.getScaleY();
  }

  private void resetPowerTips() {
    if (keywordPowerTips == null) {
      keywordPowerTips = new ArrayList<>();
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

    ArrayList<PowerTip> tooltips = new ArrayList<>(keywordPowerTips);
    tooltips.add(new PowerTip(this.name, this.description));

//    final CustomTooltipKeyword pcKeyword =  this.penaltyCard.getTooltipKeyword();
//    tooltips.add(new PowerTip(pcKeyword.getProperName(), pcKeyword.getDescription(),
//        pcKeyword.getGlyph().getGlyphAtlasRegion()));

    this.powerTips.clear();
    this.powerTips.addAll(tooltips);
  }

  @Override
  public void playChannelSFX() { }
}
