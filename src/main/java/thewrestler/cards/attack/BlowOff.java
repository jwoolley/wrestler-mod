package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
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

  private static final int DAMAGE = 2;
  private static final int DAMAGE_INCREASE = 2;
  private static final int DAMAGE_INCREASE_UPGRADE = 1;
  private static final int COST = 0;

  public BlowOff() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = this.misc = DAMAGE;
    this.baseMagicNumber = this.magicNumber = DAMAGE_INCREASE;
    this.isMultiDamage = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (p.hand.size() == 1) { // this card is still in hand, so otherwise empty
      this.exhaust = true;
      AbstractDungeon.actionManager.addToBottom(new SFXAction("WHISTLE_STEAM_1"));
      addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
    } else {
      final int damageIncrease = p.hand.size() - 1;
      AbstractDungeon.actionManager.addToTop(
          new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, damageIncrease, false));
      WrestlerMod.logger.info("BlowOff:use increasing damage by " + (damageIncrease - 1));
      AbstractDungeon.actionManager.addToBottom(new ModifyDamageAction(this.uuid, damageIncrease * this.magicNumber));
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
      this.upgradeMagicNumber(DAMAGE_INCREASE_UPGRADE);
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