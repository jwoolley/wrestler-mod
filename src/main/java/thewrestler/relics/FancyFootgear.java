package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.powers.TemporaryBufferPower;
import thewrestler.util.TextureLoader;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class FancyFootgear extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("FancyFootgear");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("fancyfootgear.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("fancyfootgear.png"));
  private static final RelicTier RELIC_TIER = RelicTier.RARE;

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(WrestlerMod.makeID("TemporaryBuffer"));

  public static final int BUFFER_AMOUNT = 1;

  public FancyFootgear() {
    super(ID, IMG, OUTLINE, RELIC_TIER, LandingSound.FLAT);
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0];
  }

  public void onMonsterDeath(AbstractMonster m) {
    if ((m.currentHealth == 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
      flash();
      AbstractPlayer player = AbstractDungeon.player;
      AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(m, this));
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(player, player, new TemporaryBufferPower(player, BUFFER_AMOUNT), BUFFER_AMOUNT));
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
}