package thewrestler.signaturemoves.moveinfos;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thewrestler.cards.EndOfCombatListener;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeRarity;
import thewrestler.signaturemoves.upgrades.UpgradeType;

import java.net.URI;

public interface AbstractSignatureMoveInfoInterface extends StartOfCombatListener, EndOfCombatListener {
  void triggerGainTrademarkMove(boolean toDeck);
  AbstractSignatureMoveCard getSignatureMoveCardReference();
  SignatureMoveUpgradeList getUpgradeList();
  void applyUpgrade(AbstractSignatureMoveUpgrade upgrade);
  void onCardPlayed(AbstractCard card);
  void onCardExhausted(AbstractCard card);
  void onEnemyGrappled();
  String getDynamicConditionText();
  String getStaticConditionText();
  boolean canStillTriggerCardGain();
  void manuallyTriggerCardGain(boolean toDeck, boolean onTop);
  void atStartOfTurn();
  void atEndOfTurn();
}
