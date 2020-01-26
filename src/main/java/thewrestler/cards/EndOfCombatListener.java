package thewrestler.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thewrestler.cards.skill.AbstractApprovalListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface EndOfCombatListener {
  void atEndOfCombat();
}
