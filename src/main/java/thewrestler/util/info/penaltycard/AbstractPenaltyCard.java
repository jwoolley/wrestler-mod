package thewrestler.util.info.penaltycard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import thewrestler.glyphs.custom.CustomGlyph;
import thewrestler.ui.UiHelper;
import thewrestler.WrestlerMod;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPenaltyCard {
  public static final float WIDTH = 32;
  public static final float HEIGHT = 38;
  public static final String GLYPH_DIR_PATH = WrestlerMod.getImageResourcePath("ui/penaltycards/tooltipglyph/");
  private static final String ICON_DIR_PATH = "penaltycards/";

  private float flashTimer = 0.0F;
  private final float FLASH_DURATION = 1.0f;
  private final float PULSE_DURATION = 1.0f;
  private Color flashColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
  private float scale = 1.0f;
  private float rotation = 0.0f;
  private boolean pulse = false;

  private static Map<String, PowerTip> powerTips = new HashMap<>();

  private final String id;
  private final String name;
  private final String description;
  private final String imgFilePath;
  private final Hitbox hb;

  public AbstractPenaltyCard(String id, String name, String description, String imgFilePath, int xPos, int yPos) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.imgFilePath = imgFilePath;
    this.hb = new Hitbox(xPos, yPos, WIDTH, HEIGHT);
  }

  public void setPosition(float x, float y) {
    this.hb.x = x;
    this.hb.y = y;
  }

  public AbstractPenaltyCard(String id, String name, String description, String imgFilePath) {
    this(id, name, description, imgFilePath, 0,  0);
  }

  public abstract CustomGlyph getTooltipGlyph();

  public PowerTip getPowerTip() {
    if (!powerTips.containsKey(this.id)) {
      PowerTip powerTip;
      powerTip = new PowerTip(this.name, this.description, this.getTooltipGlyph().getGlyphAtlasRegion());
      powerTips.put(this.id, powerTip);
    }

    return powerTips.get(this.id);
  }

  public Texture getTexture() {
    if (this.flashTimer > 0.0f) {
      return getTextureFromMap(WhitePenaltyCard.ID, WhitePenaltyCard.IMG_FILENAME);
    } else {
      return getTextureFromMap(this.id, this.imgFilePath);
    }
  }

  public void render(SpriteBatch sb) {
    sb.setColor(Color.WHITE.cpy());
    sb.draw(this.getTexture(), this.hb.x, this.hb.y);
    renderFlash(sb);
    sb.setColor(Color.WHITE.cpy());
  }

  public void update() {
    updateFlash();
    this.hb.update();
  }

  private void updateFlash() {
    if (this.flashTimer != 0.0F) {
      this.flashTimer -= Gdx.graphics.getDeltaTime();
      if (this.flashTimer < 0.0F) {
        if (this.pulse) {
          this.flashTimer = PULSE_DURATION;
        } else {
          this.flashTimer = 0.0F;
        }
      }
    }
  }

  public boolean isHovered() {
    return this.hb.hovered;
  }

  public abstract AbstractPenaltyCard makeCopy();

  public abstract void onGained();
  public abstract void onRemoved();
  public abstract void atStartOfTurn();
  public abstract void atEndOfTurn();
  public abstract void onCardUsed(AbstractCard card);
  public abstract void onCardExhausted(AbstractCard card);

  static void playTriggerSfx() {
    CardCrawlGame.sound.play("WHISTLE_BLOW_1");
  }

  public void flash() {
    playTriggerSfx();
    this.flashTimer = FLASH_DURATION;
  }

  public void renderFlash(SpriteBatch sb) {
  float tmp = Interpolation.exp10In.apply(0.0F, 4.0F, this.flashTimer / 2.0F);

    sb.setBlendFunction(770, 1);
    this.flashColor.a = (this.flashTimer * 0.2F);
    sb.setColor(this.flashColor);

    final float inner = 16.0f;
    final float innerOffset = 16.0f;
    final float outer = 32.0f;

    float tmpX = this.hb.x - inner;

    sb.draw(this.getTexture(), tmpX, this.hb.y - inner + innerOffset, inner, inner, outer, outer,
        this.scale + tmp, this.scale + tmp,this.rotation, 0, 0, (int)outer, (int)outer,
        false, false);

    sb.draw(this.getTexture(), tmpX, this.hb.y - inner + innerOffset, inner, inner, outer, outer,
        this.scale + tmp * 0.66F, this.scale + tmp * 0.66F, this.rotation, 0, 0, (int)outer,
        (int)outer, false, false);

    sb.draw(this.getTexture(), tmpX, this.hb.y - inner + innerOffset, inner, inner, outer, outer,
        this.scale + tmp / 3.0F, this.scale + tmp / 3.0F, this.rotation, 0, 0, (int)outer,
        (int)outer, false, false);

    sb.setBlendFunction(770, 771);
}

  public void beginPulse() {
    this.flashTimer = 1.5F;
  }

  public void beginLongPulse() {
    this.flashTimer = 1.0F;
    this.pulse = true;
  }

  public void stopPulse() {
    this.pulse = false;
  }

  public String getDebugDescription() {
    return this.name + " : " + this.description;
  }

  private static Texture getTextureFromMap(String id, String filePath) {
    if (!cardIconTextures.containsKey(id)) {
      Texture texture = ImageMaster.loadImage(UiHelper.getUiImageResourcePath(ICON_DIR_PATH + filePath));
      cardIconTextures.put(id, texture);
    }
    return cardIconTextures.get(id);
  }
  private static Map<String, Texture> cardIconTextures = new HashMap<>();
}