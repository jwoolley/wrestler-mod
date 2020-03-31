package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.actions.cards.skill.ShowboatAction;
import thewrestler.actions.cards.skill.SideRollAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Showboat extends CustomCard {
  public static final String ID = "WrestlerMod:Showboat";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "showboat.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = 1;

  private boolean shouldUpgrade = false;

  public Showboat() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(false), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new ShowboatAction(this.shouldUpgrade));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Showboat();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.shouldUpgrade = true;
      this.rawDescription = getDescription(true);
      initializeDescription();
    }
  }
  public static String getDescription(boolean shouldUpgrade) {
    return DESCRIPTION
        + (shouldUpgrade ? EXTENDED_DESCRIPTION[0] : "")
        + EXTENDED_DESCRIPTION[1];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}