package thewrestler.signaturemoves.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import thewrestler.cards.colorless.status.penalty.BluePenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.RedPenaltyStatusCard;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.UpgradeGroup;

import java.util.List;

public class Piledriver extends AbstractSignatureMoveCard {
  public static final String ID = "WrestlerMod:Piledriver";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "piledriver.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 7;
  private static final boolean HAS_EXHAUST = false;
  private static final boolean HAS_RETAIN = false;

  public Piledriver() {
    super(ID, NAME, IMG_PATH, COST, getDescription(), TYPE, TARGET, HAS_EXHAUST, HAS_RETAIN,
        BluePenaltyStatusCard.class, RedPenaltyStatusCard.class);
    this.baseDamage = this.damage = DAMAGE;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(m, p));

    if (m != null && !m.isDeadOrEscaped()) {
      AbstractDungeon.actionManager.addToTop(new VFXAction(
          new WeightyImpactEffect(m.hb.cX, m.hb.cY, Color.BLUE.cpy()), Settings.ACTION_DUR_XFAST));
      AbstractDungeon.actionManager.addToTop(new VFXAction(
          new WeightyImpactEffect(m.hb.cX, m.hb.cY, Color.RED.cpy()), Settings.ACTION_DUR_XFAST));
    }

    final PiledriverAction firstTick = new PiledriverAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), true);
    final PiledriverAction SecondTick = new PiledriverAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), false);

    AbstractDungeon.actionManager.addToBottom(firstTick);
    AbstractDungeon.actionManager.addToBottom(SecondTick);
  }

  private static class PiledriverAction extends AbstractGameAction {
    final private DamageInfo info;
    final private boolean isHeavy;

    public PiledriverAction(AbstractCreature target, DamageInfo info, boolean isHeavy) {
      this.info = info;
      setValues(target, info);
      this.actionType = AbstractGameAction.ActionType.DAMAGE;
      this.startDuration = Settings.ACTION_DUR_XFAST;
      this.duration = this.startDuration;
      this.isHeavy = isHeavy;
    }

    public void update() {
      if (shouldCancelAction()) {
        this.isDone = true;
        return;
      }
      tickDuration();
      if (this.isDone) {
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY,
            isHeavy ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AttackEffect.BLUNT_LIGHT, false));

        this.target.damage(this.info);
        if (this.target.lastDamageTaken > 0) {
          AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.source, this.target.lastDamageTaken));
          if (this.target.hb != null) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(new WallopEffect(this.target.lastDamageTaken, this.target.hb.cX, this.target.hb.cY)));
          }
        }
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
          AbstractDungeon.actionManager.clearPostCombatActions();
        } else {
          addToTop(new WaitAction(0.1F));
        }
      }
    }
  }

  @Override
  public void applyUpgrades(List<AbstractSignatureMoveUpgrade> upgradesToApply) {
  }

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    return new Piledriver();
  }

  @Override
  public UpgradeGroup getAllEligibleUpgrades() {
    // TODO: implement
    return null;
  }

  @Override
  public UpgradeGroup getCurrentEligibleUpgrades() {
    // TODO: implement
    return null;
  }

  @Override
  public String getIndefiniteCardName() {
    return EXTENDED_DESCRIPTION[4] + this.name;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.rawDescription = getDescription();
      initializeDescription();
    }
  }

  public static String getDescription() {
    return DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}