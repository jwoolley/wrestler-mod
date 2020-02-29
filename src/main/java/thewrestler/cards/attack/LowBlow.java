package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import basemod.devcommands.deck.Deck;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.DeckToHandAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.cards.skill.AbstractSportsmanshipListener;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class LowBlow extends CustomCard implements AbstractSportsmanshipListener, StartOfCombatListener {
  public static final String ID = "WrestlerMod:LowBlow";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "lowblow.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int DAMAGE = 15;
  private static final int DAMAGE_UPGRADE = 5;
  private static final int COST = 2;

  private boolean selfRetained = false;

  public LowBlow() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.selfRetain = SportsmanshipInfo.isUnsporting();
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
  }

  @Override
  public void triggerOnEndOfPlayerTurn() {
    super.triggerOnEndOfPlayerTurn();
    if (this.selfRetain) {
      this.flash();
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new LowBlow();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      initializeDescription();
    }
  }

  @Override
  public void upgradeDamage(int damageAmount) {
    super.upgradeDamage(damageAmount);
    initializeDescription();
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

  @Override
  public void atStartOfCombat() {

  }

  @Override
  public void atTurnStartPreDraw(){
    if (this.selfRetained) {
      this.selfRetain = this.selfRetained = false;
    }
  }



  @Override
  public void onUnsportingChanged(int changeAmount, int newValue, boolean isEndOfTurnChange) {
    if (changeAmount  > 0) {
      AbstractDungeon.actionManager.addToBottom(new DiscardToHandAction(this));
      if (isEndOfTurnChange && !this.selfRetain) {
        this.selfRetain = true;
        this.selfRetained = true;
      }
    }
  }

  @Override
  public void onBecomeSporting() {

  }

  @Override
  public void onBecomeUnsporting() {
  }
}