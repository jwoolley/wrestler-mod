package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class BlowOff extends CustomCard {
  public static final String ID = "WrestlerMod:BlowOff";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "blowoff.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int DAMAGE = 1;
  private static final int DAMAGE_INCREASE = 2;
  private static final int COST = 1;
  private static final int UPGRADED_COST = 0;

  public BlowOff() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = this.misc = DAMAGE;
    this.baseMagicNumber = this.magicNumber = DAMAGE_INCREASE;
    this.isMultiDamage = true;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
      AbstractDungeon.actionManager.addToBottom(new SFXAction("WHISTLE_STEAM_1"));
      AbstractDungeon.actionManager.addToBottom(
          new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
  }

  @Override
  public void onPlayCard(AbstractCard card, AbstractMonster target) {
    final boolean isInHand = AbstractDungeon.player.hand.group.stream().anyMatch(c -> c.uuid == this.uuid);
    if (isInHand && card.type == CardType.SKILL) {
      AbstractDungeon.actionManager.addToTop(new ModifyDamageAction(this.uuid, this.magicNumber));
      this.rawDescription = getDescription();
      initializeDescription();
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new BlowOff();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBaseCost(UPGRADED_COST);
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