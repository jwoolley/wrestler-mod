package thewrestler.signaturemoves.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.effects.utils.combat.CleanFinishEffect;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DragonGate extends AbstractSignatureMoveCard {
  public static final String ID = "WrestlerMod:DragonGate";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "dragongate.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardTarget TARGET = AbstractCard.CardTarget.NONE;

  private static final int COST = 1;
  private static final int NUM_CARDS = 3;
  private static final boolean HAS_RETAIN = true;


  public DragonGate() {
    super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, TARGET, HAS_RETAIN);
    this.baseMagicNumber = this.magicNumber = NUM_CARDS;;
  }

  private static class DragonGateAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_XFAST;
    private final int numCards;

    final AbstractPlayer player;

    DragonGateAction(int numCards) {
      this.duration = DURATION;
      this.actionType = ActionType.DAMAGE;
      this.numCards = numCards;
      this.player = AbstractDungeon.player;

    }

    @Override
    public void update() {
      if (this.duration <= DURATION) {

      }
      if (this.duration <= 0.1f) {
        if (AbstractDungeon.player.hand.size() == 10) {
          AbstractDungeon.player.createHandIsFullDialog();
          this.isDone = true;
          return;
        } if (player.exhaustPile.isEmpty()) {
          this.isDone = true;
          return;
        }
        List<AbstractCard> cards = new ArrayList();

        cards.addAll(player.exhaustPile.group.stream()
            .filter(c -> c.type != CardType.SKILL).collect(Collectors.toList()));

        if (cards.size() > this.numCards) {
          Collections.shuffle(cards);
          cards = cards.subList(0, this.numCards);
        }

//        for (AbstractCard card : cards) {
//          if (card.cost > 0) {
//            card.modifyCostForCombat(- 1);
//          }
//          AbstractDungeon.actionManager.addToBottom(new ExhaustToHandAction(card));
//        }

        this.isDone = true;
      }
      tickDuration();
    }
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    // TODO: splashy dragon gate effect (two transparent dragons + sinister noise?)

    AbstractDungeon.actionManager.addToTop(
        new VFXAction(new CleanFinishEffect(Color.GOLDENROD.cpy(), "GONG_STRIKE_2", Settings.ACTION_DUR_XFAST)));

    AbstractDungeon.actionManager.addToBottom(
        new DragonGateAction(this.magicNumber));
  }

  @Override
  public void applyUpgrades(SignatureMoveUpgradeList upgradeList) {
  }

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    return new DragonGate();
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