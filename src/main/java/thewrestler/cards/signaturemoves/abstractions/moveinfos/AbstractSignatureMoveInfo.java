package thewrestler.cards.signaturemoves.abstractions.moveinfos;

import thewrestler.cards.signaturemoves.abstractions.cards.AbstractSignatureMoveCard;
import thewrestler.cards.signaturemoves.abstractions.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.cards.signaturemoves.abstractions.upgrades.UpgradeType;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSignatureMoveInfo {
  final AbstractSignatureMoveCard signatureMoveCard;
  final Map<AbstractSignatureMoveUpgrade, Integer> upgrades;

  public AbstractSignatureMoveInfo(AbstractSignatureMoveCard signatureMoveCard,
                                   Map<AbstractSignatureMoveUpgrade, Integer> upgrades){
    this.signatureMoveCard = signatureMoveCard.makeCopy();
    this.upgrades = new HashMap<>(upgrades);
  }

  public abstract void onCardPlayed();
  public abstract void atStartOfTurn();
  public abstract void atStartOfCombat();
  public abstract void upgradeMove(UpgradeType type);
  public abstract void onEnemyGrappled();
  public abstract String getConditionText();
  public abstract String getDynamicConditionText();
  public void triggerGainCard() {

  };
}