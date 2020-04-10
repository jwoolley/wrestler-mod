package thewrestler.signaturemoves.cards;

import thewrestler.WrestlerMod;
import thewrestler.signaturemoves.moveinfos.AbstractSignatureMoveInfo;
import thewrestler.signaturemoves.moveinfos.ChokeslamMoveInfo;
import thewrestler.signaturemoves.moveinfos.DragonGateMoveInfo;
import thewrestler.signaturemoves.moveinfos.PiledriverMoveInfo;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;

import java.util.Arrays;
import java.util.Optional;

public enum SignatureMoveCardEnum {
  CHOKESLAM(new Chokeslam(), new ChokeslamMoveInfo()),
  DRAGON_GATE(new DragonGate(), new DragonGateMoveInfo()),
  PILEDRIVER(new Piledriver(), new PiledriverMoveInfo());

  SignatureMoveCardEnum(AbstractSignatureMoveCard card, AbstractSignatureMoveInfo moveInfo) {
    this.card = card;
    this.moveInfo = moveInfo;
  }

  public AbstractSignatureMoveCard getCardCopy() {
    return this.card.makeCopy();
  }

  public AbstractSignatureMoveInfo getInfoCopy(SignatureMoveUpgradeList upgradeList) {
    AbstractSignatureMoveInfo info = this.moveInfo.makeCopy();
    info.applyUpgrades(upgradeList);
    return info;
  }

  public static AbstractSignatureMoveCard getCardCopy(int index) {
    if (index < 0) {
      WrestlerMod.logger.warn("AbstractSignatureMoveCard::getCardCopy unrecognized index: " + index);
      return null;
    }
    return SignatureMoveCardEnum.values()[index].getCardCopy();
  }

  public static SignatureMoveCardEnum getEnum(AbstractSignatureMoveCard card) {
    final int index = getOrdinal(card);

    if (index == -1) {
      return null;
    }
    return SignatureMoveCardEnum.values()[index];
  }


  public static int getOrdinal(AbstractSignatureMoveCard card) {
    Optional<SignatureMoveCardEnum> optional = Arrays.stream(SignatureMoveCardEnum.values())
        .filter(e -> e.card.cardID == card.cardID)
        .findFirst();

    return optional.map(Enum::ordinal).orElse(-1);
  }

  private final AbstractSignatureMoveCard card;
  private final AbstractSignatureMoveInfo moveInfo;
}