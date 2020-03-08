package thewrestler.actions.power;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

public class GainPlatedArmorRandomMonsterAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_MED;
  private static AbstractGameAction.ActionType ACTION_TYPE = AbstractGameAction.ActionType.BLOCK;


  public GainPlatedArmorRandomMonsterAction(AbstractCreature source, int amount) {
    this.duration = ACTION_DURATION;
    this.source = source;
    this.amount = amount;
    this.actionType = ACTION_TYPE;
  }

  public void update()
  {
    if (this.duration == ACTION_DURATION) {
      ArrayList<AbstractMonster> validMonsters = new ArrayList<>();
      for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
        if ((m != this.source) && (m.intent != AbstractMonster.Intent.ESCAPE) && (!m.isDying)) {
          validMonsters.add(m);
        }
      }
      if (!validMonsters.isEmpty()) {
        this.target = (validMonsters.get(AbstractDungeon.aiRng.random(validMonsters.size() - 1)));
      } else {
        this.target = this.source;
      }
      if (this.target != null) {
        AbstractDungeon.effectList.add(
            new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.SHIELD));

        AbstractDungeon.actionManager.addToTop(
            new ApplyPowerAction(this.target, this.target,
                new PlatedArmorPower(this.target, this.amount), this.amount));

        this.target.addBlock(this.amount);
      }
    }
    tickDuration();
  }
}
