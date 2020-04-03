package thewrestler.signaturemoves.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.UpgradeGroup;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO: implement getTooltips() method for explanatory tooltips
//  (and append any additional tooltips specified in the subclass)

// TODO: color text highlighting on cardnames, thewrestler.keywords

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
                                   CardType type, CardTarget target, boolean hasExhaust, boolean hasRetain) {
    this(id, name, img, cost, rawDescription, type, target, SignatureMoveUpgradeList.NO_UPGRADES);
    this.exhaust = hasExhaust;
    this.selfRetain = hasRetain;
  }

  private AbstractSignatureMoveCard(String id, String name, String img, int cost, String rawDescription,
                                   CardType type, CardTarget target, SignatureMoveUpgradeList upgradeList) {
    super(id, name, getCardResourcePath(IMG_PATH_PREFIX + img), cost, rawDescription, type, COLOR, RARITY, target);
    this.imgName = img;
    this.upgradeList = new SignatureMoveUpgradeList();
    applyUpgrades(upgradeList);
    // apply upgrades — will need to utilize static helper methods for upgraded name, image, description, etc.
  }

  // TODO: override upgradeName() to infer name from random upgrade applied via overridden upgrade() call

  abstract public void applyUpgrades(List<AbstractSignatureMoveUpgrade> upgradeList);

  public void applyUpgrade(AbstractSignatureMoveUpgrade upgrade) {
    this.applyUpgrades(Collections.singletonList(upgrade));
  }

  public void applyUpgrades(SignatureMoveUpgradeList upgradeList) {
    this.applyUpgrades(new ArrayList<>(upgradeList));
  }

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

  private Function<Void,Void> upgradeSelectedCallback = aVoid -> null;

  public void setUpgradeSelectedCallback(Function<Void, Void> callback) {
    this.upgradeSelectedCallback = callback;
  }

  public void onChoseThisOption() {
    upgradeSelectedCallback.apply(null);
  }

  public abstract UpgradeGroup getAllEligibleUpgrades();

  public abstract UpgradeGroup getCurrentEligibleUpgrades();

  public AbstractCard makeStatEquivalentCopy() {
    AbstractSignatureMoveCard copy = (AbstractSignatureMoveCard)super.makeStatEquivalentCopy();
    copy.applyUpgrades(this.upgradeList);
    return copy;
  }

  public void reinitialize() {
    this.initializeTitle();
    this.initializeDescription();
  }
  public void upgradeCost(int newCost) {
    this.upgradeBaseCost(newCost);
  }

  static private List<AbstractSignatureMoveUpgrade> selectRandomUpgrades(AbstractSignatureMoveCard card, int numUpgrades) {
    return selectRandomUpgrades(card.getAllEligibleUpgrades(), getUpgradeGroup(card.upgradeList), numUpgrades);
  }


  static private List<AbstractSignatureMoveUpgrade> selectRandomUpgrades(UpgradeGroup allPossibleUpgrades, UpgradeGroup currentUpgrades, int numUpgrades) {
    // TODO: introduce rarity calculation / application
    final List<AbstractSignatureMoveUpgrade> possibleUpgrades = getPossibleUpgrades(allPossibleUpgrades, currentUpgrades);
    Collections.shuffle(possibleUpgrades);
    return possibleUpgrades.subList(0, Math.min(numUpgrades, possibleUpgrades.size()));
  }

  static private List<AbstractSignatureMoveUpgrade> getPossibleUpgrades(UpgradeGroup allPossibleUpgrades, UpgradeGroup currentUpgrades) {
    return allPossibleUpgrades.keySet().stream()
        .filter(u -> !currentUpgrades.containsKey(u) || currentUpgrades.get(u) < allPossibleUpgrades.get(u)).collect(Collectors.toList());
  }

  private static UpgradeGroup getUpgradeGroup(List<AbstractSignatureMoveUpgrade> upgrades) {
    UpgradeGroup group = new UpgradeGroup();
    for (AbstractSignatureMoveUpgrade upgrade : upgrades) {
        group.put(upgrade, group.containsKey(upgrade) ? group.get(upgrade) + 1 : 1);
    }

    return group;
  }

  public List<AbstractSignatureMoveUpgrade> selectRandomUpgrades(int amount) {
    return selectRandomUpgrades(this, amount);
  }

  public List<AbstractSignatureMoveUpgrade> getPossibleUpgrades() {
    UpgradeGroup currentUpgrades = getUpgradeGroup(this.upgradeList);
    return this.getAllEligibleUpgrades().keySet().stream()
        .filter(u -> !currentUpgrades.containsKey(u) || currentUpgrades.get(u) <
            this.getAllEligibleUpgrades().get(u)).collect(Collectors.toList());
  }

  public int getNumPossibleUpgrades(UpgradeGroup allPossibleUpgrades, UpgradeGroup currentUpgrades) {
    return getPossibleUpgrades(allPossibleUpgrades, currentUpgrades).size();
  }


  abstract public String getIndefiniteCardName();
}
