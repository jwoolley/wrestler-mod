package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.Acrobatics;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.powers.BearHugPower;
import thewrestler.powers.InjuredPower;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class BearHug extends CustomCard {
  public static final String ID = "WrestlerMod:BearHug";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "bearhug.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 2;
  private static final int DAMAGE = 9;
  private static final int DAMAGE_UPGRADE = 3;

  private static final int NUM_TICKS = 2;
  private static final int NUM_TICKS_UPGRADE = 1;
  private static final int DEBUFFS_PER_TICK = 2;

  public BearHug() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(DEBUFFS_PER_TICK, NUM_TICKS), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = NUM_TICKS;
    this.misc = DEBUFFS_PER_TICK;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToTop(new SFXAction("ROAR_BEAST_1"));

    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));

    AbstractDungeon.actionManager.addToBottom(new ChainApplyInjuredAction(m, p, this.misc, this.magicNumber));
  }

  class ChainApplyInjuredAction extends AbstractGameAction {
    private final int repetitions;
    public ChainApplyInjuredAction(AbstractCreature target, AbstractCreature source, int amount, int repetitions) {
      this.duration = Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST;
      this.target = target;
      this.source = source;
      this.amount = amount;
      this.repetitions = repetitions;
    }

    @Override
    public void update() {
      if (repetitions > 0 && this.target != null && !this.target.isDeadOrEscaped()) {
        AbstractDungeon.actionManager.addToBottom(
            new VFXAction(new ClawEffect(this.target.hb.cX, this.target.hb.cY, Color.BROWN, Color.RED), 0.1F));

        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(
                this.target, this.source, new InjuredPower(this.target, this.source, this.amount), this.amount, true));

        if (this.repetitions > 1) {
          AbstractDungeon.actionManager.addToBottom(
              new ChainApplyInjuredAction(this.target, this.source, this.amount, this.repetitions - 1));
        }
      }
      this.isDone = true;
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new BearHug();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.upgradeMagicNumber(NUM_TICKS_UPGRADE);
      this.rawDescription = getDescription(this.misc, this.magicNumber);
      this.initializeDescription();
    }
  }

  public static String getDescription(int injuredPerTick, int numTicks) {
    return DESCRIPTION + injuredPerTick + EXTENDED_DESCRIPTION[0]
        + (numTicks == 2 ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[2]);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}