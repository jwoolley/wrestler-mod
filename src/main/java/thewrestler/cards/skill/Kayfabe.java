package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;
import thewrestler.actions.GainApprovalAction;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.TarnishedReputationPower;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Kayfabe extends CustomCard {
  public static final String ID = "WrestlerMod:Kayfabe";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "kayfabe.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = 1;
  private static final int ENERGY_AMOUNT = 2;
  private static final int ENERGY_UPGRADE = 1;

  private static final int APPROVAL_GAIN = 5;
  private static final int APPROVAL_LOSS = 10;

  public Kayfabe() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(ENERGY_AMOUNT), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = ENERGY_AMOUNT;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(new GainApprovalAction(APPROVAL_GAIN));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new TarnishedReputationPower(APPROVAL_LOSS), APPROVAL_LOSS));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Kayfabe();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(ENERGY_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber);
      initializeDescription();
    }
  }
  public static String getDescription(int energyAmount) {
    return DESCRIPTION
        + StringUtils.repeat(EXTENDED_DESCRIPTION[0], energyAmount)
        + EXTENDED_DESCRIPTION[1] + APPROVAL_GAIN + EXTENDED_DESCRIPTION[2]
        + EXTENDED_DESCRIPTION[3] + APPROVAL_LOSS + EXTENDED_DESCRIPTION[2]
        + EXTENDED_DESCRIPTION[4];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}