package thewrestler.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.BasicUtils;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.ApprovalInfo;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

// TODO: Render keyword tooltips for Liked, Disliked, and Approval

public class RingCard extends CustomRelic {
  public static final String ID = WrestlerMod.makeID("RingCard");

  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ringcard.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ringcard.png"));


  public static final int HEAL_AMOUNT = 3;
  public static final int REWARD_PERCENTAGE_BONUS = 10;

  public RingCard() {
    super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0] + HEAL_AMOUNT + DESCRIPTIONS[1] + REWARD_PERCENTAGE_BONUS + DESCRIPTIONS[2];
  }

  @Override
  public void onVictory() {
    if (!BasicUtils.isPlayingAsWrestler()
        || !WrestlerCharacter.hasApprovalInfo()
        || WrestlerCharacter.getApprovalInfo().isDisliked()) {
      flash();
      AbstractPlayer player = AbstractDungeon.player;
      addToTop(new RelicAboveCreatureAction(player, this));
      if (player.currentHealth > 0) {
        player.heal(HEAL_AMOUNT);
      }
    }
  }
}