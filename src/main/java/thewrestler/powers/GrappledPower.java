package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.BasicUtils;
import thewrestler.util.CreatureUtils;

import java.util.List;
import java.util.stream.Collectors;

public class GrappledPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  private static final int DMG_DECREASE_PCT = 30;
  private static final int DMG_INCREASE_PCT = 30;

  public static final int HP_THRESHOLD = 10;

  public static final String POWER_ID = WrestlerMod.makeID("GrappledPower");
  public static final String IMG = "grappled.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

  // TODO: unique sound effect when applying power (for playability)

  public GrappledPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(POWER_ID, NAME, IMG, owner, source, amount, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + DMG_DECREASE_PCT + DESCRIPTIONS[1]  + DMG_INCREASE_PCT + DESCRIPTIONS[2]
      + this.amount + DESCRIPTIONS[3];
  }

  @Override
  public float atDamageReceive(float damage, DamageInfo.DamageType type) {
    if (type == DamageInfo.DamageType.NORMAL) {
      return damage * (1 + BasicUtils.percentageIntToFloat(DMG_INCREASE_PCT));
    }
    return damage;
  }

  @Override
  public float atDamageGive(float damage, DamageInfo.DamageType type) {
    if (type == DamageInfo.DamageType.NORMAL) {
      return damage * (1 - BasicUtils.percentageIntToFloat(DMG_DECREASE_PCT));
    }
    return damage;
  }

  static public List<AbstractMonster> getGrappledEnemies() {
    return CreatureUtils.getLivingMonsters().stream()
        .filter(m -> m.hasPower(POWER_ID)).collect(Collectors.toList());
  }

  @Override
  public void atEndOfRound() {
    System.out.println("GrappledPower::atEndOfRound triggered. source: " + this.source);
      if (this.source == AbstractDungeon.player) {
        MaintainGrapplePower.apply(AbstractDungeon.player, this.owner, this.amount,
            true);

        System.out.println("GrappledPower::atEndOfRound source is player. reapplying MaintainGrapplePower");
      } else {
      System.out.println("GrappledPower::atEndOfRound source isn't player. what is going on exactly");
    }
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    System.out.println("GrappledPower::atEndOfTurn triggered. isPlayer: " + isPlayer);
    if (isPlayer) {
      System.out.println("GrappledPower::atStartOfTurnPostDraw reapplying MaintainGrapplePower to source: " + this.source);
      MaintainGrapplePower.apply(this.source, this.owner, this.amount, true);
    }
  }

  @Override
  public void stackPower(int stackAmount) {


    //    // THIS ENABLES STACKING OF HP THRESHOLD (if reapplying grapple to the same target). disabling now
    //    // see similar code section in MaintainGrapplePower
    //    super.stackPower(stackAmount);

    //    // THIS DISABLES STACKING OF HP THRESHOLD (if reapplying grapple to the same target).
    final int currentGrappleStacks = owner.getPower(POWER_ID).amount;
    MaintainGrapplePower.apply(this.source, this.owner, stackAmount - currentGrappleStacks, false);

    MaintainGrapplePower.apply(this.source, this.owner, stackAmount, false);

    if (WrestlerCharacter.hasSignatureMoveInfo()) {
      WrestlerCharacter.getSignatureMoveInfo().onEnemyGrappled();
    }
  }

  @Override
  public void onInitialApplication() {
    getGrappledEnemies().stream()
        .filter(m -> m != this.owner)
        .forEach(m ->
            AbstractDungeon.actionManager.addToBottom(
                new RemoveSpecificPowerAction(m, this.owner, GrappledPower.POWER_ID)));

    // isRefreshOrTargetSwitch is either true (because another enemy is grappled) or doesn't matter
    // (the flag isn't checked the player isn't already maintaining grapple)
    MaintainGrapplePower.apply(this.source, this.owner, this.amount, true);

    if (WrestlerCharacter.hasSignatureMoveInfo()) {
      WrestlerCharacter.getSignatureMoveInfo().onEnemyGrappled();
    }
  }

  @Override
  public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
    super.onApplyPower(power, target, source);
  }

  @Override
  public void onDeath() {
    MaintainGrapplePower.clear(this.source, this.owner);
  }

  @Override
  public void onRemove() {
    if (getGrappledEnemies().size() == 0) {
      MaintainGrapplePower.clear(this.source, this.owner);
    }
  }

  public static boolean enemyWillHaveGrappleAfterAttack(AbstractMonster m) {
    return m.hasPower(GrappledPower.POWER_ID)
          || (m.hasPower(SquaringOffPower.POWER_ID) && m.getPower(SquaringOffPower.POWER_ID).amount == 1);
  }

  @Override
  public AbstractPower makeCopy() {
    return new GrappledPower(owner, source, amount);
  }
}
