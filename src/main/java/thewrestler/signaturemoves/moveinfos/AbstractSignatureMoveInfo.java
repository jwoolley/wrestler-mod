package thewrestler.signaturemoves.moveinfos;

import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.UpgradeType;
import thewrestler.util.BasicUtils;

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
  public abstract String getStaticConditionText();
  public abstract String getDynamicConditionText();

  public String getConditionText() {
    return BasicUtils.isPlayerInCombat() ? getDynamicConditionText() : getStaticConditionText();
  }

  public void triggerGainCard() {

  };
}