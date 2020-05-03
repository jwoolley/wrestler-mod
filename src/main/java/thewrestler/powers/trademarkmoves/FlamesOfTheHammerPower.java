package thewrestler.powers.trademarkmoves;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thewrestler.WrestlerMod;
import thewrestler.powers.AbstractWrestlerPower;
import thewrestler.signaturemoves.cards.attack.BurningHammer;

public class FlamesOfTheHammerPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("FlamesOfTheHammerPower");
  public static final String IMG = "hammerflame.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  public FlamesOfTheHammerPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, NAME, IMG, owner, source, amount, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public void atStartOfTurn() {
    if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) &&
        (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
      AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.owner.hb.cX, this.owner.hb.cY, AbstractGameAction.AttackEffect.FIRE));
      this.owner.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS));

      if ((this.owner.isDying || this.owner.currentHealth <= 0) && (!this.owner.halfDead)) {
        AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(new BurningHammer()));
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
          AbstractDungeon.actionManager.clearPostCombatActions();
        }
      }
    }
  }

  @Override
  public AbstractPower makeCopy() {
    return new FlamesOfTheHammerPower(owner, source, amount);
  }
}