package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.BasicUtils;
import thewrestler.util.info.approval.ApprovalInfo;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Redemption extends CustomCard implements AbstractApprovalListener, StartOfCombatListener {
  public static final String ID = "WrestlerMod:Redemption";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "redemption.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int BLOCK_AMOUNT = 12;
  private static final int BLOCK_AMOUNT_UPGRADE = 3;
  private static final int COST = 2;
  private static final int DISCOUNT = 1;
  private static final int APPROVAL_GAIN = 5;

  public Redemption() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(DISCOUNT), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    this.baseMagicNumber = this.magicNumber = APPROVAL_GAIN;
    calculateCost();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    if (ApprovalInfo.isUnpopular()) {
      WrestlerCharacter.getApprovalInfo().increaseApproval(APPROVAL_GAIN, false);
    }
  }

  private void calculateCost() {
    if (BasicUtils.isPlayerInCombat()) {
      if (ApprovalInfo.isUnpopular()) {
        modifyCostForCombat(-DISCOUNT);
        this.update();
      }
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Redemption();
  }

  public static String getDescription(int discount) {
    return DESCRIPTION + StringUtils.repeat(EXTENDED_DESCRIPTION[0], discount) + EXTENDED_DESCRIPTION[1];
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_AMOUNT_UPGRADE);
      initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }

  @Override
  public void onApprovalChanged(int changeAmount, int newValue, boolean isEndOfTurnChange) {
    if (newValue == 0 && changeAmount > 0) {
      modifyCostForCombat(DISCOUNT);
      if (this.cost == COST) {
        this.isCostModified = false;
      }
    }
  }

  @Override
  public void onBecomeLiked() { }

  @Override
  public void onBecomeDisliked() {
    modifyCostForCombat(-DISCOUNT);
  }

  @Override
  public void atStartOfCombat() {
    calculateCost();
  }
}