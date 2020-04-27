package thewrestler.signaturemoves.cards.skill;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
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

public class ChopBlock extends AbstractSignatureMoveCard {
  public static final String ID = "WrestlerMod:ChopBlock";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "chopblock.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardTarget TARGET = AbstractCard.CardTarget.NONE;

  private static final int COST = 1;
  private static final int NUM_CARDS = 3;
  private static final boolean HAS_EXHAUST = false;
  private static final boolean HAS_RETAIN = false;


  public ChopBlock() {
    super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, TARGET, HAS_EXHAUST, HAS_RETAIN);
    this.baseMagicNumber = this.magicNumber = NUM_CARDS;
  }

  private static class ChopBlockAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    final AbstractPlayer player;

    boolean hasDrawn;

    ChopBlockAction(int numCards) {
      this.duration = DURATION;
      this.actionType = ActionType.CARD_MANIPULATION;
      this.amount = numCards;
      this.player = AbstractDungeon.player;
    }

    class GainBlockForHandCostAction extends AbstractGameAction {
      @Override
      public void update() {
        final int totalHandCost = AbstractDungeon.player.hand.group.stream()
            .mapToInt(c -> c.costForTurn)
            .sum();
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, totalHandCost));
        this.isDone = true;
      }
    }

    @Override
    public void update() {
      if (this.duration <= DURATION) {
        AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.amount, new GainBlockForHandCostAction()));

        AbstractDungeon.actionManager.addToTop(new SFXAction("CHOP_WOOD_1"));

        AbstractDungeon.actionManager.addToTop(new ShakeScreenAction(0.0F,
            ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
        this.isDone = true;
      }
      tickDuration();
    }
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    // TODO: splashy dragon gate effect (two transparent dragons + sinister noise?)
    AbstractDungeon.actionManager.addToBottom(new ChopBlockAction(this.magicNumber));
  }

  @Override
  public void applyUpgrades(List<AbstractSignatureMoveUpgrade> upgradesToApply) {
  }

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    return new ChopBlock();
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