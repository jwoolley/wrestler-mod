package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.WrestlerMod;
import thewrestler.util.TextureLoader;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class BrutesTrophy extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("BrutesTrophy");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("brutestrophy.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("brutestrophy.png"));

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(/*WrestlerMod.makeID("Disliked"))*/);

  public static final int STRENGTH_AMOUNT = 2;

  public BrutesTrophy() {
    super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0] + STRENGTH_AMOUNT + DESCRIPTIONS[1];
  }

  public void atBattleStart() {
      flash();
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
              new StrengthPower(AbstractDungeon.player, STRENGTH_AMOUNT), STRENGTH_AMOUNT));
      AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
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