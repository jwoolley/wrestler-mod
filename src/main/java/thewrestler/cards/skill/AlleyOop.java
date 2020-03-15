package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.MoveRandomCardsFromDrawPileToHandAction;
import thewrestler.enums.AbstractCardEnum;

import java.util.function.Predicate;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class AlleyOop extends CustomCard {
  public static final String ID = "WrestlerMod:AlleyOop";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "alleyoop.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  private static final int NUM_CARDS = 1;
  private static final int NUM_CARDS_UPGRADE = 1;
  private static final int MAX_CARD_COST = 1;

  public AlleyOop() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(NUM_CARDS, MAX_CARD_COST), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = NUM_CARDS;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DiscardAction(p, p, this.magicNumber, false));
    final Predicate<AbstractCard> predicate =  c -> c.cost <= MAX_CARD_COST && c.type == CardType.ATTACK;
    AbstractDungeon.actionManager.addToBottom(
        new MoveRandomCardsFromDrawPileToHandAction(this.magicNumber, predicate));
  }

  @Override
  public AbstractCard makeCopy() {
    return new AlleyOop();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_CARDS_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber, MAX_CARD_COST);
      initializeDescription();
    }
  }
  public static String getDescription(int amount, int maxCardCost) {
    final String cardOrCards = (amount == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1]);
    return DESCRIPTION + cardOrCards + EXTENDED_DESCRIPTION[2] + cardOrCards + EXTENDED_DESCRIPTION[3] + maxCardCost
      + EXTENDED_DESCRIPTION[4];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}