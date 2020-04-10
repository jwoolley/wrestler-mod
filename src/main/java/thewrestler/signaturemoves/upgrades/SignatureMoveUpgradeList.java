package thewrestler.signaturemoves.upgrades;

import thewrestler.WrestlerMod;

import java.util.ArrayList;

public class SignatureMoveUpgradeList extends ArrayList<AbstractSignatureMoveUpgrade> {
  public static final SignatureMoveUpgradeList NO_UPGRADES = new SignatureMoveUpgradeList();

  public SignatureMoveUpgradeList() {
    super();
  }

  public SignatureMoveUpgradeList(SignatureMoveUpgradeList upgradeList) {
    super(upgradeList);
  }

  private static final String DELIMITER = ";";
  private static final String SEPARATOR = ",";


  public String getSerializedUpgradeList() {
    String serializedList = "";

    if (this != NO_UPGRADES && !this.isEmpty()) {
      // TODO: encode rarity (as a third value) to seralized entry
      for (AbstractSignatureMoveUpgrade upgrade : this) {
        if (serializedList.length() > 0) {
          serializedList += DELIMITER;
        }
        serializedList += upgrade.type.ordinal() + SEPARATOR + Math.abs(upgrade.numUpgrades) + SEPARATOR + upgrade.rarity.ordinal();
      }
    }
    return serializedList;
  }

  public static SignatureMoveUpgradeList listFromSerializedData(String serializedList) {
    if (serializedList.length() > 0) {
      try {
        SignatureMoveUpgradeList upgradeList = new SignatureMoveUpgradeList();

        // TODO: decode rarity (third value) from serialized entry once implemented
        for (String serializedUpgrade : serializedList.split(DELIMITER)) {
         String[] upgradePair = serializedUpgrade.split(SEPARATOR);
          final UpgradeType type = UpgradeType.values()[Integer.parseInt(upgradePair[0])];
          final int numUpgrades = Math.abs(Integer.parseInt(upgradePair[1]));
          final UpgradeRarity rarity = UpgradeRarity.values()[Integer.parseInt(upgradePair[2])];
          upgradeList.add(new AbstractSignatureMoveUpgrade(type, numUpgrades, rarity));
        }
        return upgradeList;
      } catch (Exception e) {
        WrestlerMod.logger.warn("SignatureMoveUpgradeList::listFromSerializedData error parsing serialized list: "
            + serializedList  + ": " + e.getLocalizedMessage());
      }
    }
    return NO_UPGRADES;
  }
}