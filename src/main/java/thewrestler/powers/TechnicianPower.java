package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.commons.lang3.StringUtils;
import thewrestler.WrestlerMod;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.util.CardUtil;

public class TechnicianPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("TechnicianPower");
  public static final String IMG = "technician.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public TechnicianPower(int amount) {
    super(POWER_ID, NAME, IMG, AbstractDungeon.player, AbstractDungeon.player, amount, POWER_TYPE);
  }

  @Override
  public void stackPower(int amount) {
    super.stackPower(amount);
    WrestlerMod.logger.info("TechnicianPower::stackPower called: " + amount);
    applyDiscount(amount);
  }

  @Override
  public void onInitialApplication() {
    WrestlerMod.logger.info("TechnicianPower::onInitialApplication called: " + this.amount);
    applyDiscount(this.amount);
  }

  private void applyDiscount(int amount) {
    WrestlerMod.logger.info("TechnicianPower::applyDiscount called: " + amount);
    CardUtil.forAllCardsInCombat(c -> c.updateCost(-amount), c -> c instanceof AbstractPenaltyStatusCard);
    WrestlerCharacter.getPenaltyCardInfo().forEachQueuedCard(c -> c.updateCost(-amount), c -> true);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0]
        + StringUtils.repeat(DESCRIPTIONS[1], this.amount)
        + DESCRIPTIONS[2];
  }

  @Override
  public AbstractPower makeCopy() {
    return new TechnicianPower(amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}