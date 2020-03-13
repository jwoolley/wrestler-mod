package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.WrestlerMod;
import thewrestler.powers.BravadoPower;
import thewrestler.util.TextureLoader;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class DentedTrophy extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("DentedTrophy");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("dentedtrophy.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("dentedtrophy.png"));

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(
      WrestlerMod.makeID("Bravado"),
      WrestlerMod.makeID("SignatureMove")
  );

  public static final int BRAVADO_AMOUNT = 1;

  public DentedTrophy() {
    super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0] + BRAVADO_AMOUNT + DESCRIPTIONS[1];
  }

  @Override
  public void atTurnStart() {
    AbstractPlayer player = AbstractDungeon.player;
    flash();
    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(player, this));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(player, player, new BravadoPower(player, BRAVADO_AMOUNT), BRAVADO_AMOUNT));
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