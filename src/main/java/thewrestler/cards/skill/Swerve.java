package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.cards.skill.SideRollAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Swerve extends CustomCard {
  public static final String ID = "WrestlerMod:Swerve";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "swerve.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int BLOCK_AMOUNT = 6;
  private static final int BLOCK_AMOUNT_UPGRADE = 2;
  private static final int BLOCK_ON_DISCARD = 3;
  private static final int BLOCK_ON_DISCARD_UPGRADE = 1;
  private static final int COST = 1;

  public Swerve() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    this.baseMagicNumber = this.magicNumber = BLOCK_ON_DISCARD;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    applyPowers();
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
  }

  @Override
  public void triggerOnManualDiscard() {
    final AbstractPlayer player = AbstractDungeon.player;
    this.superFlash();
    applyPowers();
    AbstractDungeon.actionManager.addToTop(new SFXAction("WHOOSH_ROCKET_1"));
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, player, this.magicNumber));
  }


  public void applyPowers() {
    this.baseBlock = BLOCK_ON_DISCARD + (this.upgraded ? BLOCK_ON_DISCARD_UPGRADE : 0);
    this.baseMagicNumber = this.baseBlock;
    super.applyPowers();
    this.magicNumber = this.block;
    this.isMagicNumberModified = this.isBlockModified;
    this.baseBlock = BLOCK_AMOUNT + (this.upgraded ? BLOCK_AMOUNT_UPGRADE : 0);
    super.applyPowers();
  }

  @Override
  public AbstractCard makeCopy() {
    return new Swerve();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_AMOUNT_UPGRADE);
      this.upgradeMagicNumber(BLOCK_ON_DISCARD_UPGRADE);
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
}