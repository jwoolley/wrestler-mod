package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class CheapShot extends CustomCard {
  public static final String ID = "WrestlerMod:CheapShot";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String[] INFO_TEXT;
  public static final String IMG_PATH = "cheapshot.png";

  private static final String INFO_TEXT_ID = "OpeningAction";
  private static final CardStrings cardStrings;
  private static final UIStrings infoTextCardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 0;
  private static final int DAMAGE = 5;
  private static final int DAMAGE_UPGRADE = 1;
  private static final int VULNERABLE_AMOUNT = 2;
  private static final int VULNERABLE_AMOUNT_UPGRADE = 1;

  public CheapShot() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = VULNERABLE_AMOUNT;
    this.isInnate = true;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));

    if (m.getIntentBaseDmg() >= 0) {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(m, p, new VulnerablePower(p, this.magicNumber, false), this.magicNumber));
    } else {
      AbstractDungeon.effectList.add(
          new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY,
              3.0F, INFO_TEXT[0], true));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new CheapShot();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.upgradeMagicNumber(VULNERABLE_AMOUNT_UPGRADE);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    infoTextCardStrings = CardCrawlGame.languagePack.getUIString(INFO_TEXT_ID);
    INFO_TEXT = infoTextCardStrings.TEXT;
  }
}