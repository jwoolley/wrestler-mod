package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.GrappledPower;
import thewrestler.powers.SprainPower;
import thewrestler.powers.WrestlerShackled;
import thewrestler.util.CreatureUtils;

import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Matrix extends CustomCard {
  public static final String ID = WrestlerMod.makeID("Matrix");
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "matrix.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = -1;
  private static final int ADDITIONAL_STACKS = 0;
  private static final int ADDITIONAL_STACKS_ON_UPGRADE = 1;

  public Matrix() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.magicNumber = this.baseMagicNumber = ADDITIONAL_STACKS;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new MatrixAction(this.energyOnUse, this.magicNumber, this.freeToPlayOnce));
  }

  private static class MatrixAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_XFAST;
    private final boolean freeToPlayOnce;

    MatrixAction(int energyOnUse, int additionalTicks,
                 boolean freeToPlayOnce) {
      this(energyOnUse, additionalTicks, freeToPlayOnce, false);
    }

    MatrixAction(int energyOnUse, int additionalTicks,
                 boolean freeToPlayOnce, boolean isSubsequentTick) {
      this.duration = DURATION;
      this.actionType = ActionType.DAMAGE;
      this.source = AbstractDungeon.player;
      this.amount = isSubsequentTick ? energyOnUse : calculateTotalAmount(this.source, energyOnUse) + additionalTicks;
      this.freeToPlayOnce = freeToPlayOnce;
    }

    private int calculateTotalAmount(AbstractCreature source, int baseAmount) {
      int netAmount = baseAmount;
      if (source.isPlayer && AbstractDungeon.player.hasRelic("Chemical X")) {
        netAmount += 2;
        AbstractDungeon.player.getRelic("Chemical X").flash();
      }
      return netAmount;
    }

    private static void applyRandomDebuff(List<AbstractMonster> targets, AbstractCreature source) {
      int roll = AbstractDungeon.relicRng.random(0, 99);
      if (roll < 50) {
        applyRandomCommonDebuff(targets, source);
      } else if (roll > 80) {
        applyRandomRareDebuff(targets, source);
      } else {
        applyRandomUncommonDebuff(targets, source);
      }
    }

    private static void applyRandomCommonDebuff(List<AbstractMonster> targets, AbstractCreature source) {
      final int index = AbstractDungeon.cardRandomRng.random(6);
      switch (index) {
        case 1:
          targets.forEach(target ->
           AbstractDungeon.actionManager.addToBottom(
              new ApplyPowerAction(target, source,
                new PoisonPower(target, source, 1), 1, true,
                AbstractGameAction.AttackEffect.NONE)));
          break;
        case 2:
          targets.forEach(target ->
            AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(target, source,new SprainPower(target, 1), 1,
                true, AbstractGameAction.AttackEffect.NONE)));
          break;
        case 3:
          targets.forEach(target ->
              AbstractDungeon.actionManager.addToBottom( new ApplyPowerAction(target, source,
              new VulnerablePower(target, 1, source instanceof AbstractMonster), 1,
              true, AbstractGameAction.AttackEffect.NONE)));
          break;
        case 4:
          targets.forEach(target -> {
            AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(target, source, new StrengthPower(target, -1), -1,
                true, AbstractGameAction.AttackEffect.NONE));

            // TODO: double should double shackled as well
            if (!target.hasPower("Artifact")) {
              AbstractDungeon.actionManager.addToBottom(
                  new ApplyPowerAction(target, source,
                      new WrestlerShackled(target, 1), 1, true,
                      AbstractGameAction.AttackEffect.NONE));
            }
          });
          break;
        case 5:
        default:
          targets.forEach(target ->
            AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(target, source,
             new WeakPower(target, 1, source instanceof AbstractMonster), 1, true,
            AbstractGameAction.AttackEffect.NONE)));
      }
    }

    private static void applyRandomUncommonDebuff(List<AbstractMonster> targets, AbstractCreature source) {
      applyRandomCommonDebuff(targets, source);
    }

    private static void applyRandomRareDebuff(List<AbstractMonster> targets, AbstractCreature source) {
      final int index = AbstractDungeon.cardRandomRng.random(2);
      switch (index) {
        case 1:
          targets.forEach(target ->
            AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(target, source,
                new CorpseExplosionPower(target), 1, true,
                AbstractGameAction.AttackEffect.NONE)));
          break;
        case 2:
          // only apply Grappled to one target, and reroll if any target is Grappled already
          if (targets.stream().noneMatch(t -> t.hasPower(GrappledPower.POWER_ID))) {
            AbstractMonster target = AbstractDungeon.getRandomMonster();
            AbstractDungeon.actionManager.addToBottom(new ApplyGrappledAction(target, source));
          } else {
            applyRandomDebuff(targets, source);
          }
          break;
        case 3:
        default:
          targets.forEach(target ->
            AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(target, source,
                new ConstrictedPower(target, source, 1), 1, true,
                AbstractGameAction.AttackEffect.NONE)));
      }
    }

    @Override
    public void update() {
      if (this.duration <= DURATION) {
        if (this.amount > 0) {
          applyRandomDebuff(CreatureUtils.getLivingMonsters(), this.source);
          AbstractDungeon.actionManager.addToBottom(
              new MatrixAction(--this.amount, 0, this.freeToPlayOnce, true));
        } else {

          CreatureUtils.getLivingMonsters().forEach(_target -> {
            List<AbstractPower> debuffs = CreatureUtils.getDebuffs(_target);
            if (_target.hasPower(WrestlerShackled.POWER_ID)) {
              debuffs.add(_target.getPower(WrestlerShackled.POWER_ID));
            }
            for (AbstractPower debuff : debuffs) {
              debuff.stackPower(debuff.amount);
            }
          });
          if (!this.freeToPlayOnce && this.source.isPlayer) {
            AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
          }
        }
        this.isDone = true;
      }
      this.tickDuration();
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Matrix();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(ADDITIONAL_STACKS_ON_UPGRADE);
      this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
      initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}