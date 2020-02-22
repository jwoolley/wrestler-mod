package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.ChooseAndAddFilteredDiscardCardsToHandAction;
import thewrestler.actions.MoveRandomCardsFromDiscardToHandAction;
import thewrestler.enums.AbstractCardEnum;

import java.util.Arrays;
import java.util.function.Predicate;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class OffTheRopes extends CustomCard {
  public static final String ID = "WrestlerMod:OffTheRopes";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "offtheropes.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int BLOCK_AMOUNT = 6;
  private static final int COST = 1;

  public OffTheRopes() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(false), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));

    Predicate<AbstractCard> predicate =  c -> c.type == CardType.ATTACK;

    if (this.upgraded) {
      AbstractDungeon.actionManager.addToBottom(
          new ChooseAndAddFilteredDiscardCardsToHandAction(1,
              predicate, Arrays.copyOfRange(EXTENDED_DESCRIPTION, 3, 6), false));
    } else {
      AbstractDungeon.actionManager.addToBottom(
          new MoveRandomCardsFromDiscardToHandAction(1, predicate));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new OffTheRopes();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.rawDescription = getDescription(true);
      initializeDescription();
    }
  }
  public static String getDescription(boolean upgraded) {
    return DESCRIPTION
        + (!upgraded ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}