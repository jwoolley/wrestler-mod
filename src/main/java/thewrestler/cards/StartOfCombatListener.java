package thewrestler.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.cards.skill.AbstractApprovalListener;
import thewrestler.util.RefreshHandListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface StartOfCombatListener {
  void atStartOfCombat();

  static public void triggerStartOfCombatCards() {
    final List<StartOfCombatListener> cards = new ArrayList<>();

    AbstractPlayer player = AbstractDungeon.player;
    cards.addAll(player.drawPile.group.stream()
        .filter(c -> c instanceof AbstractApprovalListener).map(c -> (StartOfCombatListener)c).collect(Collectors.toList()));
    cards.addAll(player.hand.group.stream()
        .filter(c -> c instanceof AbstractApprovalListener).map(c -> (StartOfCombatListener)c).collect(Collectors.toList()));
    cards.addAll(player.discardPile.group.stream()
        .filter(c -> c instanceof AbstractApprovalListener).map(c -> (StartOfCombatListener)c).collect(Collectors.toList()));
    cards.addAll(player.exhaustPile.group.stream()
        .filter(c -> c instanceof AbstractApprovalListener).map(c -> (StartOfCombatListener)c).collect(Collectors.toList()));

    cards.forEach(StartOfCombatListener::atStartOfCombat);
  }

  static public void triggerStartOfCombatPowers() {
    AbstractDungeon.player.powers.stream().filter(p -> p instanceof StartOfCombatListener)
        .forEach(p -> ((StartOfCombatListener)p).atStartOfCombat());
  }
}
