package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import thewrestler.WrestlerMod;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class LuckyTrunks extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("LuckyTrunks");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("luckytrunks.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("luckytrunks.png"));
  private static final RelicTier RELIC_TIER = RelicTier.BOSS;

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(WrestlerMod.makeID("Liked"));

  public static final int ENERGY_AMOUNT = 1;

  public LuckyTrunks() {
    super(ID, IMG, OUTLINE, RELIC_TIER, LandingSound.FLAT);
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0];
  }

  public void atTurnStart() {
    if (SportsmanshipInfo.hasSportsmanshipInfo() && SportsmanshipInfo.isSporting()) {
      flash();
      AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_AMOUNT));
    }
  }

  @Override
  public boolean canSpawn() {
    return SportsmanshipInfo.hasSportsmanshipInfo() && !AbstractDungeon.player.hasRelic(RefereesWhistle.ID);
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