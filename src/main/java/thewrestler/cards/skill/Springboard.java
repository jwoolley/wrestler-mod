package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.actions.GainGoldAction;
import thewrestler.actions.cards.skill.HardwayAction;
import thewrestler.actions.cards.skill.SpringboardAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Springboard extends CustomCard {
  public static final String ID = "WrestlerMod:Springboard";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "springboard.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = 1;
  private static final int CARDS_DRAWN = 3;
  private static final int CARDS_DRAWN_UPGRADE = 1;

  public Springboard() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(CARDS_DRAWN), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.exhaust = true;
    this.baseMagicNumber = this.magicNumber = CARDS_DRAWN;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    // TODO: add VFX/SFX
    AbstractDungeon.actionManager.addToBottom(new SpringboardAction(this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Springboard();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(CARDS_DRAWN_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber);
      initializeDescription();
    }
  }

  public static String getDescription(int amount) {
    return DESCRIPTION + (amount == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION [1])
        + EXTENDED_DESCRIPTION[2];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}