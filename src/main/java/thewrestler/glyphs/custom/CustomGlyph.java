package thewrestler.glyphs.custom;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import thewrestler.WrestlerMod;
import thewrestler.util.info.penaltycard.*;
import java.util.*;

public class CustomGlyph {
  private static final String CUSTOM_GLYPH_BASE_DIR = WrestlerMod.getImageResourcePath("glyphs/");

  private static Map<CustomGlyphEnum, TextureAtlas.AtlasRegion> customGlyphAtlasRegionMap = new HashMap<>();
  private static Map<CustomGlyphEnum, String> customGlyphMap = new HashMap<>();

  public enum CustomGlyphEnum {
    PENALTY_CARD,
    PENALTY_CARD_BLUE,
    PENALTY_CARD_ORANGE,
    PENALTY_CARD_RED,
    PENALTY_CARD_YELLOW,
    PENALTY_CARD_GREEN,
    PENALTY_CARD_WHITE;
  }

  public static CustomGlyph getGlyph(CustomGlyphEnum glyphEnum) {
    Optional<CustomGlyph> opt = glyphList.stream().filter(g -> g.key.equals(glyphEnum)).findFirst();
    return opt.orElse(null);
  }

  private static final List<CustomGlyph> glyphList = Arrays.asList(
      new CustomGlyph(CustomGlyphEnum.PENALTY_CARD, "penaltycard.png", 'P'),

      new CustomGlyph(CustomGlyphEnum.PENALTY_CARD_BLUE, AbstractPenaltyCardSprite.GLYPH_DIR_PATH,
          BluePenaltyCardSprite.GLYPH_IMG_FILENAME, 'L'),

      new CustomGlyph(CustomGlyphEnum.PENALTY_CARD_GREEN, AbstractPenaltyCardSprite.GLYPH_DIR_PATH,
          GreenPenaltyCardSprite.GLYPH_IMG_FILENAME, 'N'),

      new CustomGlyph(CustomGlyphEnum.PENALTY_CARD_ORANGE, AbstractPenaltyCardSprite.GLYPH_DIR_PATH,
          OrangePenaltyCardSprite.GLYPH_IMG_FILENAME, 'O'),

      new CustomGlyph(CustomGlyphEnum.PENALTY_CARD_RED, AbstractPenaltyCardSprite.GLYPH_DIR_PATH,
          RedPenaltyCardSprite.GLYPH_IMG_FILENAME, 'D'),

      new CustomGlyph(CustomGlyphEnum.PENALTY_CARD_YELLOW, AbstractPenaltyCardSprite.GLYPH_DIR_PATH,
          YellowPenaltyCardSprite.GLYPH_IMG_FILENAME, 'Y'),

      new CustomGlyph(CustomGlyphEnum.PENALTY_CARD_WHITE, AbstractPenaltyCardSprite.GLYPH_DIR_PATH,
                      WhitePenaltyCardSprite.GLYPH_IMG_FILENAME, '~')
  );

  public static TextureAtlas.AtlasRegion getAtlasRegion(CustomGlyphEnum key) {
    if (CustomGlyph.customGlyphAtlasRegionMap.containsKey(key)) {
      return CustomGlyph.customGlyphAtlasRegionMap.get(key);
    }
    return getPlaceholderGlyph(); }

  private static TextureAtlas.AtlasRegion getPlaceholderGlyph() {
    return BaseMod.getCardSmallEnergy();
  }

  public static List<CustomGlyph> getCustomGlyphsList() {
    return Collections.unmodifiableList(glyphList);
  }

  private final CustomGlyphEnum key;
  private final String filepath;
  private final char inlineCodeLetter;

  public CustomGlyph(CustomGlyphEnum key, String glyphSubdirectory, String imageFilename, char inlineCodeLetter) {
    this.key = key;
    this.filepath = glyphSubdirectory + imageFilename;
    this.inlineCodeLetter = inlineCodeLetter;
    customGlyphMap.put(this.key, this.filepath);
  }

  public CustomGlyph(CustomGlyphEnum key, String imageFilename, char inlineCodeLetter) {
    this(key, CUSTOM_GLYPH_BASE_DIR, imageFilename, inlineCodeLetter);
  }

  public char getCodeLetter() {
    return this.inlineCodeLetter;
  }

  public String getFullCode() {
    return "[" + this.inlineCodeLetter + "]";
  }

  public  TextureAtlas.AtlasRegion getGlyphAtlasRegion() {
    TextureAtlas.AtlasRegion glyph = CustomGlyph.customGlyphAtlasRegionMap.get(this.key);
    if (glyph != null) {
      return glyph;
    }
    final String glyphFile = CustomGlyph.customGlyphMap.get(this.key);
    if (glyphFile != null) {
      final Texture glyphTexture = ImageMaster.loadImage(glyphFile);
      final int tw = glyphTexture.getWidth();
      final int th = glyphTexture.getHeight();
      glyph = new TextureAtlas.AtlasRegion(glyphTexture, 0, 0, tw, th);
      CustomGlyph.customGlyphAtlasRegionMap.put(this.key, glyph);
      return glyph;
    }
    return getPlaceholderGlyph();
  }
}
