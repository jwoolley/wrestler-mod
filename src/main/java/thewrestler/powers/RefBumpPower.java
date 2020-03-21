package thewrestler.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thewrestler.WrestlerMod;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.cards.colorless.status.penalty.AbstractPenaltyStatusCard;
import thewrestler.cards.skill.AbstractPenaltyCardListener;
import thewrestler.util.CardUtil;
import thewrestler.util.RefreshHandListener;

public class RefBumpPower extends AbstractWrestlerPower implements AbstractPenaltyCardListener {
  public static final String POWER_ID = WrestlerMod.makeID("RefBumpPower");
  public static final String IMG = "refbump.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public RefBumpPower() {
    super(POWER_ID, NAME, IMG, AbstractDungeon.player, AbstractDungeon.player, -1, POWER_TYPE);
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    if (isPlayer) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));
    }
  }

  @Override
  public void onCardDraw(AbstractCard card) {
    if (card.hasTag(WrestlerCardTags.PENALTY)) {
      card.setCostForTurn(-9);
    }
  }

  @Override
  public void onInitialApplication() {
    CardUtil.forAllCardsInCombat(c -> c.setCostForTurn(-9), c -> c.hasTag(WrestlerCardTags.PENALTY));
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0];
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }

  @Override
  public void onGainedWarningCard() {

  }

  @Override
  public void onGainedPenaltyCard(AbstractPenaltyStatusCard card) {
    card.setCostForTurn(-9);
  }
}
