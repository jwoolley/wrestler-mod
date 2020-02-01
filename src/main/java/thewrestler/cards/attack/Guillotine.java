package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.effects.utils.combat.CleanFinishEffect;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.SprainPower;
import thewrestler.util.CreatureUtils;

import java.util.Arrays;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Guillotine extends CustomCard {
  public static final String ID = "WrestlerMod:Guillotine";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "guillotine.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 9;
  private static final int DAMAGE_UPGRADE = 3;

  public Guillotine() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.isMultiDamage = true;
    tags.add(WrestlerCardTags.DIRTY);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {

    AbstractDungeon.actionManager.addToBottom(
        new DamageAllEnemiesAction(p, this.multiDamage, this.damageType,
            AttackEffect.SLASH_VERTICAL, true));

    if (p.hand.size() == 1 && p.hand.group.get(0).uuid == this.uuid) {
      AbstractDungeon.actionManager.addToBottom(new SFXAction("GUILLOTINE_1"));
      AbstractDungeon.actionManager.addToBottom(new GuillotineAction(p, this.multiDamage, this.damageType,
          AttackEffect.SLASH_HEAVY, 0.25f, 0.4f, Settings.FAST_MODE));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Guillotine();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
    }
  }

  private static class GuillotineAction extends AbstractGameAction {
    private static final float DURATION_2 = 0.2f;
    private final int[] multiDamage;
    private final float flashDuration;
    private final boolean isFast;
    private boolean tickedOnce;

    GuillotineAction(AbstractPlayer source, int[] multiDamage, DamageInfo.DamageType damageType,
                    AttackEffect attackEffect, float flashDuration, float damageDuration, boolean isFast) {
      this.duration = DURATION_2 + damageDuration;
      this.actionType = ActionType.DAMAGE;
      this.source = source;
      this.multiDamage = Arrays.copyOf(multiDamage, multiDamage.length);
      this.damageType = damageType;
      this.attackEffect = attackEffect;
      this.flashDuration = flashDuration;
      this.isFast = isFast;
      this.tickedOnce = false;
    }

    @Override
    public void update() {
      if ((this.duration <= DURATION_2 || this.isFast) && !tickedOnce) {
        AbstractDungeon.actionManager.addToTop(
            new VFXAction(new CleanFinishEffect(Color.SCARLET, null, this.flashDuration, this.flashDuration, true)));
        this.tickedOnce = true;
      } else if (this.duration <= 0.1f || this.isFast && this.duration <= DURATION_2 / 2.0f) {
        AbstractDungeon.actionManager.addToBottom(
            new DamageAllEnemiesAction(this.source, this.multiDamage, this.damageType,
               this.attackEffect, true));
        this.isDone = true;
      }
      this.tickDuration();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}