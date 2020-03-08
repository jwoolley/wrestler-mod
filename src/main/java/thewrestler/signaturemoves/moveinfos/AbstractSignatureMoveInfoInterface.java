package thewrestler.signaturemoves.moveinfos;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thewrestler.cards.EndOfCombatListener;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeType;

public interface AbstractSignatureMoveInfoInterface extends StartOfCombatListener, EndOfCombatListener {
  AbstractSignatureMoveCard getSignatureMoveCard();
  SignatureMoveUpgradeList getUpgradeList();
  void onCardPlayed(AbstractCard card);
  void onCardExhausted(AbstractCard card);
  void upgradeMove(UpgradeType type);
  void onEnemyGrappled();
  String getDynamicConditionText();
  String getStaticConditionText();
  boolean canStillTriggerCardGain();
  void manuallyTriggerCardGain(boolean toDeck, boolean onTop);
  void atStartOfTurn();
  void atEndOfTurn();
}
