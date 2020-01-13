package thewrestler.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.util.TextureLoader;

import java.util.List;
import java.util.Map;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class Headgear extends CustomWrestlerRelic {
  // ID, images, text.
  public static final String ID = WrestlerMod.makeID("Headgear");

  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("headgear.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("headgear.png"));

  public Headgear() {
    super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.FLAT);
  }

  private static final int BLOCK_AMOUNT = 3;

  public void onApplyPower(AbstractPower power) {
    AbstractPlayer player = AbstractDungeon.player;
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, player, BLOCK_AMOUNT));
    triggerEffects();
  }

  private void triggerEffects() {
    AbstractDungeon.actionManager.addToBottom(new SFXAction("THUD_MEDIUM_1"));
    this.flash();
  }

  // Description
  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0] + BLOCK_AMOUNT + DESCRIPTIONS[1];
  }

  @Override
  protected List<String> getKeywordList() {
    return null;
  }

  @Override
  protected List<Keyword> getBaseGameKeywordList() {
    return null;
  }
}
