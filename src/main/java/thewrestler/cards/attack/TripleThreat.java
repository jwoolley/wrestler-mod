package thewrestler.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.cards.AbstractCardWithPreviewCard;
import thewrestler.cards.colorless.attack.Elbow;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.TripleThreatPower;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class TripleThreat extends AbstractCardWithPreviewCard {
  public static final String ID = "WrestlerMod:TripleThreat";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "triplethreat.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;
  private static AbstractCard PREVIEW_CARD;

  private static final int DAMAGE = 5;
  private static final int DAMAGE_UPGRADE = 2;
  private static final int COST = 1;

  public TripleThreat() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(Elbow.NAME), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
  }

  @Override
  public AbstractCard getPreviewCard() {
    return PREVIEW_CARD;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p,
            new TripleThreatPower(m, 1, getPreviewCard().makeStatEquivalentCopy()), 1, true));
  }

  @Override
  public AbstractCard makeCopy() {
    return new TripleThreat();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(DAMAGE_UPGRADE);
      this.rawDescription = getDescription(getBonusCard().name);
      initializeDescription();
    }
  }

  private static AbstractCard getBonusCard() {
    if (PREVIEW_CARD == null) {
      PREVIEW_CARD = new Elbow();
    }
    return PREVIEW_CARD;
  }

  private static String getDescription(String bonusCardName) {
    return DESCRIPTION + WrestlerMod.getModKeywordPrefix() + bonusCardName + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    PREVIEW_CARD = new Elbow();
  }
}