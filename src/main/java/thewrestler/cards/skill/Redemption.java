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
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Redemption extends CustomCard implements AbstractSportsmanshipListener, StartOfCombatListener {
  public static final String ID = "WrestlerMod:Redemption";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "redemption.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int BLOCK_AMOUNT = 20;
  private static final int BLOCK_AMOUNT_UPGRADE = 6;
  private static final int COST = 2;

  public Redemption() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    calculateCost();
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    WrestlerCharacter.getSportsmanshipInfo().resetUnsporting(false);
  }

  private void calculateCost() {
    if (BasicUtils.isPlayerInCombat()) {
      if (SportsmanshipInfo.isUnsporting()) {
        modifyCostForCombat(SportsmanshipInfo.getAmount());
        this.update();
      }
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Redemption();
  }

  public static String getDescription() {
    return DESCRIPTION;
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
  public void onUnsportingChanged(int changeAmount, int newValue, boolean isEndOfTurnChange) {
    if (newValue == 0 && changeAmount > 0) {
      modifyCostForCombat(SportsmanshipInfo.getAmount());
      if (this.cost == COST) {
        this.isCostModified = false;
      }
    }
  }

  @Override
  public void onBecomeSporting() { }

  @Override
  public void onBecomeUnsporting() {
    modifyCostForCombat(-SportsmanshipInfo.getAmount());
  }

  @Override
  public void atStartOfCombat() {
    calculateCost();
  }
}