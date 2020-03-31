package thewrestler.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thewrestler.WrestlerMod;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.util.RefreshHandListener;

public class NearFallPower extends AbstractWrestlerPower implements RefreshHandListener {
  public static final String POWER_ID = WrestlerMod.makeID("NearFallPower");
  public static final String IMG = "nearfall.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  private boolean disabledForThisTurn;
  private boolean disabled;

  public NearFallPower() {
    super(POWER_ID, NAME, IMG, AbstractDungeon.player, AbstractDungeon.player, -1, POWER_TYPE);
    this.disabledForThisTurn = false;
    this.disabled = false;
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    if (isPlayer) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));
    }
  }

  public void disableForTurn() {
    this.disabledForThisTurn = true;
  }

  @Override
  public void onRefreshHand() {
    if ((AbstractDungeon.actionManager.actions.isEmpty()) && (AbstractDungeon.player.hand.isEmpty())
        && (!AbstractDungeon.actionManager.turnHasEnded) && (!this.disabled)
        && (!AbstractDungeon.player.hasPower("No Draw")) && (!AbstractDungeon.isScreenUp)) {
      if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) && (!this.disabledForThisTurn) && (
          (AbstractDungeon.player.discardPile.size() > 0) || (AbstractDungeon.player.drawPile.size() > 0))) {
        flash();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
      }
    }
  }


  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0];
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
