package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import basemod.devcommands.deck.Deck;
import basemod.helpers.TooltipInfo;
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
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import java.util.Arrays;
import java.util.List;

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

  public LowBlow() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
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

  private static List<AbstractTooltipKeyword> EXTRA_KEYWORDS = Arrays.asList(
      CustomTooltipKeywords.getTooltipKeyword(CustomTooltipKeywords.PENALTY_CARD)
  );

  @Override
  public List<TooltipInfo> getCustomTooltips() {
    return TooltipKeywords.getTooltipInfos(EXTRA_KEYWORDS);
  }

  @Override
  public void atStartOfCombat() {

  }

  @Override
  public void atTurnStartPreDraw(){
    this.selfRetain = false;
  }



  @Override
  public void onUnsportingChanged(int changeAmount, int newValue, boolean isEndOfTurnChange) {
    if (changeAmount  > 0) {
      AbstractDungeon.actionManager.addToBottom(new DiscardToHandAction(this));
      if (isEndOfTurnChange && !this.selfRetain) {
        this.selfRetain = true;
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