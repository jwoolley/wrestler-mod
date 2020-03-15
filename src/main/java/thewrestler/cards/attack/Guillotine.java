package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import thewrestler.actions.cards.DamageEnemiesAction;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.effects.utils.combat.CleanFinishEffect;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.CreatureUtils;

import java.util.List;
import java.util.stream.Collectors;

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
  private static final int DAMAGE = 8;
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
    final AbstractGameAction guillotineAction =
        new GuillotineAction(p, this.damage, this.damageType,  AttackEffect.SLASH_HEAVY,
            0.25f, 0.4f, Settings.FAST_MODE);

    AbstractDungeon.actionManager.addToBottom(
        new DamageEnemiesAction(p, this.damage, this.damageType,
            AttackEffect.SLASH_VERTICAL, CreatureUtils.getLivingMonsters(), guillotineAction));
  }

  class GuillotineDamageAction extends AbstractGameAction {
    public GuillotineDamageAction(int damage) {
      this.amount = damage;
    }

    @Override
    public void update() {
      final List<AbstractMonster> guillotinedMonsters = null;

      if (!guillotinedMonsters.isEmpty()) {
        guillotinedMonsters.forEach(monster -> AbstractDungeon.actionManager.addToTop(new VFXAction(
            new WeightyImpactEffect(monster.hb.cX, monster.hb.cY, Color.SCARLET.cpy()), Settings.ACTION_DUR_XFAST)));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("GUILLOTINE_1"));
        AbstractDungeon.actionManager.addToBottom(
            new GuillotineAction(AbstractDungeon.player, 0, Guillotine.this.damageType,
            AttackEffect.SLASH_HEAVY, 0.25f, 0.4f, Settings.FAST_MODE));
      }
      this.isDone = true;
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
    private final float flashDuration;
    private final boolean isFast;
    private int currentTick;
    private final List<AbstractMonster> guillotinedMonsters;

    GuillotineAction(AbstractPlayer source, int damage, DamageInfo.DamageType damageType,
                     AttackEffect attackEffect, float flashDuration, float damageDuration, boolean isFast) {
      this.duration = DURATION_2 + damageDuration;
      this.amount = damage;
      this.actionType = ActionType.DAMAGE;
      this.source = source;
      this.damageType = damageType;
      this.attackEffect = attackEffect;
      this.flashDuration = flashDuration;
      this.isFast = isFast;
      this.currentTick = 0;

      guillotinedMonsters = CreatureUtils.getLivingMonsters().stream()
          .filter(mo -> mo.currentHealth * 2 <= mo.maxHealth)
          .collect(Collectors.toList());
    }

    @Override
    public void update() {
      if (guillotinedMonsters.isEmpty()) {
        this.isDone = true;
        return;
      }

      if (this.currentTick == 0) {
        this.currentTick++;
      } else if (this.duration <= DURATION_2 || this.isFast) {
        if (this.currentTick == 1) {
          AbstractDungeon.actionManager.addToTop(
              new VFXAction(new CleanFinishEffect(Color.SCARLET, null, this.flashDuration, this.flashDuration,
                  true)));
          this.currentTick++;
        } else if (currentTick == 2 && this.duration <= 0.1f || this.isFast && this.duration <= DURATION_2 / 2.0f) {
          AbstractDungeon.actionManager.addToBottom(
              new DamageEnemiesAction(this.source, this.amount, this.damageType, AttackEffect.SLASH_VERTICAL,
                  this.guillotinedMonsters));
          this.isDone = true;
        }
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