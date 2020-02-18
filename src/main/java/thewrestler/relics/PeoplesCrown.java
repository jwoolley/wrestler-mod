package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.powers.DexterityPower;
import thewrestler.WrestlerMod;
import thewrestler.util.TextureLoader;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class PeoplesCrown extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("PeoplesCrown");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("peoplescrown.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("peoplescrown.png"));

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(/*WrestlerMod.makeID("Liked")*/);

  public static final int DEX_AMOUNT = 2;

  public PeoplesCrown() {
    super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0] + DEX_AMOUNT + DESCRIPTIONS[1];
  }

  public void atBattleStart() {
    flash();
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
            new DexterityPower(AbstractDungeon.player, DEX_AMOUNT), DEX_AMOUNT));

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
