package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import thewrestler.actions.cards.skill.SideRollAction;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.NearFallPower;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class NearFall extends CustomCard {
  public static final String ID = "WrestlerMod:NearFall";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "nearfall.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;

  private static final int NUM_CARDS_EXHAUST = 1;

  public NearFall() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(true), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.magicNumber = this.baseMagicNumber = NUM_CARDS_EXHAUST;
    this.isEthereal = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new ExhaustAction(this.magicNumber,false));
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NearFallPower()));
  }

  @Override
  public AbstractCard makeCopy() {
    return new NearFall();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.rawDescription = getDescription(false);
      initializeDescription();
    }
  }
  public static String getDescription(boolean isEthereal) {
    return (isEthereal ? EXTENDED_DESCRIPTION[0] : "") + DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}