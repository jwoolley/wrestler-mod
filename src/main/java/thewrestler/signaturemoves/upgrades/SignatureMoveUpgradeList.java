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
}
