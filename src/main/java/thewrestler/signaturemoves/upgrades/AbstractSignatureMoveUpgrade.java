package thewrestler.signaturemoves.upgrades;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AbstractSignatureMoveUpgrade {
  final private UpgradeType upgradeType;
  final int numUpgrades;

  public AbstractSignatureMoveUpgrade(UpgradeType upgradeType, int numUpgrades) {
    this.upgradeType = upgradeType;
    this.numUpgrades = numUpgrades;
  }
}
