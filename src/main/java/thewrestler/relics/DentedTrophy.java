package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import thewrestler.WrestlerMod;
import thewrestler.actions.UpgradeRandomCardInDrawPileAction;
import thewrestler.util.TextureLoader;

import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class DentedTrophy extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("DentedTrophy");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("dentedtrophy.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("dentedtrophy.png"));


  public DentedTrophy() {
    super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0];
  }

  @Override
  public void atBattleStartPreDraw() {
    triggerRelicEffect();
  }

  private void triggerRelicEffect() {
    flash();
    AbstractDungeon.actionManager.addToTop(new SFXAction("DING_SIMPLE_1"));
    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    AbstractDungeon.actionManager.addToBottom(new UpgradeRandomCardInDrawPileAction());
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