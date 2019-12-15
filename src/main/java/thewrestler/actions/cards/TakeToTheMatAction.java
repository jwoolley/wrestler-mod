package thewrestler.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thewrestler.actions.power.ApplyGrappledAction;
import thewrestler.util.BasicUtils;

public class TakeToTheMatAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private static DamageInfo.DamageType DAMAGE_TYPE = DamageInfo.DamageType.NORMAL;
  private static AbstractGameAction.AttackEffect ATTACK_EFFECT = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
  private final int hpPercentThreshold;

  private boolean tickedOnce = false;

  public TakeToTheMatAction(AbstractMonster monster, int damage, int hpPercentThreshold) {
    this.actionType = ActionType.DEBUFF;
    this.target = monster;
    this.source = AbstractDungeon.player;
    this.amount = damage;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
    this.hpPercentThreshold = hpPercentThreshold;
  }

  @Override
  public void update() {

    if (!tickedOnce) {
      AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, ATTACK_EFFECT));
      DamageInfo info = new DamageInfo(this.source, this.amount, DAMAGE_TYPE);
      this.target.damage(info);
      tickedOnce = true;
    } else if (this.duration <= this.startDuration) {
      if (target.currentHealth <= target.maxHealth * BasicUtils.percentageIntToFloat(this.hpPercentThreshold)) {
        AbstractDungeon.actionManager.addToBottom(new ApplyGrappledAction(this.target, this.source));
      }
      this.tickDuration();
      this.isDone = true;
    }
    this.tickDuration();
  }
}
