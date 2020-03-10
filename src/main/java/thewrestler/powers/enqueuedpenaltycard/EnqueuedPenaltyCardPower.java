package thewrestler.powers.enqueuedpenaltycard;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.powers.AbstractWrestlerPower;
import thewrestler.util.BasicUtils;
import thewrestler.WrestlerMod;
import java.util.LinkedList;

public abstract class EnqueuedPenaltyCardPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("EnqueuedPenaltyCardPower");
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static final PowerType POWER_TYPE = PowerType.BUFF;

  protected final String baseID;
  protected final String cardName;

  public EnqueuedPenaltyCardPower(AbstractCreature owner, int amount, String baseID, String cardName,
                                  String imgFilePath) {
    super(generateId(baseID), getPowerName(cardName), imgFilePath, owner, owner, amount, POWER_TYPE);
    this.baseID = baseID;
    this.cardName = cardName;
    updateDescription();
  }

  public static class GainEnqueuedPenaltyCardPowerAction extends AbstractGameAction {

    private final EnqueuedPenaltyCardPower power;

    public GainEnqueuedPenaltyCardPowerAction(EnqueuedPenaltyCardPower power) {
      this.power = power;
    }

    @Override
    public void update() {
      AbstractPlayer player = AbstractDungeon.player;
      if (player.hasPower(this.power.baseID)) {
        player.getPower(this.power.baseID).stackPower(this.amount);
      } else {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, this.power, this.power.amount));
      }
      this.isDone = true;
    }
  }

  private static String getPowerName(String cardName) {
    return NAME + cardName;
  }

  private static String generateId(String baseID) {
    return baseID + "_" + BasicUtils.generateRandomAlphanumeric(12);
  }

  public void triggerGainCard() {
    this.flash();
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
  }

  private static final LinkedList<String> enqueuedPowers = new LinkedList<>();

  @Override
  public void onInitialApplication() {
    enqueuedPowers.add(this.baseID);
  }

  @Override
  public void onDeath() {
    resetForCombat();
  }

  @Override
  public void onVictory() {
    resetForCombat();
  }

  @Override
  public void onRemove() {
    enqueuedPowers.remove(this.baseID);
  }

  public static void resetForCombat() {
    enqueuedPowers.clear();
  }

  private static EnqueuedPenaltyCardPower getNextEnqueuedPower() {
    return (EnqueuedPenaltyCardPower) AbstractDungeon.player.powers.stream()
        .filter(p -> p instanceof  EnqueuedPenaltyCardPower)
        .findFirst().orElse(null);
  }

  public static boolean playerHasAnyCardsEnqueued() {
    return getNextEnqueuedPower() != null;
  }

  public static AbstractPenaltyStatusCard getNextCard() {
    AbstractPenaltyStatusCard card = null;
    if (playerHasAnyCardsEnqueued()) {
      EnqueuedPenaltyCardPower power = getNextEnqueuedPower();

      if (power != null) {
        power.triggerGainCard();
        card = power.getCard();
      }
    }
    return card;
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0]
        + (this.amount == 1 ? DESCRIPTIONS[1] : this.amount + DESCRIPTIONS[2])
        + DESCRIPTIONS[3]
        + (this.cardName != null ? "#y" + this.cardName.replaceAll("(\\s)", " #y") : "")
        + DESCRIPTIONS[4];
  }

  protected abstract AbstractPenaltyStatusCard getCard();

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }


}