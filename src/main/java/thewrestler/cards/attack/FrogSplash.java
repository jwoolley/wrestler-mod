package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thewrestler.cards.colorless.status.penalty.BluePenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.OrangePenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.RedPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.YellowPenaltyStatusCard;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.CreatureUtils;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;

import java.util.Arrays;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class FrogSplash extends CustomCard {
  public static final String ID = "WrestlerMod:FrogSplash";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "frogsplash.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 11;
  private static final int DAMAGE_UPGRADE = 3;
  private static final int WEAK_AMOUNT = 1;
  private static final int WEAK_AMOUNT_UPGRADE = 1;

  public FrogSplash() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = WEAK_AMOUNT;
    this.isMultiDamage = true;
    this.exhaust = true;
    this.cardsToPreview = new BluePenaltyStatusCard();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {

    AbstractDungeon.actionManager.addToBottom(
        new DamageAllEnemiesAction(p, this.multiDamage, this.damageType, AttackEffect.SMASH, true));

    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_POISON"));

    CreatureUtils.getLivingMonsters().forEach(mo -> {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(mo, p, new WeakPower(mo, this.magicNumber, false), this.magicNumber));
    });

    PenaltyCardInfo.gainPenaltyCard( new BluePenaltyStatusCard());
  }

  @Override
  public AbstractCard makeCopy() {
    return new FrogSplash();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.upgradeMagicNumber(WEAK_AMOUNT_UPGRADE);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}