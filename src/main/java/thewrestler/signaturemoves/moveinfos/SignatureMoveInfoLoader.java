package thewrestler.signaturemoves.moveinfos;

import thewrestler.signaturemoves.cards.SignatureMoveCardEnum;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;

public class SignatureMoveInfoLoader {
  public static AbstractSignatureMoveInfo makeInfo(SignatureMoveCardEnum cardEnum,
                                                   SignatureMoveUpgradeList upgradeList) {
    return cardEnum.getInfoCopy(upgradeList);
  }
}
