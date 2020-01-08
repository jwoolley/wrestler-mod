package thewrestler.signaturemoves.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactBlurEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import org.apache.commons.lang3.StringUtils;
import thewrestler.signaturemoves.upgrades.SignatureMoveUpgradeList;

public class Piledriver extends AbstractSignatureMoveCard {
  public static final String ID = "WrestlerMod:Piledriver";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "piledriver.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 2;
  private static final int DAMAGE = 15;
  private static final int NUM_CARDS = 1;
  private static final int NUM_ENERGY = 1;
  private static final boolean HAS_RETAIN = true;

  private int energyGain;

  public Piledriver() {
    super(ID, NAME, IMG_PATH, COST, getDescription(NUM_ENERGY, NUM_CARDS), TYPE, TARGET, HAS_RETAIN);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = NUM_CARDS;
    this.energyGain = NUM_ENERGY;
  }

  private static class PiledriverAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_FAST;
    private final DamageInfo damageInfo;
    private final int numEnergy;
    private final int numCards;

    PiledriverAction(AbstractMonster target, AbstractPlayer source, int damage, int numEnergy, int numCards) {
      this.duration = DURATION;
      this.actionType = ActionType.DAMAGE;
      this.target = target;
      this.source = source;
      this.numEnergy = numEnergy;
      this.numCards = numCards;
      this.damageInfo =  new DamageInfo(this.source, damage, DamageInfo.DamageType.NORMAL);
    }

    @Override
    public void update() {
      if (this.duration <= 0.1f) {
        AbstractDungeon.effectList.add(
            new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.NONE));
        boolean shouldTrigger = false;

        if (target.currentBlock > 0 && damageInfo.output > target.currentBlock) {
          shouldTrigger = true;
        }

        this.target.damage(damageInfo);

        if (!shouldTrigger && (this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead){
          shouldTrigger = true;
        }

        if (shouldTrigger) {
          CardCrawlGame.sound.play("CARD_BURN");
          AbstractDungeon.actionManager.addToTop(new VFXAction(
              new DamageImpactBlurEffect(this.target.hb.cX, this.target.hb.cY), Settings.ACTION_DUR_XFAST));
          AbstractDungeon.actionManager.addToTop(new GainEnergyAction(this.numEnergy));
          AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, this.numCards));
        }
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
          AbstractDungeon.actionManager.clearPostCombatActions();
        }
        this.isDone = true;
      }
      tickDuration();
    }
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (m != null) {
      AbstractDungeon.actionManager.addToBottom(
          new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY, Color.MAROON.cpy()), Settings.ACTION_DUR_XFAST));

      AbstractDungeon.actionManager.addToBottom(
          new PiledriverAction(m, p, this.damage, this.energyGain, this.magicNumber));
    }
  }

  @Override
  public void applyUpgrades(SignatureMoveUpgradeList upgradeList) {
  }

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    return new Piledriver();
  }

  @Override
  public String getIndefiniteCardName() {
    return EXTENDED_DESCRIPTION[4] + this.name;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.rawDescription = getDescription(this.energyGain, this.magicNumber);
      initializeDescription();
    }
  }
  public static String getDescription(int numEnergy, int numBonusCards) {
    return DESCRIPTION
        + StringUtils.repeat(EXTENDED_DESCRIPTION[0], numEnergy)
        + EXTENDED_DESCRIPTION[1]
        + (numBonusCards == 1 ? EXTENDED_DESCRIPTION[2] : EXTENDED_DESCRIPTION[3]);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}