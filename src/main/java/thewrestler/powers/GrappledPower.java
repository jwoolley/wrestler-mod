package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;
import thewrestler.util.BasicUtils;

import java.util.List;
import java.util.stream.Collectors;

public class GrappledPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  private static final int DMG_DECREASE_PCT = 25;
  private static final int DMG_INCREASE_PCT = 25;
  public static final int MAX_HP_PCT = 10;

  public static final String POWER_ID = WrestlerMod.makeID("GrappledPower");
  public static final String IMG = "grappled.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.DEBUFF;

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
    return AbstractDungeon.getCurrRoom().monsters.monsters.stream()
        .filter(m -> m.hasPower(POWER_ID)).collect(Collectors.toList());
  }

  static public GrappledPower getGrapplePowerInstance() {
     List<AbstractMonster> grappledMonsters = getGrappledEnemies();
     if (!grappledMonsters.isEmpty()) {
       return (GrappledPower) grappledMonsters.get(0).getPower(POWER_ID);
     }
     return null;
  }

  @Override
  public void atEndOfRound() {
    System.out.println("GrappledPower::atEndOfRound triggered. source: " + this.source);
      if (this.source == AbstractDungeon.player) {
        GrappleContestedPower.applyGrappleContested(AbstractDungeon.player, this.owner, this.amount);
        System.out.println("GrappledPower::atEndOfRound source is player. reapplying GrappleContestedPower");
      } else {
      System.out.println("GrappledPower::atEndOfRound source isn't player. what is going on exactly");
    }
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    System.out.println("GrappledPower::atEndOfTurn triggered. isPlayer: " + isPlayer);
    if (isPlayer) {
      System.out.println("GrappledPower::atStartOfTurnPostDraw reapplying GrappleContestedPower to source: " + this.source);
      GrappleContestedPower.applyGrappleContested(this.source, this.owner, this.amount);
    }
  }


  public void onInitialApplication() {
    GrappleContestedPower.applyGrappleContested(this.source, this.owner, this.amount);
  }

  @Override
  public void onDeath() {
    GrappleContestedPower.clearGrappleContested(this.source, this.owner);
  }

  @Override
  public void onRemove() {
    GrappleContestedPower.clearGrappleContested(this.source, this.owner);
  }

  @Override
  public AbstractPower makeCopy() {
    return new GrappledPower(owner, source, amount);
  }
}
