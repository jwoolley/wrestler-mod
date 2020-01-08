package thewrestler.signaturemoves.cards;

import basemod.abstracts.CustomCard;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

// TODO: implement getTooltips() method for explanatory tooltips
//  (and append any additional tooltips specified in the subclass)

// TODO: color text highlighting on cardnames, keywords

import static thewrestler.WrestlerMod.getCardResourcePath;

abstract public class AbstractSignatureMoveCard extends CustomCard {
  // TODO: add effects on the info panel, etc. for full trigger (with card gain) and partial trigger (for multi-step
  //  conditions)

  public static final CardColor COLOR = AbstractCardEnum.THE_WRESTLER_ORANGE;
  public static final CardRarity RARITY = CardRarity.SPECIAL;

  private static final String IMG_PATH_PREFIX = "signaturemoves/";
  private final String imgName;

  final SignatureMoveUpgradeList upgradeList;

  public AbstractSignatureMoveCard(String id, String name, String img, int cost, String rawDescription,
                                   CardType type, CardTarget target, boolean hasRetain) {
    this(id, name, img, cost, rawDescription, type, target, SignatureMoveUpgradeList.NO_UPGRADES);
    this.selfRetain = hasRetain;
  }

  public AbstractSignatureMoveCard(String id, String name, String img, int cost, String rawDescription,
                                   CardType type, CardTarget target, SignatureMoveUpgradeList upgradeList) {
    super(id, name, getCardResourcePath(IMG_PATH_PREFIX + img), cost, rawDescription, type, COLOR, RARITY, target);
    this.imgName = img;
    this.upgradeList = upgradeList;
    // apply upgrades — will need to utilize static helper methods for upgraded name, image, description, etc.
  }

  // TODO: override upgradeName() to infer name from random upgrade applied via overridden upgrade() call

  abstract public void applyUpgrades(SignatureMoveUpgradeList upgradeList);

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    try {
      // TODO: does upgrades need to be cast / reconstructed?
      final Constructor<? extends AbstractSignatureMoveCard> constructor =
          this.getClass().getConstructor(String.class, String.class, String.class, Integer.class, String.class,
              CardType.class, CardTarget.class, SignatureMoveUpgradeList.class);

      return constructor.newInstance(this.cardID, this.name, this.imgName, this.cost, this.rawDescription,
          this.type, this.target, this.upgradeList) ;
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException("WrestlerMod failed to auto-generate makeCopy for AbstractSignatureMoveCard: " + this.cardID);
    }
  }



  abstract public String getIndefiniteCardName();
}
