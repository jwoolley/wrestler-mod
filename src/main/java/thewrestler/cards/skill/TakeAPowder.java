package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import thewrestler.cards.colorless.skill.*;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.info.approval.ApprovalInfo;

import java.util.ArrayList;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class TakeAPowder extends CustomCard {
  public static final String ID = "WrestlerMod:TakeAPowder";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "takeapowder.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 2;
  private static final int BUFFER_OR_POTION_AMOUNT = 1;
  private static final int BUFFER_OR_POTION_AMOUNT_UPGRADE = 1;

  public TakeAPowder() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this. baseMagicNumber = this.magicNumber = BUFFER_OR_POTION_AMOUNT;
    this.tags.add(AbstractCard.CardTags.HEALING);
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (ApprovalInfo.isPopular()) {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(p, p, new BufferPower(p, this.magicNumber), this.magicNumber));
    } else if (ApprovalInfo.isUnpopular()) {
      AbstractDungeon.actionManager.addToBottom(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
    } else {
      ArrayList<AbstractCard> options = new ArrayList<>();

      if (this.upgraded) {
        options.add(new PowderBufferUpgraded());
        options.add(new PowderHybrid());
        options.add(new PowderPotionUpgraded());
      } else {
        options.add(new PowderBuffer());
        options.add(new PowderPotion());
      }
      AbstractDungeon.actionManager.addToBottom(new ChooseOneAction(options));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new TakeAPowder();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(BUFFER_OR_POTION_AMOUNT_UPGRADE);
      this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
      this.initializeDescription();
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