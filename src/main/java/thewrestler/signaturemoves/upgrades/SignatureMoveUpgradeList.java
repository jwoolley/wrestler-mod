package thewrestler.signaturemoves.upgrades;

import java.util.ArrayList;

public class SignatureMoveUpgradeList extends ArrayList<AbstractSignatureMoveUpgrade> {
  public static final SignatureMoveUpgradeList NO_UPGRADES = new SignatureMoveUpgradeList();

  public SignatureMoveUpgradeList() {
    super();
  }

  public SignatureMoveUpgradeList(SignatureMoveUpgradeList upgradeList) {
    super(upgradeList);
  }

  public int getSerializedUpgradeList() {
    int serializedList = 0;

    if (this != NO_UPGRADES && !this.isEmpty()) {
      for (AbstractSignatureMoveUpgrade upgrade : this) {
        serializedList += upgrade.numUpgrades * Math.pow(2, upgrade.type.ordinal());
      }
    }
    return serializedList;
  }

  public static SignatureMoveUpgradeList listFromSerializedData(int serializedList) {
    if (serializedList <= 0) {
      return NO_UPGRADES;
    }

    SignatureMoveUpgradeList upgradeList = new SignatureMoveUpgradeList();

    // TODO: implement bit-based conversion to upgrade list

    return upgradeList;
  }
}
