package thewrestler.signaturemoves.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ChokePower;
import thewrestler.cards.colorless.attack.Elbow;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.UpgradeGroup;
import thewrestler.signaturemoves.upgrades.UpgradeRarity;
import thewrestler.signaturemoves.upgrades.UpgradeType;

import java.util.List;

// TODO: implement getTooltips() method for Grapple and Knee thewrestler.keywords

public class Chokeslam extends AbstractSignatureMoveCard {
  public static final String ID = "WrestlerMod:Chokeslam";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "chokeslam.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 8;
  private static final int NUM_CARD_COPIES = 1;
  private static final int NUM_CHOKE_STACKS = 1;
  private static final boolean HAS_EXHAUST = true;
  private static final boolean HAS_RETAIN = false;

  private boolean bonusCardIsFreeToPlay;

  private static AbstractCard BONUS_CARD;

  private int numChokeStacks;

  private static final UpgradeGroup ELIGIBLE_UPGRADES;
  private static final int DAMAGE_UPGRADE_COMMON = 5;
  private static final int DAMAGE_UPGRADE_RARE = 8;
  private static final int COST_REDUCTION = 1;

  public Chokeslam() {
    super(ID, NAME, IMG_PATH, COST, getDescription(HAS_RETAIN, HAS_EXHAUST, NUM_CARD_COPIES, false),
        TYPE, TARGET, HAS_EXHAUST, HAS_RETAIN);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = NUM_CARD_COPIES;
    this.numChokeStacks = NUM_CHOKE_STACKS;
    bonusCardIsFreeToPlay = false;
  }

  private AbstractCard makeBonusCard() {
    if (BONUS_CARD == null) {
      BONUS_CARD = new Elbow();
    }
    AbstractCard bonusCard = BONUS_CARD.makeCopy();

    if (this.bonusCardIsFreeToPlay) {
      bonusCard.setCostForTurn(0);
    }
    return bonusCard;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
            AbstractGameAction.AttackEffect.BLUNT_HEAVY));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new ChokePower(m, this.numChokeStacks), this.numChokeStacks));

    for (int i = 0; i < this.magicNumber; i++) {
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(this.makeBonusCard()));
    }
  }

  @Override
  public void applyUpgrades(List<AbstractSignatureMoveUpgrade> upgradesToApply) {
    upgradesToApply.forEach(upgrade -> {
      if (upgrade.type == UpgradeType.DAMAGE) {
        this.upgradeDamage(upgrade.rarity == UpgradeRarity.RARE ? DAMAGE_UPGRADE_RARE : DAMAGE_UPGRADE_COMMON);
      } else if (upgrade.type == UpgradeType.COST_REDUCTION && this.cost > 0) {
        this.upgradeBaseCost(this.cost - COST_REDUCTION);
      } else if (upgrade.type == UpgradeType.RETAIN) {
        this.selfRetain = true;
      } else if (upgrade.type == UpgradeType.LOSE_EXHAUST) {
        this.exhaust = false;
      }
      this.timesUpgraded++;
      this.upgraded = true;
      this.upgradeList.add(upgrade);
    });

    updateDescription();
    upgradeName(this.upgradeList);
  }

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    return new Chokeslam();
  }

  @Override
  public UpgradeGroup getAllEligibleUpgrades() {
    return ELIGIBLE_UPGRADES;
  }

  @Override
  public UpgradeGroup getCurrentEligibleUpgrades() {
    return null;
  }

  @Override
  public String getIndefiniteCardName() {
    return EXTENDED_DESCRIPTION[6] + this.name;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      updateDescription();
    }
  }

  private void updateDescription() {
    this.rawDescription = getDescription(this.selfRetain, this.exhaust, this.magicNumber, this.bonusCardIsFreeToPlay);
    initializeDescription();
  }

  public static String getDescription(boolean hasRetain, boolean hasExhaust, int numBonusCards, boolean bonusIsFree) {
    return
        (hasRetain ? EXTENDED_DESCRIPTION[6] : "")
        + DESCRIPTION + (numBonusCards == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2]
        + (bonusIsFree
            ? (numBonusCards == 1 ? EXTENDED_DESCRIPTION[3] : EXTENDED_DESCRIPTION[4]) + EXTENDED_DESCRIPTION[5]
            : "")
        +  (hasExhaust ? EXTENDED_DESCRIPTION[7] : "");
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    ELIGIBLE_UPGRADES = new UpgradeGroup() {{
      put(new AbstractSignatureMoveUpgrade(UpgradeType.DAMAGE, UpgradeRarity.COMMON), 999);
      put(new AbstractSignatureMoveUpgrade(UpgradeType.COST_REDUCTION, UpgradeRarity.COMMON), 1);
      put(new AbstractSignatureMoveUpgrade(UpgradeType.RETAIN, UpgradeRarity.COMMON), 1);
      put(new AbstractSignatureMoveUpgrade(UpgradeType.LOSE_EXHAUST, UpgradeRarity.COMMON), 1);
    }};
  }
}