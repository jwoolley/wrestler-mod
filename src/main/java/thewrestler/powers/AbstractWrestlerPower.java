package thewrestler.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thewrestler.WrestlerMod;

public class AbstractWrestlerPower extends AbstractPower {
  private static final String BASE_DIR = WrestlerMod.getImageResourcePath("powers/");
  protected static final Logger logger = WrestlerMod.logger;

  protected AbstractCreature source;
  protected final String imgFilename;

  public AbstractWrestlerPower(String id, String name, String imgFilename,
                               AbstractCreature owner, AbstractCreature source, int amount, PowerType powerType) {
    this.ID = id;
    this.name = name;
    this.owner = owner;
    this.source = source;
    this.amount = amount;
    this.type = powerType;
    this.imgFilename = imgFilename;
    updateDescription();

    this.region128 =
        new TextureAtlas.AtlasRegion(
            ImageMaster.loadImage(BASE_DIR + "128/" + imgFilename), 0, 0, 128, 128);

    this.region48 =
        new TextureAtlas.AtlasRegion(
            ImageMaster.loadImage(BASE_DIR + "48/" + imgFilename), 0, 0, 48, 48);
  }
}
