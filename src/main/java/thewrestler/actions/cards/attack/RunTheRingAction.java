package thewrestler.actions.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RunTheRingAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private int[] multiDamage;
  private DamageInfo.DamageType damageType;
  private AbstractPlayer player;

  public RunTheRingAction(AbstractPlayer player, int[] multiDamage, DamageInfo.DamageType damageType, int amount) {
    this.multiDamage = multiDamage;
    this.damageType = damageType;
    this.player = player;
    this.duration = ACTION_DURATION;
    this.actionType = AbstractGameAction.ActionType.SPECIAL;
    this.amount = amount;
  }

  @Override
  public void update() {
    if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
      AbstractDungeon.actionManager.clearPostCombatActions();
      this.isDone = true;
      return;
    }

    for (int i = 0; i < this.amount; i++) {
      AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(
          this.player, this.multiDamage, this.damageType, AttackEffect.BLUNT_LIGHT, true));
    }

//    if (this.amount > 0) {
//      this.amount--;
//      AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(
//          this.player, this.multiDamage, this.damageType, AttackEffect.BLUNT_LIGHT, true));
//
//      if ((this.amount > 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
//        AbstractDungeon.actionManager.addToBottom(new RunTheRingAction(
//            this.player, this.multiDamage, this.damageType, this.amount));
//      }
//    }
    this.isDone = true;
  }
}