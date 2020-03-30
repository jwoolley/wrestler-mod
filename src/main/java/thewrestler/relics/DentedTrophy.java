package thewrestler.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.powers.BravadoPower;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.CombatInfo;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class DentedTrophy extends CustomWrestlerRelic {
  public static final String ID = WrestlerMod.makeID("DentedTrophy");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("dentedtrophy.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("dentedtrophy.png"));

  public final int MIN_ATTACKS = 3;

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(
      WrestlerMod.makeID("Bravado"),
      WrestlerMod.makeID("SignatureMove")
  );

  public DentedTrophy() {
    super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    this.counter = 0;
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0] + MIN_ATTACKS + DESCRIPTIONS[1];
  }

  @Override
  public void atTurnStart() {
    this.counter = 0;
    triggerGainPower();
  }

  @Override
  public void onPlayCard(AbstractCard card, AbstractMonster monster) {
    if (card.type == AbstractCard.CardType.ATTACK) {
      this.counter++;
      if (this.counter >= MIN_ATTACKS) {
        CardCrawlGame.sound.play("BOXING_BELL_DOUBLE_1");
        triggerGainPower();
        this.counter -= MIN_ATTACKS;
      }
    }
  }

  private void triggerGainPower() {
    flash();
    AbstractPlayer player = AbstractDungeon.player;
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(player, player, new BravadoPower(player, 1), 1));
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