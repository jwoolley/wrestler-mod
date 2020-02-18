package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Pinfall extends CustomCard implements AbstractSportsmanshipListener, StartOfCombatListener {
  public static final String ID = "WrestlerMod:Pinfall";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "pinfall.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int BLOCK_AMOUNT = 8;
  private static final int BLOCK_AMOUNT_UPGRADE = 3;
  private static final int COST = 1;

  public Pinfall() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    this.selfRetain = SportsmanshipInfo.isSporting();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
  }

  @Override
  public void triggerOnEndOfPlayerTurn() {
    super.triggerOnEndOfPlayerTurn();
    if (this.selfRetain) {
      this.flash();
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Pinfall();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_AMOUNT_UPGRADE);
      this.rawDescription = getDescription();
      initializeDescription();
    }
  }
  public static String getDescription() {
    return DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }

  @Override
  public void atStartOfCombat() {
    this.selfRetain = SportsmanshipInfo.isSporting();
  }

  @Override
  public void atTurnStartPreDraw() {
    this.selfRetain = SportsmanshipInfo.isSporting();
  }

  @Override
  public void onUnsportingChanged(int changeAmount, int newValue, boolean isEndOfTurnChange) {
    if (!isEndOfTurnChange || isEndOfTurnChange) {
      this.selfRetain = SportsmanshipInfo.isSporting();
    }
  }

  @Override
  public void onBecomeSporting() {

  }

  @Override
  public void onBecomeUnsporting() {

  }
}