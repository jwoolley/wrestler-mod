package thewrestler.actions.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thewrestler.util.CreatureUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DamageEnemiesAction extends AbstractGameAction
{
  private boolean utilizeBaseDamage = false;
  private boolean firstFrame = true;
  private int baseDamage;
  public int[] multiDamage;
  private final AbstractGameAction followUpAction;
  private final List<AbstractMonster> monsters;

  public DamageEnemiesAction(AbstractCreature source, int baseDamage, DamageInfo.DamageType type, AttackEffect effect, Predicate<AbstractMonster> predicate, boolean isFast, AbstractGameAction followUpAction) {
    this(source, baseDamage, type, effect, getMonsters(predicate), followUpAction);
  }

  public DamageEnemiesAction(AbstractCreature source, int[] multiDamage, DamageInfo.DamageType type,
                             AbstractGameAction.AttackEffect effect, List<AbstractMonster> monsters,
                             boolean isFast, AbstractGameAction followUpAction) {
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
    this.followUpAction = followUpAction;
  }

  public DamageEnemiesAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type,
                             AbstractGameAction.AttackEffect effect, List<AbstractMonster> monsters) {
    this(source, amount, type, effect, monsters, false, null);
  }

  public DamageEnemiesAction(AbstractCreature source, int baseDamage, DamageInfo.DamageType type,
                             AbstractGameAction.AttackEffect effect, List<AbstractMonster> monsters) {
    this(source, null, type, effect, monsters,false, null);
    this.baseDamage = baseDamage;
    this.utilizeBaseDamage = true;
  }

  public DamageEnemiesAction(AbstractCreature source, int baseDamage, DamageInfo.DamageType type,
                             AbstractGameAction.AttackEffect effect, List<AbstractMonster> monsters,
                             AbstractGameAction followUpAction) {
    this(source, null, type, effect, monsters,false, followUpAction);
    this.baseDamage = baseDamage;
    this.utilizeBaseDamage = true;
  }

  private static List<AbstractMonster> getMonsters(Predicate<AbstractMonster> predicate) {
    List<AbstractMonster> monsters = new ArrayList<>();

    AbstractDungeon.getCurrRoom().monsters.monsters.stream().filter(m -> predicate.test(m)).collect(Collectors.toList());
    return monsters;
  }

  public void update() {
    boolean playedMusic;
    if (this.firstFrame) {
      playedMusic = false;
      int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();
      if (this.utilizeBaseDamage) {
        this.multiDamage = DamageInfo.createDamageMatrix(this.baseDamage);
      }
      for (int i = 0; i < temp; i++) {
        if ((!(AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isDying) &&
            ((AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).currentHealth > 0) &&
            (!(AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isEscaping)) {
          if (playedMusic) {
            AbstractDungeon.effectList.add(
                new FlashAtkImgEffect(
                (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cX,
                (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cY, this.attackEffect, false));
          }
          else {
            playedMusic = true;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(

                (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cX,
                (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cY, this.attackEffect));
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
        if (!this.monsters.get(i).isDeadOrEscaped()) {
          if (this.attackEffect == AbstractGameAction.AttackEffect.POISON) {
            this.monsters.get(i).tint.color.set(Color.CHARTREUSE);
            this.monsters.get(i).tint.changeColor(Color.WHITE.cpy());
          }
          else if (this.attackEffect == AbstractGameAction.AttackEffect.FIRE) {
            this.monsters.get(i).tint.color.set(Color.RED);
            this.monsters.get(i).tint.changeColor(Color.WHITE.cpy());
          }
          this.monsters.get(i).damage(new DamageInfo(this.source, this.multiDamage[i], this.damageType));
        }
      }
      if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        AbstractDungeon.actionManager.clearPostCombatActions();
      }
      if (followUpAction != null) {
        AbstractDungeon.actionManager.addToBottom(followUpAction);
      } else if (!Settings.FAST_MODE) {
        AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
      }
    }
  }
}