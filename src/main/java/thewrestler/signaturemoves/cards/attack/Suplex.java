package thewrestler.signaturemoves.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import thewrestler.cards.colorless.status.penalty.RedPenaltyStatusCard;
import thewrestler.cards.colorless.status.penalty.YellowPenaltyStatusCard;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.signaturemoves.upgrades.UpgradeGroup;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Suplex extends AbstractSignatureMoveCard {
  public static final String ID = "WrestlerMod:Suplex";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "suplex.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int NUM_CARDS = 3;
  private static final int DAMAGE_INCREASE = 3;
  private static final boolean HAS_EXHAUST = false;
  private static final boolean HAS_RETAIN = false;


  private static final int DAMAGE = 8;
  private static final int DAMAGE_UPGRADE = 3;


  public Suplex() {
    super(ID, NAME, IMG_PATH, COST, getDescription(DAMAGE_INCREASE), TYPE, TARGET, HAS_EXHAUST, HAS_RETAIN,
        YellowPenaltyStatusCard.class, RedPenaltyStatusCard.class);
    this.baseMagicNumber = this.magicNumber = NUM_CARDS;
    this.baseDamage = this.damage = DAMAGE;
    this.misc = DAMAGE_INCREASE;
  }

  private static class SuplexAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    final AbstractPlayer player;

    final int damageIncrease;
    final boolean isFirstTick;

    final List<AbstractCard> candidateCards;

    SuplexAction(List<AbstractCard> candidateCards, int numCards, int damageIncrease, boolean isFirstTick) {
      this.duration = DURATION;
      this.actionType = ActionType.CARD_MANIPULATION;
      this.amount = numCards;
      this.damageIncrease = damageIncrease;
      this.player = AbstractDungeon.player;
      this.isFirstTick = isFirstTick;

      this.candidateCards = candidateCards.stream().limit(numCards).collect(Collectors.toList());
    }

    SuplexAction(AbstractCard originatingCard, int numCards, int damageIncrease) {
      this(getCandidateCards(originatingCard, AbstractDungeon.player.hand.group), numCards, damageIncrease, true);
    }

    private static List<AbstractCard> getCandidateCards(AbstractCard excludeCard, List<AbstractCard> cards) {
      List<AbstractCard> candidateCards = cards.stream()
          .filter(c -> c.type == CardType.ATTACK && !c.uuid.equals(excludeCard.uuid))
          .collect(Collectors.toList());
      Collections.shuffle(candidateCards);
      return candidateCards;
    }

    @Override
    public void update() {
      if (this.candidateCards.isEmpty()) {
        this.isDone = true;
        return;
      }

      if (this.duration < DURATION) {
        if (this.isFirstTick) {
          AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.SCARLET, 0.99F, 1.0f, true));
          AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SCARLET.cpy(), false));
        }
        CardCrawlGame.sound.play("DING_SIMPLE_1");
        final AbstractCard candidateCard = this.candidateCards.get(0);
        candidateCard.baseDamage += this.damageIncrease;
        candidateCard.upgradedDamage = true;
        candidateCard.superFlash(Color.SCARLET);
        candidateCard.glowColor = Color.FIREBRICK;

        if (candidateCards.size() > 1) {
          AbstractDungeon.actionManager.addToBottom(
              new SuplexAction(this.candidateCards.subList(1, this.candidateCards.size()), this.amount, this.damageIncrease, false));
        }
        this.isDone = true;
      }
      tickDuration();
    }
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8F));

    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
            AbstractGameAction.AttackEffect.BLUNT_HEAVY));

    AbstractDungeon.actionManager.addToBottom(new SuplexAction(this, this.magicNumber, this.misc));
  }

  @Override
  public void applyUpgrades(List<AbstractSignatureMoveUpgrade> upgradesToApply) {
  }

  @Override
  public AbstractSignatureMoveCard makeCopy() {
    return new Suplex();
  }

  @Override
  public UpgradeGroup getAllEligibleUpgrades() {
    // TODO: implement
    return null;
  }

  @Override
  public UpgradeGroup getCurrentEligibleUpgrades() {
    // TODO: implement
    return null;
  }

  @Override
  public String getIndefiniteCardName() {
    return EXTENDED_DESCRIPTION[0] + this.name;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(3); // TODO: remove this once upgrade system is in place
      this.upgradeDamage(DAMAGE_UPGRADE); // TODO: remove this once upgrade system is in place

      this.rawDescription = getDescription(DAMAGE_INCREASE);
      initializeDescriptionCN();
    }
  }

  private static String getDescription(int damageIncrease) {
    return DESCRIPTION + damageIncrease + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}