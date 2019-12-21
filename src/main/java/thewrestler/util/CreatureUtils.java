package thewrestler.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;
import java.util.stream.Collectors;

public class CreatureUtils {
  public static List<AbstractPower> getDebuffs(AbstractCreature creature) {
    return creature.powers.stream()
        .filter(pow -> pow.type == AbstractPower.PowerType.DEBUFF).collect(Collectors.toList());
  }
}