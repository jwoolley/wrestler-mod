package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
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

public class CageMatchPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("CageMatchPower");
  public static final String IMG = "cagematch.png";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public static final int REDUCED_DAMAGE_AMOUNT = 1;

  private final AbstractCreature opponent;
  // TODO: unique VFX/SFX effect when applying power

  public CageMatchPower(AbstractCreature owner, AbstractCreature opponent) {
    super(POWER_ID, NAME, IMG, owner, AbstractDungeon.player, -1, POWER_TYPE);
    this.opponent = opponent;
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + REDUCED_DAMAGE_AMOUNT + DESCRIPTIONS[1];
  }

  static public List<AbstractMonster> getCageMatchedEnemies() {
    return AbstractDungeon.getCurrRoom().monsters.monsters.stream()
        .filter(m -> m.hasPower(POWER_ID)).collect(Collectors.toList());
  }

  @Override
  public void onInitialApplication() {
    if (this.owner != AbstractDungeon.player) {

      if (this.owner.hasPower(NotCageFightingPower.POWER_ID)) {
        AbstractDungeon.actionManager.addToBottom(
            new RemoveSpecificPowerAction(this.owner, AbstractDungeon.player, NotCageFightingPower.POWER_ID));
      }

      AbstractDungeon.getCurrRoom().monsters.monsters.stream()
          .filter(m -> m != this.owner)
          .forEach(m -> {
            if (m.hasPower(CageMatchPower.POWER_ID)) {
              AbstractDungeon.actionManager.addToBottom(
                  new RemoveSpecificPowerAction(m, this.owner, CageMatchPower.POWER_ID));
            }
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, this.source, new NotCageFightingPower(this.source)));
          });
    }
  }

  @Override
  public void atEndOfRound() {
    removePowerFromAllCharacters();
  }

  @Override
  public void onDeath() {
    removePowerFromAllCharacters();
  }

  public static void removePowerFromAllCharacters() {
    AbstractPlayer p = AbstractDungeon.player;
    List<AbstractMonster> cageMatchedEnemies = getCageMatchedEnemies();
    if (cageMatchedEnemies.size() > 0) {
      cageMatchedEnemies.forEach(m -> AbstractDungeon.actionManager.addToBottom(
          new RemoveSpecificPowerAction(m, p, CageMatchPower.POWER_ID)));
    }
    if (p.hasPower(POWER_ID)) {
      AbstractDungeon.actionManager.addToBottom(
          new RemoveSpecificPowerAction(p, p, CageMatchPower.POWER_ID));
    }
  }

  @Override
  public AbstractPower makeCopy() {
    return new CageMatchPower(owner, source);
  }
}