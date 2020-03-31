package thewrestler.cards.colorless.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import thewrestler.WrestlerMod;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class PowderBuffer extends CustomCard {
  public static final String ID = WrestlerMod.makeID("PowderBuffer");
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "powderbuffer1.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.SPECIAL;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = -2;
  private static final int BUFFER_AMOUNT = 1;

  public PowderBuffer() {
    this(NAME, IMG_PATH, BUFFER_AMOUNT);
  }

  public PowderBuffer(String name, String imgPath, int bufferAmount) {
    super(ID, name, getCardResourcePath(imgPath), COST, DESCRIPTION, TYPE,
        CardColor.COLORLESS, RARITY, TARGET);
    this.magicNumber = this.baseMagicNumber = bufferAmount;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    onChoseThisOption();
  }

  public void onChoseThisOption() {
    AbstractPlayer player = AbstractDungeon.player;
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(player, player, new BufferPower(player, this.magicNumber), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new PowderBuffer();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(1);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}