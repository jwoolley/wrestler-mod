package thewrestler.signaturemoves.upgrades;

public class AbstractSignatureMoveUpgrade {
  public final UpgradeType type;
  public final int numUpgrades;
  public final UpgradeRarity rarity;

  public AbstractSignatureMoveUpgrade(UpgradeType type, int numUpgrades, UpgradeRarity rarity) {
    this.type = type;
    this.numUpgrades = numUpgrades;
    this.rarity = rarity;
  }

  public AbstractSignatureMoveUpgrade(UpgradeType upgradeType, UpgradeRarity rarity) {
    this(upgradeType, -1, rarity);
  }
}
