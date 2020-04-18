package thewrestler.relics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class RefereesWhistle extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("RefereesWhistle");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("refereeswhistle.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("refereeswhistle.png"));
  private static final RelicTier RELIC_TIER = RelicTier.STARTER;

  public RefereesWhistle() {
    super(ID, IMG, OUTLINE, RELIC_TIER, LandingSound.CLINK);
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0];
  }

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(WrestlerMod.makeID("PenaltyCard"));

  @Override
  protected List<String> getKeywordList() {
    return POWERTIP_KEYWORDS;
  }

  @Override
  protected List<Keyword> getBaseGameKeywordList() {
    return null;
  }

}