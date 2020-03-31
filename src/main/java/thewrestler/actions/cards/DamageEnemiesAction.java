package thewrestler.actions.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;
import java.util.List;

public class DamageEnemiesAction extends AbstractGameAction
{
  private boolean utilizeBaseDamage = false;
  private boolean firstFrame = true;
  private int baseDamage;
  public int[] multiDamage;
  private final List<AbstractMonster> monsters;

  public DamageEnemiesAction(AbstractCreature source, int[] multiDamage, DamageInfo.DamageType type,
                             AbstractGameAction.AttackEffect effect, List<AbstractMonster> monsters,
                             boolean isFast) {
    this.source = source;
    this.multiDamage = multiDamage;
    this.actionType = AbstractGameAction.ActionType.DAMAGE;
    this.damageType = type;
    this.attackEffect = effect;

    this.monsters = new ArrayList<>(monsters);
    if (isFast) {
      this.duration = Settings.ACTION_DUR_XFAST;
    } else {
      this.duration = Settings.ACTION_DUR_FAST;
    }
  }

  public DamageEnemiesAction(AbstractCreature source, int baseDamage, DamageInfo.DamageType type,
                             AbstractGameAction.AttackEffect effect, List<AbstractMonster> monsters) {
    this(source, null, type, effect, monsters,false);
    this.baseDamage = baseDamage;
    this.utilizeBaseDamage = true;
  }

  public void update() {
    boolean playedMusic;
    if (this.firstFrame) {
      playedMusic = false;
      int temp = monsters.size();
      if (this.utilizeBaseDamage) {
        this.multiDamage = DamageInfo.createDamageMatrix(this.baseDamage);
      }
      for (int i = 0; i < temp; i++) {
        AbstractMonster mo = this.monsters.get(i);
        if (!mo.isDying && mo.currentHealth > 0 && mo.isEscaping) {
          if (playedMusic) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(mo.hb.cX, mo.hb.cY, this.attackEffect, true));
          }
          else {
            playedMusic = true;
            AbstractDungeon.effectList.add(
                new FlashAtkImgEffect(mo.hb.cX, mo.hb.cY, this.attackEffect, false));
          }
        }
      }
      this.firstFrame = false;
    }
    tickDuration();
    if (this.isDone) {
      if (this.monsters.size() == AbstractDungeon.getCurrRoom().monsters.monsters.size())
      for (AbstractPower p : AbstractDungeon.player.powers) {
        p.onDamageAllEnemies(this.multiDamage);
      }
      int temp = this.monsters.size();
      for (int i = 0; i < temp; i++) {
        AbstractMonster mo = this.monsters.get(i);

        if (!mo.isDeadOrEscaped()) {
          if (this.attackEffect == AbstractGameAction.AttackEffect.POISON) {
            mo.tint.color.set(Color.CHARTREUSE);mo.tint.changeColor(Color.WHITE.cpy());
          }
          else if (this.attackEffect == AbstractGameAction.AttackEffect.FIRE) {
            mo.tint.color.set(Color.RED);
            mo.tint.changeColor(Color.WHITE.cpy());
          }
          this.monsters.get(i).damage(new DamageInfo(this.source, this.multiDamage[i], this.damageType));
        }
      }
      if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        AbstractDungeon.actionManager.clearPostCombatActions();
      }
      if (!Settings.FAST_MODE) {
        AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
      }
    }
  }
}