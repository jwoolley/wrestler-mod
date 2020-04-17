package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.enums.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class ComeClean extends CustomCard {
  public static final String ID = "WrestlerMod:ComeClean";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "comeclean.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  private static final int NUM_CARDS_DRAWN = 1;
  private static final int NUM_CARDS_DRAWN_UPGRADE = 1;
  private static final int BONUS_CARDS_DRAWN = 1;

  public ComeClean() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST,
        getDescription(NUM_CARDS_DRAWN, BONUS_CARDS_DRAWN), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = NUM_CARDS_DRAWN;
    this.misc = BONUS_CARDS_DRAWN;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new ComeCleanAction(this.magicNumber, this.misc));
  }

  private static class ComeCleanAction extends AbstractGameAction {
    private static final UIStrings uiStrings =
        CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("ComeCleanAction"));
    public static final String[] TEXT = uiStrings.TEXT;
    private AbstractPlayer player;
    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    private AbstractCard selectedCard;
    private boolean cardSelectionFinished;
    private final int numBonusCards;

    public ComeCleanAction(int numCards, int numBonusCards) {
      this.source = this.target = this.player = AbstractDungeon.player;
      this.actionType = AbstractGameAction.ActionType.DISCARD;
      this.duration = DURATION;
      this.amount = numCards;
      this.numBonusCards = numBonusCards;
    }


    private final List<AbstractCard> discardedCards = new ArrayList<>();

    public void update() {
      if (this.duration == DURATION) {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
          this.isDone = true;
          return;
        }
        if (this.player.hand.size() <= this.amount) {
          this.amount = this.player.hand.size();
          discardedCards.addAll(this.player.hand.group);
          for (int i = 0; i < this.amount; i++) {
            AbstractCard c = this.player.hand.getTopCard();
            selectedCard = c;
            this.player.hand.moveToDiscardPile(c);
            c.triggerOnManualDiscard();
            GameActionManager.incrementDiscard(false);
          }
          cardSelectionFinished = true;
        } else {
          AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false);
        }
        AbstractDungeon.player.hand.applyPowers();
        tickDuration();
        return;
      } else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
        discardedCards.addAll(AbstractDungeon.handCardSelectScreen.selectedCards.group);
        for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
          cardSelectionFinished = true;
          this.player.hand.moveToDiscardPile(c);
          c.triggerOnManualDiscard();
          GameActionManager.incrementDiscard(false);
        }
        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        tickDuration();
        return;
      } else if (cardSelectionFinished) {
        int numCardsToDraw = this.amount;

        if (discardedCards.stream()
            .anyMatch(c-> (c.hasTag(WrestlerCardTags.DIRTY)|| c.hasTag(WrestlerCardTags.PENALTY)))) {
          numCardsToDraw += this.numBonusCards;
        }
        if (numCardsToDraw > 0) {
          AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.player, numCardsToDraw));
        }
        this.isDone = true;
        this.tickDuration();
        return;
      }
      tickDuration();
    }
  }


  @Override
  public AbstractCard makeCopy() {
    return new ComeClean();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_CARDS_DRAWN_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber, this.misc);
      initializeDescription();
    }
  }
  public static String getDescription(int numCards, int bonusCards) {
    return DESCRIPTION
        + (numCards == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2]
        + (numCards == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[3] + (numCards + bonusCards) + EXTENDED_DESCRIPTION[4];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}