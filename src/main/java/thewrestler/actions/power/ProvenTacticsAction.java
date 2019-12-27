package thewrestler.actions.power;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.GoForTheEyes;
import com.megacrit.cardcrawl.cards.green.Neutralize;
import com.megacrit.cardcrawl.cards.red.Flex;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.omg.PortableInterceptor.ACTIVE;
import org.omg.PortableServer.POA;
import thewrestler.actions.cards.skill.SpringboardEnergyAction;
import thewrestler.powers.WrestlerShackled;
import thewrestler.util.CreatureUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProvenTacticsAction extends AbstractGameAction  {
  private static final float DURATION = Settings.ACTION_DUR_FAST;

  static final List<AbstractCard> POSSIBLE_CARDS = new ArrayList<>();

  public ProvenTacticsAction(int amount) {
    setValues(null, AbstractDungeon.player, amount);
    this.actionType = ActionType.SPECIAL;
    this.duration = DURATION;
  }

  public void update() {
    if (this.duration == DURATION) {

      this.isDone = true;
    }
    this.tickDuration();
  }

  public AbstractCard getRandomCard() {
    List<AbstractCard> possibleCards = getPossibleCards();
    return possibleCards.get(AbstractDungeon.cardRandomRng.random(0,possibleCards.size() - 1));
  }

  public static List<AbstractCard> getPossibleCards() {
    if (POSSIBLE_CARDS.isEmpty()) {
      POSSIBLE_CARDS.add(new Flex());
      POSSIBLE_CARDS.add(new Neutralize());
      POSSIBLE_CARDS.add(new GoForTheEyes());
    }
    return Collections.unmodifiableList(POSSIBLE_CARDS);
  }
}