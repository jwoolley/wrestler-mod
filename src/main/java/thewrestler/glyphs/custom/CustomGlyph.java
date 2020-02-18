package thewrestler.glyphs.custom;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import thewrestler.WrestlerMod;

import java.util.*;

public class CustomGlyph {
  private static final String CUSTOM_GLYPH_BASE_DIR = WrestlerMod.getImageResourcePath("glyphs/");

  private static Map<CustomGlyphEnum, TextureAtlas.AtlasRegion> customGlyphAtlasRegionMap = new HashMap<>();
  private static Map<CustomGlyphEnum, String> customGlyphMap = new HashMap<>();

  private enum CustomGlyphEnum {
    PENALTY_CARD;
  }

  private static final List<CustomGlyph> glyphList = Arrays.asList(
      new CustomGlyph(CustomGlyphEnum.PENALTY_CARD, "penaltycard.png", 'P')
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

  public CustomGlyph(CustomGlyphEnum key, String imageFilename, char inlineCodeLetter) {
    this.key = key;
    this.filepath = CUSTOM_GLYPH_BASE_DIR + imageFilename;
    this.inlineCodeLetter = inlineCodeLetter;
    customGlyphMap.put(this.key, this.filepath);
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
