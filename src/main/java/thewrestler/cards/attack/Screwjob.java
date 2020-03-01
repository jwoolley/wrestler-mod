package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.skill.AbstractSportsmanshipListener;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Screwjob extends CustomCard implements AbstractSportsmanshipListener {
  public static final String ID = "WrestlerMod:Screwjob";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "screwjob.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 3;
  private static final int NUM_REPS = 3;
  private static final int DAMAGE_INCREASE_PER_PENALTY_CARD = 2;
  private static final int DAMAGE_INCREASE_PER_PENALTY_CARD_UPGRADE = 1;

  public Screwjob() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = this.misc = DAMAGE;
    this.baseMagicNumber = this.magicNumber = DAMAGE_INCREASE_PER_PENALTY_CARD;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ScrewjobAction(m, this.damage, NUM_REPS, true, this.magicNumber, this.uuid, this));
  }

  @Override
  public void onUnsportingChanged(int changeAmount, int newValue, boolean isEndOfTurnChange) {
    if (changeAmount > 0) {
      AbstractDungeon.actionManager.addToBottom(new ModifyDamageAction(this.uuid, this.magicNumber * changeAmount));
    }
  }

  @Override
  public void onBecomeSporting() {

  }

  @Override
  public void onBecomeUnsporting() {

  }

  private static class ScrewjobAction extends AbstractGameAction {
    private static final float EFFECT_DURATION = 0.1f;
    private static final float DAMAGE_DURATION = 0.1f;
    private static final DamageInfo.DamageType DAMAGE_TYPE = DamageInfo.DamageType.NORMAL;
    private int increasePerUnsporting;
    private final int damage;
    private final int numReps;
    private boolean isFirstRep;

    private UUID uuid;
    private Screwjob card;

    public ScrewjobAction(AbstractCreature target, int damage, int numReps, boolean isFirstRep, int incAmount,
                          UUID targetUUID, Screwjob card) {
      this.target = target;
      this.damage = damage;
      this.numReps = numReps;
      this.isFirstRep = isFirstRep;
      this.increasePerUnsporting = incAmount;
      this.actionType = AbstractGameAction.ActionType.DAMAGE;
      this.duration = EFFECT_DURATION + DAMAGE_DURATION;
      this.uuid = targetUUID;
      this.card = card;
    }

    public void update() {
      if (numReps <= 0 || this.target == null) {
        this.isDone = true;
        return;
      }

      if (this.isFirstRep) {
          if (SportsmanshipInfo.isUnsporting()) {
            this.card.flash();
            this.upgradeCardDamage();
          }
          CardCrawlGame.sound.play("DRILL_SPIN_1");
      }
      final DamageInfo info =  new DamageInfo(AbstractDungeon.player, this.damage, DAMAGE_TYPE);
      AbstractDungeon.actionManager.addToBottom(new DamageAction(this.target, info, AttackEffect.SLASH_HORIZONTAL,
          true));

        if (!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
          AbstractDungeon.actionManager.addToBottom(
              new ScrewjobAction(this.target, this.damage, this.numReps - 1, false,
                  0, null, null));
        } else {
          AbstractDungeon.actionManager.clearPostCombatActions();
        }
        this.isDone = true;
    }

    private void upgradeCardDamage() {
      final int damageIncrease = SportsmanshipInfo.getAmount() * this.increasePerUnsporting;
      for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
        if (c.uuid.equals(this.uuid)) {
          c.misc += damageIncrease;
          c.applyPowers();
          c.baseDamage = c.misc;
          c.isDamageModified = false;
        }
      }
      for (AbstractCard c : GetAllInBattleInstances.get(this.uuid)) {
        c.misc += damageIncrease;
        c.applyPowers();
        c.baseDamage = c.misc;
      }
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Screwjob();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(DAMAGE_INCREASE_PER_PENALTY_CARD_UPGRADE);
      this.rawDescription = getDescription();
      this.initializeDescription();
    }
  }

  public static String getDescription() {
    return DESCRIPTION;
  }

  private static List<AbstractTooltipKeyword> EXTRA_KEYWORDS = Arrays.asList(
      CustomTooltipKeywords.getTooltipKeyword(CustomTooltipKeywords.PENALTY_CARD)
  );

  @Override
  public List<TooltipInfo> getCustomTooltips() {
    return TooltipKeywords.getTooltipInfos(EXTRA_KEYWORDS);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}