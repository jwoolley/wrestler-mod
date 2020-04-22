package thewrestler.cards.debug;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.BluePenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.RedPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.YellowPenaltyStatusCard;
import thewrestler.enums.AbstractCardEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class DebugTrademarkMoveScreen extends CustomCard {
  public static final String ID = "WrestlerMod:DebugTrademarkMoveScreen";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "debug/opentrademarkmovescreen.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  public DebugTrademarkMoveScreen() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST,
        getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    final AbstractPenaltyStatusCard testSelectedCard = new BluePenaltyStatusCard();
    final List<AbstractPenaltyStatusCard> testAllPenaltyCards = Arrays.asList(new RedPenaltyStatusCard(), new YellowPenaltyStatusCard());
    AbstractDungeon.actionManager.addToBottom(new ChooseTrademarkMoveAction(testSelectedCard, testAllPenaltyCards));
  }

  private static class ChooseTrademarkMoveAction extends AbstractGameAction {
    private static final UIStrings uiStrings =
        CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("ComeCleanAction"));
    public static final String[] TEXT = uiStrings.TEXT;
    private static final float DURATION = Settings.ACTION_DUR_XLONG;

    private final AbstractPenaltyStatusCard selectedCard;
    private final List<AbstractPenaltyStatusCard> allPenaltyCards;
    private boolean cardSelectionFinished;

    public ChooseTrademarkMoveAction(AbstractPenaltyStatusCard selectedCard, List<AbstractPenaltyStatusCard> allPenaltyCards) {
      this.selectedCard = selectedCard;
      this.allPenaltyCards = Collections.unmodifiableList(allPenaltyCards);
      this.actionType = ActionType.CARD_MANIPULATION;
      this.duration = this.startDuration = DURATION;
      cardSelectionFinished = false;
    }

    public void update() {
      if (this.duration == this.startDuration) {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead() || allPenaltyCards.size() < 2) {
          this.isDone = true;
        } else {
          WrestlerMod.openTrademarkMoveSelectScreen();
        }
        tickDuration();
        return;
      }
      if (!WrestlerMod.getTrademarkMoveSelectScreen().isOpen()) {
        this.isDone = true;
      }
      tickDuration();
    }
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      initializeDescription();
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new DebugTrademarkMoveScreen();
  }

  public static String getDescription() {
    return DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}