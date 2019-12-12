package theplaceholder.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theplaceholder.util.TextureLoader;

import static theplaceholder.PlaceholderMod.makeRelicOutlinePath;
import static theplaceholder.PlaceholderMod.makeRelicPath;

public class Headgear extends CustomRelic {
  // ID, images, text.
  public static final String ID = theplaceholder.PlaceholderMod.makeID("Headgear");

  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("headgear.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("headgear.png"));

  public Headgear() {
    super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.FLAT);
  }

  private static final int BLOCK_AMOUNT = 4;

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

}
