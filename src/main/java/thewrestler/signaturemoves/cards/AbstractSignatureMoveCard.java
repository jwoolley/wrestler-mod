package thewrestler.signaturemoves.cards;

import basemod.abstracts.CustomCard;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.enums.AbstractCardEnum;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

// TODO: implement getTooltips() method for explanatory tooltips
//  (and append any additional tooltips specified in the subclass)

// TODO: color text highlighting on cardnames, keywords

import static thewrestler.WrestlerMod.getCardResourcePath;

abstract public class AbstractSignatureMoveCard extends CustomCard {
  public static final CardColor COLOR = AbstractCardEnum.THE_WRESTLER_ORANGE;
  public static final CardRarity RARITY = CardRarity.SPECIAL;

  private static final String IMG_PATH_PREFIX = "signaturemoves/";
  private final String imgName;

  final Map<AbstractSignatureMoveUpgrade, Integer> upgrades;

  public AbstractSignatureMoveCard(String id, String name, String img, int cost, String rawDescription,
                                   CardType type, CardTarget target, boolean hasRetain) {
    this(id, name, img, cost, rawDescription, type, target, AbstractSignatureMoveUpgrade.NO_UPGRADES);
    this.retain = hasRetain;
  }

  public AbstractSignatureMoveCard(String id, String name, String img, int cost, String rawDescription,
                                   CardType type, CardTarget target, Map<AbstractSignatureMoveUpgrade, Integer> upgrades) {
    super(id, name, getCardResourcePath(IMG_PATH_PREFIX + img), cost, rawDescription, type, COLOR, RARITY, target);
    this.imgName = img;
    this.upgrades = new HashMap<>(upgrades);
    // apply upgrades — will need to utilize static helper methods for upgraded name, image, description, etc.
  }

  // TODO: override upgradeName() to infer name from random upgrade applied via overridden upgrade() call

  abstract protected void applyUpgrades(Map<AbstractSignatureMoveUpgrade, Integer> upgrades);

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    try {
      // TODO: does upgrades need to be cast / reconstructed?
      final Constructor<? extends AbstractSignatureMoveCard> constructor =
          this.getClass().getConstructor(String.class, String.class, String.class, Integer.class, String.class,
              CardType.class, CardTarget.class, Map.class);

      return constructor.newInstance(this.cardID, this.name, this.imgName, this.cost, this.rawDescription,
          this.type, this.target, this.upgrades) ;
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException("WrestlerMod failed to auto-generate makeCopy for AbstractSignatureMoveCard: " + this.cardID);
    }
  }

  abstract public String getIndefiniteCardName();
}
