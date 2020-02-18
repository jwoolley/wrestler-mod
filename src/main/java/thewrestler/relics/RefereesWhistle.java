package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import thewrestler.WrestlerMod;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class RefereesWhistle extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("RefereesWhistle");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("refereeswhistle.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("refereeswhistle.png"));
  private static final RelicTier RELIC_TIER = RelicTier.BOSS;

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(WrestlerMod.makeID("Disliked"));

  public static final int CARD_AMOUNT = 1;

  public RefereesWhistle() {
    super(ID, IMG, OUTLINE, RELIC_TIER, LandingSound.CLINK);
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0];
  }

  public void atTurnStart() {
    if (SportsmanshipInfo.hasSportsmanshipInfo() && SportsmanshipInfo.isUnsporting()) {
      flash();
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(CARD_AMOUNT));
    }
  }

  @Override
  public boolean canSpawn() {
    return SportsmanshipInfo.hasSportsmanshipInfo() && !AbstractDungeon.player.hasRelic(LuckyTrunks.ID);
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