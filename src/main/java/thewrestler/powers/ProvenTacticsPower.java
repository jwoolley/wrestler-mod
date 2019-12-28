package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.BeamCell;
import com.megacrit.cardcrawl.cards.green.Neutralize;
import com.megacrit.cardcrawl.cards.red.Flex;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import thewrestler.WrestlerMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProvenTacticsPower extends AbstractWrestlerPower implements CloneablePowerInterface {
  public static final String POWER_ID = WrestlerMod.makeID("ProvenTacticsPower");
  public static final String IMG = "proventactics.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  private static final List<AbstractCard> POSSIBLE_CARDS = new ArrayList<>();

  public ProvenTacticsPower(AbstractCreature owner, int amount) {
    super(POWER_ID, NAME, IMG, owner, owner, amount, POWER_TYPE);
  }

  @Override
  public void atStartOfTurnPostDraw() {
    flash();
    for (int i = 0; i < this.amount; i++) {
      AbstractCard card = getRandomCard().makeStatEquivalentCopy();
      UnlockTracker.markCardAsSeen(card.cardID);

      List<AbstractCard> handBefore = new ArrayList<>(AbstractDungeon.player.hand.group);

      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card));
      AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
        @Override
        public void update() {
          AbstractDungeon.player.hand.group.forEach(c -> {
            if (!handBefore.contains(c)
                && getPossibleCards().stream().anyMatch(card -> card.cardID.equals(c.cardID))
                && !c.exhaust && !c.exhaustOnUseOnce) {
                c.exhaustOnUseOnce = true;
                c.rawDescription += DESCRIPTIONS[6];
                c.initializeDescription();
                AbstractDungeon.player.hand.refreshHandLayout();
              }
          });
          this.isDone = true;
        }
      });
    }
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0]
        + (this.amount == 1
          ? (DESCRIPTIONS[3] + DESCRIPTIONS[4])
          : (DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + DESCRIPTIONS[3] + DESCRIPTIONS[5]));
  }

  @Override
  public AbstractPower makeCopy() {
    return new ProvenTacticsPower(owner, amount);
  }


  public AbstractCard getRandomCard() {
    List<AbstractCard> possibleCards = getPossibleCards();
    return possibleCards.get(AbstractDungeon.cardRandomRng.random(0,possibleCards.size() - 1));

  }

  public static List<AbstractCard> getPossibleCards() {
    if (POSSIBLE_CARDS.isEmpty()) {
      POSSIBLE_CARDS.add(new Flex());
      POSSIBLE_CARDS.add(new Neutralize());
      POSSIBLE_CARDS.add(new BeamCell());
    }
    return Collections.unmodifiableList(POSSIBLE_CARDS);
  }


  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
