package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.InjuredPower;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Octopus extends CustomCard {
  public static final String ID = "WrestlerMod:Octopus";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "octopus.png";

  private static final int DEBUFF_APPLICATIONS = 8;
  private static final int DEBUFF_AMOUNT_PER_APPLICATION = 1;

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = 1;

  public Octopus() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(DEBUFF_APPLICATIONS, true), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = DEBUFF_AMOUNT_PER_APPLICATION;
    this.misc = DEBUFF_APPLICATIONS;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new OctopusAction(m, this.magicNumber, this.misc));
  }

  private static class OctopusAction extends AbstractGameAction {
    private static final float EFFECT_DURATION = 0.01f;
    private final int numReps;

    public OctopusAction(AbstractCreature target, int amountPerRep, int numReps) {
      this.target = target;
      this.amount = amountPerRep;
      this.numReps = numReps;
      this.duration = EFFECT_DURATION;
    }

    public void update() {
      this.target = AbstractDungeon.getRandomMonster();

      if (numReps <= 0 || this.target == null) {
        this.isDone = true;
        return;
      }

      if (!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        trigger();

        AbstractDungeon.actionManager.addToBottom(
            new OctopusAction(this.target, this.amount, this.numReps - 1));
      } else {
        AbstractDungeon.actionManager.clearPostCombatActions();
      }
      this.isDone = true;
    }

    private void trigger() {
      AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY,
          AbstractGameAction.AttackEffect.BLUNT_LIGHT,this.target.hasPower(ArtifactPower.POWER_ID)));

      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(this.target, AbstractDungeon.player, new InjuredPower(this.target, this.source, this.amount),
              this.amount));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Octopus();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.exhaust = false;
      this.rawDescription = getDescription(DEBUFF_APPLICATIONS, false);
      initializeDescription();
    }
  }
  public static String getDescription(int numTimesApplied, boolean willExhaust) {
    return DESCRIPTION + numTimesApplied  + (!willExhaust ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1]);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}