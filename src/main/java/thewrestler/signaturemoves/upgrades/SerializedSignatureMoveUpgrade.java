package thewrestler.signaturemoves.upgrades;

public class SerializedSignatureMoveUpgrade {
  final UpgradeType upgradeType;
  int numTimesUpgraded;

  public SerializedSignatureMoveUpgrade(int upgradeTypeOrdinal, int numTimesUpgraded) {
    this(getUpgradeType(upgradeTypeOrdinal), numTimesUpgraded);
  }


  public SerializedSignatureMoveUpgrade(UpgradeType upgradeType, int numTimesUpgraded) {
    this.upgradeType = upgradeType;
    this.numTimesUpgraded = numTimesUpgraded;
  }

  public static UpgradeType getUpgradeType(int ordinal) {
    return UpgradeType.values()[ordinal];
  }
}