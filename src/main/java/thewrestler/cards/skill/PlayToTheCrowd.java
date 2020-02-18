package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.colorless.attack.Knee;
import thewrestler.cards.colorless.skill.KneedAFriend;
import thewrestler.cards.colorless.skill.Sidestep;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import java.util.ArrayList;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class PlayToTheCrowd extends CustomCard {
  public static final String ID = "WrestlerMod:PlayToTheCrowd";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "playtothecrowd.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  private static final int BLOCK = 4;
  private static final int BLOCK_UPGRADE = 2;

  private boolean upgradeKnee;

  public PlayToTheCrowd() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
    this.cardsToPreview = new Knee();
    this.upgradeKnee = false;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (SportsmanshipInfo.hasSportsmanshipInfo() && SportsmanshipInfo.isUnsporting()) {
      AbstractCard card = this.cardsToPreview.makeStatEquivalentCopy();
      if (this.upgradeKnee) {
        card.upgrade();
      }
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, SportsmanshipInfo.getAmount()));
    } else {
      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new PlayToTheCrowd();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_UPGRADE);
      this.cardsToPreview.upgrade();
      upgradeKnee = true;
      this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
      initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}