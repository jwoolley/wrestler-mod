package thewrestler.actions.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import org.apache.logging.log4j.Logger;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;
import thewrestler.util.BasicUtils;
import thewrestler.util.CreatureUtils;

import java.util.List;
import java.util.stream.Collectors;

public class HeartPunchAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  public HeartPunchAction(AbstractCreature target, AbstractCreature source, int hpPerDebuff) {
    this.actionType = ActionType.DAMAGE;
    this.target = target;
    this.source = source;
    this.amount = hpPerDebuff;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
  }

  @Override
  public void update() {
    if (this.duration <= this.startDuration) {
      List<AbstractPower> debuffs = CreatureUtils.getDebuffs(this.target);

      Logger logger = WrestlerMod.logger;
      logger.info("HeartPunchAction::update # of debuffs: " + debuffs.size());
      if (!debuffs.isEmpty()) {
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.MAROON.cpy(), true));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("HEART_SIMPLE"));
        CreatureUtils.queueRemoveAllDebuffsAction(this.target, this.source);

        debuffs.forEach(debuff ->
            AbstractDungeon.actionManager.addToBottom(new HealAction(this.source, this.source, this.amount)));
      }
      this.tickDuration();
      this.isDone = true;
    }
    this.tickDuration();
  }
}
