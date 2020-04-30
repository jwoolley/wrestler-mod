package thewrestler.signaturemoves.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import thewrestler.cards.colorless.status.penalty.YellowPenaltyStatusCard;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.UpgradeGroup;

import java.util.List;

public class WindUp extends AbstractSignatureMoveCard {
  public static final String ID = "WrestlerMod:WindUp";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "windup.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardTarget TARGET = AbstractCard.CardTarget.NONE;

  private static final int COST = 1;
  private static final int NUM_ATTACKS_DOUBLED = 2;
  private static final int ENERGY_GAIN = 2; // not parameterized; fix description if this is changed
  private static final boolean HAS_EXHAUST = false;
  private static final boolean HAS_RETAIN = true;

  public WindUp() {
    super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, TARGET, HAS_EXHAUST, HAS_RETAIN,
        YellowPenaltyStatusCard.class, YellowPenaltyStatusCard.class);
    this.baseMagicNumber = this.magicNumber = NUM_ATTACKS_DOUBLED;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new SFXAction("SPRING_WIND_UP_BOING_2"));
    AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GAIN));
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DoubleTapPower(p, this.magicNumber), this.magicNumber));
  }

  @Override
  public void applyUpgrades(List<AbstractSignatureMoveUpgrade> upgradesToApply) {
  }

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    return new WindUp();
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
    return EXTENDED_DESCRIPTION[0] + this.name;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(1); // TODO: remove this once upgrade system is in place
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}