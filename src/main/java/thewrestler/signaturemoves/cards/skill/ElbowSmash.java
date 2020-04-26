package thewrestler.signaturemoves.cards.skill;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javafx.util.Pair;
import thewrestler.cards.colorless.attack.Elbow;
import thewrestler.effects.utils.combat.CleanFinishEffect;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.UpgradeGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ElbowSmash extends AbstractSignatureMoveCard {
  public static final String ID = "WrestlerMod:ElbowSmash";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "elbowsmash.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardTarget TARGET = AbstractCard.CardTarget.NONE;

  private static final int COST = 1;
  private static final int NUM_CARDS = 3;
  private static final boolean HAS_EXHAUST = true;
  private static final boolean HAS_RETAIN = true;


  public ElbowSmash() {
    super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, TARGET, HAS_EXHAUST, HAS_RETAIN);
    this.cardsToPreview = new Elbow();
  }

  private static class ElbowSmashAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    final AbstractPlayer player;

    ElbowSmashAction() {
      this.duration = DURATION;
      this.actionType = ActionType.CARD_MANIPULATION;
      this.player = AbstractDungeon.player;
    }

    @Override
    public void update() {
      if (this.duration <= DURATION) {
        CardCrawlGame.sound.play("SPRINGBOARD_1");
        if (!this.player.hand.isEmpty()) {
          final int numCards = AbstractDungeon.player.hand.size();
          AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(new Elbow(), numCards));
          AbstractDungeon.actionManager.addToTop(new DiscardAction(this.player, this.player, numCards, true));

        }
        this.isDone = true;
        return;
      }
      tickDuration();
    }
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    // TODO: splashy dragon gate effect (two transparent dragons + sinister noise?)
    AbstractDungeon.actionManager.addToBottom(new ElbowSmashAction());
  }

  @Override
  public void applyUpgrades(List<AbstractSignatureMoveUpgrade> upgradesToApply) {
  }

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    return new ElbowSmash();
  }

  @Override
  public UpgradeGroup getAllEligibleUpgrades() {
    // TODO: implement
    return null;
  }

  @Override
  public UpgradeGroup getCurrentEligibleUpgrades() {
    // TODO: implement
    return null;
  }

  @Override
  public String getIndefiniteCardName() {
    return EXTENDED_DESCRIPTION[0] + this.name;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(1); // TODO: remove this once upgrade system is in place
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}