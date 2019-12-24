package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.WrestlerMod;

import java.util.List;
import java.util.stream.Collectors;

public class NotCageFightingPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("NotCageFightingPower");
  public static final String IMG = "notcagematch.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public NotCageFightingPower(AbstractCreature owner) {
    super(POWER_ID, NAME, IMG, owner, AbstractDungeon.player, -1, POWER_TYPE);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + CageMatchPower.REDUCED_DAMAGE_AMOUNT + DESCRIPTIONS[1];
  }

  @Override
  public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
    if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
      CardCrawlGame.sound.play("DOOR_HATCH_OPEN_1");
      return CageMatchPower.REDUCED_DAMAGE_AMOUNT;
    }
    return damageAmount;
  }

  @Override
  public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
    if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
      CardCrawlGame.sound.play("DOOR_HATCH_OPEN_1");
      return CageMatchPower.REDUCED_DAMAGE_AMOUNT;
    }
    return damageAmount;
  }

  static public List<AbstractMonster> getNotCageMatchedEnemies() {
    return AbstractDungeon.getCurrRoom().monsters.monsters.stream()
        .filter(m -> m.hasPower(POWER_ID)).collect(Collectors.toList());
  }

  @Override
  public void atEndOfRound() {
    removePowerFromAllCharacters();
  }

  public static void removePowerFromAllCharacters() {
    AbstractPlayer p = AbstractDungeon.player;
    List<AbstractMonster> notCageMatchedEnemies = getNotCageMatchedEnemies();
    if (notCageMatchedEnemies.size() > 0) {
      notCageMatchedEnemies.forEach(m -> AbstractDungeon.actionManager.addToBottom(
          new RemoveSpecificPowerAction(m, p, NotCageFightingPower.POWER_ID)));
    }
    if (p.hasPower(POWER_ID)) {
      AbstractDungeon.actionManager.addToBottom(
          new RemoveSpecificPowerAction(p, p, NotCageFightingPower.POWER_ID));
    }
  }

  @Override
  public AbstractPower makeCopy() {
    return new NotCageFightingPower(owner);
  }
}