package thewrestler.util.info.penaltycard;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
    return getTextureFromMap(this.id, this.imgFilePath);
  }

  public void render(SpriteBatch sb) {
    sb.draw(this.getTexture(), this.hb.x, this.hb.y);
  }

  public void update() {
    this.hb.update();
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

  // TODO: implement this
  public void flash() {

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