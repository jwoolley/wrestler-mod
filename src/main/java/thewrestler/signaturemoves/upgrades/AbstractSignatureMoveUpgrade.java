package thewrestler.signaturemoves.upgrades;

public class AbstractSignatureMoveUpgrade {
  public final UpgradeType upgradeType;
  public final int numUpgrades;

  public AbstractSignatureMoveUpgrade(UpgradeType upgradeType, int numUpgrades) {
    this.upgradeType = upgradeType;
    this.numUpgrades = numUpgrades;
  }
}
