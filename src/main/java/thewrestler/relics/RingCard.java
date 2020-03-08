package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import thewrestler.WrestlerMod;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.BasicUtils;
import thewrestler.util.TextureLoader;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

// TODO: Render keyword tooltips for Liked, Disliked, and Approval

public class RingCard extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("RingCard");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ringcard.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ringcard.png"));

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(WrestlerMod.makeID("Liked"), WrestlerMod.makeID("Disliked"));

  public static final int HEAL_AMOUNT = 3;
  public static final int REWARD_PERCENTAGE_BONUS = 20;
  public static final int RELIC_PERCENTAGE_CHANCE = 25;

  public boolean shouldRewardGold() {
    return false;
    // DISABLING IN FAVOR OF RELIC REWARD
    // return BasicUtils.isPlayingAsWrestler() && ApprovalInfo.isUnsporting();
  }

  private boolean shouldAwardCombatRelic() {
    if (!WrestlerCharacter.hasPenaltyCardInfo()) {
      return  false;
    }
    return !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)
        && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite)
        && AbstractDungeon.relicRng.random(0, 99) < RELIC_PERCENTAGE_CHANCE;
  }

  public RingCard() {
    super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
  }

  @Override
  public String getUpdatedDescription() {
//    return DESCRIPTIONS[0] + HEAL_AMOUNT + DESCRIPTIONS[1] + REWARD_PERCENTAGE_BONUS + DESCRIPTIONS[2];
    return DESCRIPTIONS[0] + HEAL_AMOUNT + DESCRIPTIONS[1] + RELIC_PERCENTAGE_CHANCE + DESCRIPTIONS[2];
  }

  @Override
  public void onVictory() {

    // TODO: add new condition & effect
    if (BasicUtils.isPlayingAsWrestler()) {
      flash();
      AbstractPlayer player = AbstractDungeon.player;
      addToTop(new RelicAboveCreatureAction(player, this));
      if (player.currentHealth > 0) {
        player.heal(HEAL_AMOUNT);
      }
    }
    if (shouldAwardCombatRelic()) {
      flash();
      awardRandomRelic();
    }
  }

  private void awardRandomRelic() {
     AbstractDungeon.getCurrRoom().addRelicToRewards(getRandomRelicTier());
  }

  private RelicTier getRandomRelicTier() {
    return RelicTier.COMMON;

    /*
    int roll = AbstractDungeon.relicRng.random(0, 99);
    if (roll < 50) {
      return AbstractRelic.RelicTier.COMMON;
    }
    if (roll > 85) {
      return AbstractRelic.RelicTier.RARE;
    }
    return AbstractRelic.RelicTier.UNCOMMON;
    */
  }

  @Override
  protected List<String> getKeywordList() {
    return POWERTIP_KEYWORDS;
  }

  @Override
  protected List<Keyword> getBaseGameKeywordList() {
    return null;
  }
}