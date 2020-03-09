package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.GainGoldAction;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.TemporaryBufferPower;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class TakeAPowder extends CustomCard {
  public static final String ID = "WrestlerMod:TakeAPowder";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "takeapowder.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 2;
  private static final int TEMP_BUFFER_AMOUNT = 2;
  private static final int TEMP_BUFFER_AMOUNT_UPGRADE = 1;
  private static final int GOLD_AMOUNT = 10;

  private int goldAmount;

  public TakeAPowder() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(GOLD_AMOUNT, false), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this. baseMagicNumber = this.magicNumber = TEMP_BUFFER_AMOUNT;
    this.goldAmount = GOLD_AMOUNT;
    this.tags.add(AbstractCard.CardTags.HEALING);
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (PenaltyCardInfo.hasPenaltyCardInfo()) {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(p, p, new TemporaryBufferPower(p, this.magicNumber), this.magicNumber));
    } else {
      AbstractDungeon.actionManager.addToBottom(new GainGoldAction(this.goldAmount));
      if (this.upgraded) {
        AbstractDungeon.actionManager.addToBottom(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
      }
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new TakeAPowder();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(TEMP_BUFFER_AMOUNT_UPGRADE);
      this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
      this.rawDescription = getDescription(this.goldAmount, true);
      this.initializeDescription();
    }
  }


  private static String getDescription(int goldAmount, boolean isUpgraded) {
    return DESCRIPTION + goldAmount
        + (!isUpgraded ? EXTENDED_DESCRIPTION[0] :  EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}