package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import sun.nio.cs.ext.PCK;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;
import thewrestler.cards.colorless.attack.Knee;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.GreenPenaltyStatusCard;
import thewrestler.cards.skill.Cloverleaf;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.CloverleafPower;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class CloverleafAttack extends CustomCard {
  public static final String ID = WrestlerMod.makeID("CloverleafAttack");

  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cloverleafattack.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE_AMOUNT = 6;
  private static final int DAMAGE_AMOUNT_UPGRADE = 2;
  private static final int BLOCK_AMOUNT = 3;
  private static final int BLOCK_AMOUNT_UPGRADE = 1;

  public CloverleafAttack() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(BLOCK_AMOUNT), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.damage = this.baseDamage = DAMAGE_AMOUNT;
    this.misc = BLOCK_AMOUNT;
    this.exhaust = true;

    this.cardsToPreview = new GreenPenaltyStatusCard();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
            AbstractGameAction.AttackEffect.SLASH_HEAVY));

//    AbstractDungeon.actionManager.addToBottom(
//        new ApplyPowerAction(m, p, new CloverleafPower(p, this.misc), this.misc));
    AbstractPenaltyStatusCard penaltyCard = new GreenPenaltyStatusCard();
    penaltyCard.applyEnqueuedCardPower(1);
  }

  @Override
  public AbstractCard makeCopy() {
    return new CloverleafAttack();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_AMOUNT_UPGRADE);
      this.misc += BLOCK_AMOUNT_UPGRADE;
      this.rawDescription = getDescription(this.misc);
      initializeDescription();
    }
  }

  public static String getDescription(int bloackAmount) {
    return DESCRIPTION + bloackAmount + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}