package thewrestler.signaturemoves.moveinfos;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;
import thewrestler.signaturemoves.upgrades.UpgradeType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractSignatureMoveInfo {
  public final boolean isFirstInstance;

  final AbstractSignatureMoveCard signatureMoveCard;
  final SignatureMoveUpgradeList upgradeList;

  public AbstractSignatureMoveInfo(AbstractSignatureMoveCard signatureMoveCard,
                                   SignatureMoveUpgradeList upgradeList, boolean isFirstInstance){
    this.signatureMoveCard = signatureMoveCard.makeCopy();
    this.upgradeList = new SignatureMoveUpgradeList(upgradeList);
    this.isFirstInstance = isFirstInstance;

    if (upgradeList != SignatureMoveUpgradeList.NO_UPGRADES) {
      this.signatureMoveCard.applyUpgrades(upgradeList);
    }
  }

  public AbstractSignatureMoveInfo makeCopy() {
    return this.makeCopy(this.upgradeList);
  }

  public AbstractSignatureMoveInfo makeCopy(SignatureMoveUpgradeList upgradeList) {
    try {
      final Constructor<? extends AbstractSignatureMoveInfo> constructor =
          this.getClass().getConstructor(AbstractSignatureMoveCard.class, SignatureMoveUpgradeList.class, Boolean.class);

      return constructor.newInstance(this.signatureMoveCard, this.upgradeList, this.isFirstInstance) ;
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException("WrestlerMod failed to auto-generate makeCopy for AbstractSignatureMoveInfo: " + this);
    }
  }

  public abstract void onCardPlayed(AbstractCard card);
  public abstract void atStartOfTurn();
  public abstract void atStartOfCombat();
  public abstract void atEndOfCombat();
  public abstract void upgradeMove(UpgradeType type);
  public abstract void onEnemyGrappled();
  public abstract String getDynamicConditionText();
  public abstract String getStaticConditionText();

  public AbstractSignatureMoveCard getSignatureMoveCard() {
    return this.signatureMoveCard;
  }

  public void triggerGainCard() {
    AbstractSignatureMoveCard card = this.signatureMoveCard.makeCopy();
    card.setCostForTurn(0);
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(this.signatureMoveCard));
  }

  abstract public boolean canStillTriggerCardGain();
}