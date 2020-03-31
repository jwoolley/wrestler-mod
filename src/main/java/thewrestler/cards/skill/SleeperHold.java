package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;
import thewrestler.enums.AbstractCardEnum;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class SleeperHold extends CustomCard {
  public static final String ID = WrestlerMod.makeID("SleeperHold");

  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "sleeperhold.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 2;
  private static final int NUM_CARDS = 1;
  private static final int NUM_CARDS_UPGRADE = 1;
  private static final int COST_REDUCTION = 1;

  public SleeperHold() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(NUM_CARDS), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.magicNumber = this.baseMagicNumber = COST_REDUCTION;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new ApplyGrappledAction(m, p));

    List<AbstractCard> eligibleCards = AbstractDungeon.player.hand.group.stream()
        .filter(c -> c.type != CardType.ATTACK && c.costForTurn > 0 && c.uuid != this.uuid)
        .collect(Collectors.toList());

    if (!eligibleCards.isEmpty()) {
      Collections.shuffle(eligibleCards);
      eligibleCards.stream()
          .limit(this.magicNumber)
          .forEach(c -> {
            c.setCostForTurn(c.cost - COST_REDUCTION);
            c.superFlash(Color.TEAL);
          });
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new SleeperHold();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_CARDS_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber);
      initializeDescription();
    }
  }

  public static String getDescription(int numCards) {
    return DESCRIPTION
        + (numCards == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2] + COST_REDUCTION + EXTENDED_DESCRIPTION[3];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}