package thewrestler.util.info.penaltycard;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import thewrestler.ui.UiHelper;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPenaltyCard {
  private static final String ICON_DIR_PATH = "penaltycards/";

  private final String id;
  private final String name;
  private final String description;
  private final String imgFilePath;

  public AbstractPenaltyCard(String id, String name, String description, String imgFilePath) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.imgFilePath = imgFilePath;
  }

  public Texture getTexture() {
    return getTextureFromMap(this.id, this.imgFilePath);
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