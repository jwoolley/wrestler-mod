package thewrestler.signaturemoves.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ChokePower;
import thewrestler.cards.colorless.attack.Knee;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;

import java.util.Map;

// TODO: this should extend? implement? AbstractPreviewCard and

public class Chokeslam extends AbstractSignatureMoveCard {
  public static final String ID = "WrestlerMod:Chokeslam";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "chokeslam.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 10;
  private static final int NUM_CARD_COPIES = 2;
  private static final int NUM_CHOKE_STACKS = 1;

  private static AbstractCard BONUS_CARD;

  private int numChokeStacks;

  public Chokeslam() {
    super(ID, NAME, IMG_PATH, COST, getDescription(), TYPE, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = NUM_CARD_COPIES;
    this.numChokeStacks = NUM_CHOKE_STACKS;
  }

  private AbstractCard makeBonusCard() {
    if (BONUS_CARD == null) {
      BONUS_CARD = new Knee();
    }
    AbstractCard bonusCard = BONUS_CARD.makeCopy();
    bonusCard.setCostForTurn(0);
    return bonusCard;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
            AbstractGameAction.AttackEffect.BLUNT_HEAVY));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new ChokePower(m, this.numChokeStacks), this.numChokeStacks));

    for (int i = 0; i < this.magicNumber; i++) {
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(this.makeBonusCard()));
    }
  }

  @Override
  protected void applyUpgrades(Map<AbstractSignatureMoveUpgrade, Integer> upgrades) {

  }

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    return new Chokeslam();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.rawDescription = getDescription();
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