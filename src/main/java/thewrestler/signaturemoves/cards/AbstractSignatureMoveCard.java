package thewrestler.signaturemoves.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import org.omg.CORBA.WCharSeqHelper;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.BluePenaltyStatusCard;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.signaturemoves.upgrades.*;
import thewrestler.WrestlerMod;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static thewrestler.WrestlerMod.getCardResourcePath;

// TODO: implement getTooltips() method for explanatory tooltips
//  (and append any additional tooltips specified in the subclass)
// TODO: color text highlighting on cardnames, thewrestler.keywords

abstract public class AbstractSignatureMoveCard extends CustomCard {
  // TODO: add effects on the info panel, etc. for full trigger (with card gain) and partial trigger (for multi-step
  //  conditions)

  public static final CardColor COLOR = AbstractCardEnum.THE_WRESTLER_ORANGE;
  public static final CardRarity RARITY = CardRarity.SPECIAL;

  private static final String IMG_PATH_PREFIX = "signaturemoves/";
  private final String imgName;

  final protected SignatureMoveUpgradeList upgradeList;

  final Class<? extends AbstractPenaltyStatusCard> comboClass1;
  final Class<? extends AbstractPenaltyStatusCard> comboClass2;

  public AbstractSignatureMoveCard(String id, String name, String img, int cost, String rawDescription,
                                   CardType type, CardTarget target, boolean hasExhaust, boolean hasRetain,
                                   Class<? extends AbstractPenaltyStatusCard> comboClass1,
                                   Class<? extends AbstractPenaltyStatusCard> comboClass2) {
    this(id, name, img, cost, rawDescription, type, target, SignatureMoveUpgradeList.NO_UPGRADES,
        comboClass1, comboClass2);
    this.exhaust = hasExhaust;
    this.selfRetain = hasRetain;
  }

  private AbstractSignatureMoveCard(String id, String name, String img, int cost, String rawDescription,
                                    CardType type, CardTarget target, SignatureMoveUpgradeList upgradeList,
                                    Class<? extends AbstractPenaltyStatusCard> comboClass1,
                                    Class<? extends AbstractPenaltyStatusCard> comboClass2) {
    super(id, name, getCardResourcePath(IMG_PATH_PREFIX + img), cost, rawDescription, type, COLOR, RARITY, target);
    this.imgName = img;
    this.upgradeList = new SignatureMoveUpgradeList();
    applyUpgrades(upgradeList);
    this.comboClass1 = comboClass1;
    this.comboClass2 = comboClass2;
    // apply upgrades — will need to utilize static helper methods for upgraded name, image, description, etc.
  }

  // TODO: override upgradeName() to infer name from random upgrade applied via overridden upgrade() call

  abstract public void applyUpgrades(List<AbstractSignatureMoveUpgrade> upgradeList);

  public void applyUpgrade(AbstractSignatureMoveUpgrade upgrade) {
    this.applyUpgrades(Collections.singletonList(upgrade));
  }

  void applyUpgrades(SignatureMoveUpgradeList upgradeList) {
    if (WrestlerCharacter.hasSignatureMoveInfo()) {
      // TODO: this should live in the info-but that means all calls to apply upgrades need to go through the info, not here
    }
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

  public boolean matchCombo(AbstractPenaltyStatusCard card1, AbstractPenaltyStatusCard card2) {
    return comboClass1.isInstance(card1) && comboClass2.isInstance(card2)
        || comboClass1.isInstance(card2) && comboClass2.isInstance(card1);
  }

  static private List<AbstractSignatureMoveUpgrade> selectRandomUpgrades(UpgradeGroup allPossibleUpgrades, UpgradeGroup currentUpgrades, int numUpgrades) {
    // TODO: introduce rarity calculation / application
    final List<AbstractSignatureMoveUpgrade> possibleUpgrades = getPossibleUpgrades(allPossibleUpgrades, currentUpgrades);
    Collections.shuffle(possibleUpgrades);
    return possibleUpgrades.subList(0, Math.min(numUpgrades, possibleUpgrades.size()));
  }

  static private List<AbstractSignatureMoveUpgrade> getPossibleUpgrades(UpgradeGroup allPossibleUpgrades, UpgradeGroup currentUpgrades) {
    final Map<UpgradeType, Integer> allCommon = new HashMap<>();
    allPossibleUpgrades.keySet().stream()
        .filter(upgrade -> upgrade.rarity == UpgradeRarity.COMMON)
        .forEach(upgrade ->  allCommon.put(upgrade.type, allPossibleUpgrades.get(upgrade)));

    final Map<UpgradeType, Integer> currentCommon = new HashMap<>();
    currentUpgrades.keySet().stream()
        .filter(upgrade -> upgrade.rarity == UpgradeRarity.COMMON )
        .forEach(upgrade ->  currentCommon.put(upgrade.type, currentUpgrades.get(upgrade)));

    final Map<UpgradeType, Integer> allRare = new HashMap<>();
    allPossibleUpgrades.keySet().stream()
        .filter(upgrade -> upgrade.rarity == UpgradeRarity.RARE)
        .forEach(upgrade ->  allRare.put(upgrade.type, allPossibleUpgrades.get(upgrade)));

    final Map<UpgradeType, Integer> currentRare = new HashMap<>();
    currentUpgrades.keySet().stream()
        .filter(upgrade -> upgrade.rarity == UpgradeRarity.RARE)
        .forEach(upgrade ->  currentRare.put(upgrade.type, currentUpgrades.get(upgrade)));

    List<AbstractSignatureMoveUpgrade> availableUpgrades = new ArrayList<>();

    availableUpgrades.addAll(allCommon.keySet().stream()
      .filter(type -> !currentCommon.containsKey(type) || currentCommon.get(type) < allCommon.get(type))
      .map(type -> new AbstractSignatureMoveUpgrade(type, 1, UpgradeRarity.COMMON))
      .collect(Collectors.toList()));

    availableUpgrades.addAll(allRare.keySet().stream()
        .filter(type -> !allRare.containsKey(type) || currentRare.get(type) < allRare.get(type))
        .map(type -> new AbstractSignatureMoveUpgrade(type, 1, UpgradeRarity.RARE))
        .collect(Collectors.toList()));

    return availableUpgrades;

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

  static final float BASELINE_TITLE_FONT_SIZE = 23.0f;
  static final Map<Integer, Float> sizeFactorMap = new HashMap<>();
  static {
    sizeFactorMap.put(24, 0.9f);
    sizeFactorMap.put(36, 0.8f);
    sizeFactorMap.put(48, 0.7f);
  }

  @Override
  public float getTitleFontSize() {
    float sizeFactor = 1.0f;

    for(Integer minLength : sizeFactorMap.keySet()) {
      if (this.name.length() >= minLength && sizeFactor > sizeFactorMap.get(minLength)) {
        sizeFactor = sizeFactorMap.get(minLength);
      }
    }

    if (sizeFactor == 1.0f) {
      return super.getTitleFontSize();
    }
    return sizeFactor * BASELINE_TITLE_FONT_SIZE;
  }

  final
  protected void upgradeName(List<AbstractSignatureMoveUpgrade> upgrades) {
    final UpgradeGroup group = getUpgradeGroup(upgrades);

    String newName = originalName;

    for (AbstractSignatureMoveUpgrade upgrade : upgrades) {
      final int count = group.get(upgrade);
      if (upgrade.type == UpgradeType.COST_REDUCTION) {
        if (count == 1) {
          newName = UPGRADE_NAMES_1[2] + " " + newName;
        } else if (count == 2)  {
          newName = UPGRADE_NAMES_1[3] + " " + newName;
        } else if (count == 3)  {
          newName = UPGRADE_NAMES_1[4] + " " + newName;
        }
      } else if (upgrade.type == UpgradeType.RETAIN) {
        newName = UPGRADE_NAMES_1[9] + " " + newName;
      } else if (upgrade.type == UpgradeType.LOSE_EXHAUST) {
        newName = UPGRADE_NAMES_1[5] + " " + newName;
      }
      else if (upgrade.type == UpgradeType.DAMAGE) {
        if (upgrade.rarity == UpgradeRarity.COMMON) {
          for (int i = 0; i < count; i++) {
            newName += "+";
          }
        }
      }
    }

    this.name = newName;
    initializeTitle();
  }


  public static final String[] UPGRADE_NAMES_1 = CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("TrademarkMoveNames_1")).TEXT;
  public static final String[] UPGRADE_NAMES_2 = CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("TrademarkMoveNames_2")).TEXT;
  public static final String[] UPGRADE_NAMES_3 = CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("TrademarkMoveNames_3")).TEXT;
  public static final String[] UPGRADE_NAMES_4 = CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("TrademarkMoveNames_4")).TEXT;
  public static final String[] UPGRADE_NAMES_5 = CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("TrademarkMoveNames_5")).TEXT;
  public static final String[] UPGRADE_NAMES_6 = CardCrawlGame.languagePack.getUIString(WrestlerMod.makeID("TrademarkMoveNames_6")).TEXT;

  abstract public String getIndefiniteCardName();
}
