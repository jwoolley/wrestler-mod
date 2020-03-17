package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.cards.colorless.status.penalty.BluePenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.RedPenaltyStatusCard;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.ShortarmPower;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Shortarm extends CustomCard {
  public static final String ID = "WrestlerMod:Shortarm";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "shortarm.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 4;
  private static final int DAMAGE_UPGRADE = 2;
  private static final int TEMP_STRENGTH_AMOUNT = 2;
  private static final int TEMP_STRENGTH_AMOUNT_UPGRADE = 2;

  public Shortarm() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = TEMP_STRENGTH_AMOUNT;
    this.cardsToPreview = new RedPenaltyStatusCard();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new ShortarmPower(p, this.magicNumber), this.magicNumber));

    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber),
        this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseStrengthPower(p, this.magicNumber),
        this.magicNumber));

    PenaltyCardInfo.gainPenaltyCard( new RedPenaltyStatusCard());
  }

  @Override
  public AbstractCard makeCopy() {
    return new Shortarm();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.upgradeMagicNumber(TEMP_STRENGTH_AMOUNT_UPGRADE);
      initializeDescription();
    }
  }

  private static String getDescription() {
    return DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}