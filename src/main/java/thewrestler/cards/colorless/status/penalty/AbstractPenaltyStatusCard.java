package thewrestler.cards.colorless.status.penalty;

import basemod.abstracts.CustomCard;
import thewrestler.cards.WrestlerCardTags;

import static thewrestler.WrestlerMod.getCardResourcePath;

public abstract class AbstractPenaltyStatusCard extends CustomCard {
  private static final CardType TYPE = CardType.STATUS;
  private static final CardRarity RARITY = CardRarity.SPECIAL;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final String IMG_PATH_PREFIX = "penaltycards/";

  public AbstractPenaltyStatusCard(String id, String name, String imgPath, String description) {
    super(id, name, imgPath,1, description, TYPE, CardColor.COLORLESS, RARITY, TARGET);
    this.tags.add(WrestlerCardTags.PENALTY);
  }

  public abstract void triggerOnCardGained();

  static String getPenaltyCardImgPath(String imgFilename) {
    return getCardResourcePath(IMG_PATH_PREFIX + imgFilename);
  }
}