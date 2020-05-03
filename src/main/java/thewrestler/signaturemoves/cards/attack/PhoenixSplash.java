package thewrestler.signaturemoves.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import thewrestler.cards.colorless.status.penalty.OrangePenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.RedPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.YellowPenaltyStatusCard;
import thewrestler.powers.trademarkmoves.FlamesOfTheHammerPower;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.UpgradeGroup;
import thewrestler.util.CreatureUtils;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;

import java.util.Arrays;
import java.util.List;

public class PhoenixSplash extends AbstractSignatureMoveCard {
  public static final String ID = "WrestlerMod:PhoenixSplash";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "phoenixsplash.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 2;
  private static final int DAMAGE = 16;
  private static final int FLAMES_AMOUNT = 4;
  private static final boolean HAS_EXHAUST = true;
  private static final boolean HAS_RETAIN = false;

  public PhoenixSplash() {
    super(ID, NAME, IMG_PATH, COST, getDescription(), TYPE, TARGET, HAS_EXHAUST, HAS_RETAIN,
        OrangePenaltyStatusCard.class, RedPenaltyStatusCard.class);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = FLAMES_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new PhoenixSplashAction(p, this.multiDamage, this.damageType,
        AbstractGameAction.AttackEffect.FIRE, this.magicNumber, Settings.FAST_MODE));
  }

  private static class PhoenixSplashAction extends AbstractGameAction {
    private static final float DURATION_1 = Settings.ACTION_DUR_XFAST;
    private static final float DURATION_2 = 0.1f;
    private final int[] multiDamage;
    private final int numDebuffs;
    private final boolean isFast;

    PhoenixSplashAction(AbstractPlayer source, int[] multiDamage, DamageInfo.DamageType damageType,
                    AttackEffect attackEffect, int numDebuffs, boolean isFast) {
      this.duration = DURATION_2 + DURATION_1;
      this.actionType = ActionType.DAMAGE;
      this.source = source;
      this.multiDamage = Arrays.copyOf(multiDamage, multiDamage.length);
      this.damageType = damageType;
      this.numDebuffs = numDebuffs;
      this.attackEffect = attackEffect;
      this.isFast = isFast;
    }

    boolean tickedOnce = false;

    @Override
    public void update() {
      if (this.duration <=  DURATION_2 + DURATION_1 && !tickedOnce) {
        AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.ORANGE.cpy(), 0.75F, 1.0f, true));
        CardCrawlGame.sound.play("BOMB_IMPACT_1");
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true);
        CreatureUtils.getLivingMonsters().forEach(mo ->
            AbstractDungeon.effectList.add(
                new FlashAtkImgEffect(mo.hb.cX, mo.hb.cY, AttackEffect.FIRE)));
        this.tickedOnce = true;
      } else if (this.duration <= DURATION_1) {
        AbstractDungeon.actionManager.addToBottom(
            new DamageAllEnemiesAction(
                this.source, this.multiDamage, this.damageType, this.attackEffect, this.isFast));

        CreatureUtils.getLivingMonsters().forEach(mo ->
          AbstractDungeon.actionManager.addToBottom(
              new ApplyPowerAction(mo, this.source,
                  new FlamesOfTheHammerPower(mo, this.source, this.numDebuffs), this.numDebuffs)));

        this.isDone = true;
      }
      this.tickDuration();
    }
  }


  @Override
  public void applyUpgrades(List<AbstractSignatureMoveUpgrade> upgradesToApply) {
  }

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    return new PhoenixSplash();
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