package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.actions.UpgradeRandomCardInDrawPileAction;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.skill.AbstractPenaltyCardListener;
import thewrestler.util.TextureLoader;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class DentedTrophy extends CustomWrestlerRelic implements AbstractPenaltyCardListener {
  public static final String ID = WrestlerMod.makeID("DentedTrophy");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("dentedtrophy.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("dentedtrophy.png"));

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(WrestlerMod.makeID("PenaltyCard"));

  public DentedTrophy() {
    super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    this.counter = -1;
  }

  private final int STARTING_HEAL = 6;
  private final int HEAL_REDUCTION = 2;

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0] + STARTING_HEAL + DESCRIPTIONS[1] + HEAL_REDUCTION + DESCRIPTIONS[2];
  }

  @Override
  public void atBattleStart() {
    flash();
    this.counter = STARTING_HEAL;
  }

  public void onVictory() {
    flash();
    if (AbstractDungeon.player.currentHealth > 0 && this.counter > 0) {
      AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
      AbstractDungeon.player.heal(this.counter);
    }
    this.counter = -1;
  }

  @Override
  protected List<String> getKeywordList() {
    return POWERTIP_KEYWORDS;
  }

  @Override
  protected List<Keyword> getBaseGameKeywordList() {
    return null;
  }

  @Override
  public void onGainedWarningCard() {

  }

  @Override
  public void onGainedPenaltyCard(AbstractPenaltyStatusCard card) {
    if (this.counter > 0) {
      flash();
      this.counter -= HEAL_REDUCTION;
    }
  }
}