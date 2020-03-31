package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.colorless.attack.Knee;
import thewrestler.cards.colorless.skill.FairPlay;
import thewrestler.cards.colorless.skill.FoulPlay;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.BravadoPower;

import java.util.ArrayList;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class PlayToTheCrowd extends CustomCard {
  public static final String ID = "WrestlerMod:PlayToTheCrowd";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "playtothecrowd.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  private static final int BRAVADO_AMOUNT = 1;

  public PlayToTheCrowd() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(false), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = BRAVADO_AMOUNT;
    this.baseBlock = this.block = FairPlay.BLOCK_AMOUNT;
    this.cardsToPreview = new Knee();
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new BravadoPower(p, this.magicNumber), this.magicNumber));

    ArrayList<AbstractCard> options = new ArrayList<>();
    options.add(new FairPlay());
    options.add(new FoulPlay());
    if (this.upgraded) {
      options.forEach(AbstractCard::upgrade);
    }
    AbstractDungeon.actionManager.addToBottom(new ChooseOneAction(options));
  }

  @Override
  public AbstractCard makeCopy() {
    return new PlayToTheCrowd();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeName();
      this.upgradeBlock(FairPlay.BLOCK_AMOUNT_UPGRADE);
      this.cardsToPreview.upgrade();
      this.rawDescription = getDescription(true);
      initializeDescription();
    }
  }

  private static String getDescription(boolean upgraded) {
    return upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}