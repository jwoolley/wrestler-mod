package thewrestler.relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import thewrestler.WrestlerMod;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.BasicUtils;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.approval.ApprovalInfo;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

// TODO: Render keyword tooltips for Liked, Disliked, and Approval

public class FightCard extends CustomWrestlerRelic implements CustomSavable<Integer> {
  public static final String ID = WrestlerMod.makeID("FightCard");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("fightcard.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("fightcard.png"));

  private static final List<String> POWERTIP_KEYWORDS =
      Arrays.asList(WrestlerMod.makeID("Liked"), WrestlerMod.makeID("Disliked"),
                    WrestlerMod.makeID("PeoplesCrown"), WrestlerMod.makeID("BrutesTrophy"));

  private static AbstractRelic POPULAR_RELIC_REWARD = new PeoplesCrown();
  private static AbstractRelic UNPOPULAR_RELIC_REWARD = new BrutesTrophy();
  private static final int GOLD_ON_PICKUP = 40;
  private static final int THRESHOLD = 3;

  private int fightRecord;

  // TODO: TOOLTIP with RECORD
  // change color of counter text? red or green?
  public FightCard() {
    super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    fightRecord = 0;
    counter = -1;
  }

  public boolean shouldRewardRelic() {
    return !this.usedUp && Math.abs(fightRecord) >= THRESHOLD;
  }

  public AbstractRelic redeemRelicReward() {
    usedUp();
    this.fightRecord = RELIC_USED_KEY;
    this.counter = -1;

    if (BasicUtils.isPlayingAsWrestler()
        && WrestlerCharacter.hasApprovalInfo()) {
      ApprovalInfo info = WrestlerCharacter.getApprovalInfo();
      return info.isLiked() ? POPULAR_RELIC_REWARD : UNPOPULAR_RELIC_REWARD;
    }

    return AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier());
  }

  @Override
  public boolean canSpawn() {
    final AbstractPlayer player = AbstractDungeon.player;
    return !player.hasRelic(BrutesTrophy.ID) && !player.hasRelic(PeoplesCrown.ID);
  }

  @Override
  public void onEquip() {
    CardCrawlGame.sound.play("GOLD_GAIN");
    AbstractDungeon.player.gainGold(GOLD_ON_PICKUP);
  }

  @Override
  public void onVictory() {
    if (this.usedUp) {
      setCounter(-1);
      return;
    }

    if (!this.usedUp && BasicUtils.isPlayingAsWrestler()
        || WrestlerCharacter.hasApprovalInfo()) {

      ApprovalInfo info = WrestlerCharacter.getApprovalInfo();

      if (info.isDisliked()) {
        triggerRelicTickEffect();
        if (this.fightRecord > 0) {
          this.fightRecord = -1;
        } else if (this.fightRecord > -THRESHOLD) {
          this.fightRecord--;
        }
      } else if (info.isLiked()) {
        triggerRelicTickEffect();
        if (this.fightRecord < 0) {
          this.fightRecord = 1;
        } else if (this.fightRecord < THRESHOLD) {
          this.fightRecord++;
        }
      } else {
        this.fightRecord = 0;
        this.counter = -1;
      }
    }
    setCounter(Math.abs(this.fightRecord));
  }

  public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
    if (this.counter > 0) {
      final Color color = this.fightRecord > 0 ? Color.GREEN.cpy() : Color.RED.cpy();
      if (inTopPanel) {
        FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont,
            Integer.toString(this.counter), this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, color);
      } else {
        FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont,
            Integer.toString(this.counter), this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, color);
      }
    }
  }

  private void triggerRelicTickEffect() {
    flash();
    if (shouldRewardRelic()) {
      CardCrawlGame.sound.play("TINGSHA");
    } else {
      CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");
    }
  }

  @Override
  protected List<String> getKeywordList() {
    return POWERTIP_KEYWORDS;
  }

  @Override
  protected List<Keyword> getBaseGameKeywordList() {
    return null;
  }

  final static private int RELIC_USED_KEY = -999;
  @Override
  public Integer onSave() {
    return this.usedUp ? RELIC_USED_KEY : this.fightRecord;
  }

  @Override
  public void onLoad(Integer i) {
    this.fightRecord = i;
    if (i == RELIC_USED_KEY) {
      usedUp();
      this.counter = -1;
    } else {
      this.counter = Math.abs(fightRecord);
    }
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0] + GOLD_ON_PICKUP + DESCRIPTIONS[1] + THRESHOLD + DESCRIPTIONS[2] + THRESHOLD + DESCRIPTIONS[3];
  }
}