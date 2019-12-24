package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class CageMatch extends CustomCard {
  public static final String ID = WrestlerMod.makeID("CageMatch");

  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cagematch.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 2;
  private static final int UPGRADED_COST = 1;

  public CageMatch() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (m.hasPower(StunMonsterPower.POWER_ID)) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, p,StunMonsterPower.POWER_ID));
    }

    AbstractDungeon.actionManager.addToBottom(new ApplyGrappledAction(m, p));

    AbstractDungeon.getCurrRoom().monsters.monsters.stream()
        .filter(mo -> !mo.isDying && !mo.isDeadOrEscaped() && mo != m)
        .forEach(mo -> {
          AbstractDungeon.actionManager.addToBottom(
              new ApplyPowerAction(mo, p, new StunMonsterPower(mo)));
        });
  }

  @Override
  public AbstractCard makeCopy() {
    return new CageMatch();
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