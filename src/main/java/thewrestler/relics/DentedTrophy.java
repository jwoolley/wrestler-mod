package thewrestler.relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.omg.CORBA.WCharSeqHelper;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.powers.BravadoPower;
import thewrestler.powers.DentedTrophyPower;
import thewrestler.util.CardUtil;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.CombatInfo;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.makeRelicOutlinePath;
import static thewrestler.WrestlerMod.makeRelicPath;

public class DentedTrophy extends CustomWrestlerRelic implements CustomSavable<Integer>  {
  public static final String ID = WrestlerMod.makeID("DentedTrophy");
  private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("dentedtrophy.png"));
  private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("dentedtrophy.png"));

  public final int MIN_ATTACKS = 2;
  private final int MIN_BRAVADO = 5;

  private static final List<String> POWERTIP_KEYWORDS = Arrays.asList(
      WrestlerMod.makeID("Bravado"),
      WrestlerMod.makeID("SignatureMove")
  );

  public static final int BRAVADO_AMOUNT = 1;

  public DentedTrophy() {
    super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    this.counter = 0;
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0] + MIN_ATTACKS+ DESCRIPTIONS[1] + MIN_BRAVADO + DESCRIPTIONS[2];
  }

  @Override
  public void atBattleStart() {
    AbstractPlayer player = AbstractDungeon.player;
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(player, player, new DentedTrophyPower(player, 0), 0));
  }

  @Override
  public void atTurnStart() {
    if (this.counter >= MIN_BRAVADO) {

      flash();
      // AbstractDungeon.actionManager.addToTop(new GainTradeMarkMoveAction());
      CardCrawlGame.sound.play("BOXING_BELL_1");

      WrestlerCharacter.getSignatureMoveInfo().triggerGainTrademarkMove();

      final AbstractPlayer player = AbstractDungeon.player;
      AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(player, this));


      this.counter -= MIN_BRAVADO;
    }
  }

  @Override
  public void onPlayCard(AbstractCard card, AbstractMonster monster) {
    if (card.type == AbstractCard.CardType.ATTACK) {
      AbstractPlayer player = AbstractDungeon.player;
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(player, player, new DentedTrophyPower(player, BRAVADO_AMOUNT), BRAVADO_AMOUNT));
    }
  }

  @Override
  public void onPlayerEndTurn() {
    if (CombatInfo.getNumAttacksPlayed() >= MIN_ATTACKS) {
      flash();
      this.counter++;
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

  @Override
  public Integer onSave() {
    return this.counter;
  }

  @Override
  public void onLoad(Integer savedAmount) {
    if (savedAmount != null) {
      this.counter = savedAmount;
    }
  }
}