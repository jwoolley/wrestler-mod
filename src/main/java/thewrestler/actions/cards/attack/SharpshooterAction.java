package thewrestler.actions.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import org.apache.logging.log4j.Logger;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;
import thewrestler.util.BasicUtils;

import java.util.List;
import java.util.stream.Collectors;

public class SharpshooterAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private static DamageInfo.DamageType DAMAGE_TYPE = DamageInfo.DamageType.NORMAL;
  private static AbstractGameAction.AttackEffect ATTACK_EFFECT = AttackEffect.BLUNT_LIGHT;

  private boolean tickedOnce = false;

  public SharpshooterAction(AbstractCreature target, AbstractCreature source, int damagePerDebuff) {
    this.actionType = ActionType.DAMAGE;
    this.target = target;
    this.source = source;
    this.amount = damagePerDebuff;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
  }

  @Override
  public void update() {
    if (this.duration <= this.startDuration) {
      List<AbstractPower> debuffs = this.target.powers.stream()
          .filter(pow -> pow.type == AbstractPower.PowerType.DEBUFF).collect(Collectors.toList());

      Logger logger = WrestlerMod.logger;
      logger.info("SharpshooterAction::update # of debuffs: " + debuffs.size());
      debuffs.forEach(pow -> {
        AbstractDungeon.actionManager.addToBottom(
            new DamageAction(this.target, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SMASH));
      });
      this.tickDuration();
      this.isDone = true;
    }
    this.tickDuration();
  }
}
