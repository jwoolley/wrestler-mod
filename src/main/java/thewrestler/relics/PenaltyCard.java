package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import thewrestler.WrestlerMod;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class PenaltyCard extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("PenaltyCard");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("penaltycard.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("penaltycard.png"));
  private static final RelicTier RELIC_TIER = RelicTier.STARTER;

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(WrestlerMod.makeID("Unsporting"), WrestlerMod.makeID("PenaltyCard"));

  public static final int CARD_AMOUNT = 1;
  public static final int HP_LOSS_AMOUNT = 1;

  public PenaltyCard() {
    super(ID, IMG, OUTLINE, RELIC_TIER, LandingSound.FLAT);
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0]
        + (CARD_AMOUNT  == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2])
        + DESCRIPTIONS[3] + HP_LOSS_AMOUNT + DESCRIPTIONS[4];
  }

  public void atTurnStart() {
    if (SportsmanshipInfo.hasSportsmanshipInfo() && SportsmanshipInfo.isUnsporting()) {
      flash();
      AbstractPlayer player = AbstractDungeon.player;
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(CARD_AMOUNT));
      AbstractDungeon.actionManager.addToBottom(new LoseHPAction(player, player, HP_LOSS_AMOUNT));
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