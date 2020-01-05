package thewrestler.signaturemoves.cards;

import thewrestler.signaturemoves.moveinfos.AbstractSignatureMoveInfo;
import thewrestler.signaturemoves.moveinfos.ChokeslamMoveInfo;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;

import java.util.Arrays;
import java.util.Optional;

public enum SignatureMoveCardEnum {
  CHOKESLAM(new Chokeslam(), new ChokeslamMoveInfo());

  SignatureMoveCardEnum(AbstractSignatureMoveCard card, AbstractSignatureMoveInfo moveInfo) {
    this.card = card;
    this.moveInfo = moveInfo;
  }

  public AbstractSignatureMoveCard getCardCopy() {
    return this.card.makeCopy();
  }

  public AbstractSignatureMoveInfo getInfoCopy(SignatureMoveUpgradeList upgradeList) {
    return this.moveInfo.makeCopy();
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