package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.WrestlerMod;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.BasicUtils;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.approval.ApprovalInfo;

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
    if (ApprovalInfo.hasApprovalInfo() && ApprovalInfo.isUnpopular()) {
      flash();
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(CARD_AMOUNT));
    }
  }

  @Override
  public boolean canSpawn() {
    return ApprovalInfo.hasApprovalInfo() && !AbstractDungeon.player.hasRelic(LuckyTrunks.ID);
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