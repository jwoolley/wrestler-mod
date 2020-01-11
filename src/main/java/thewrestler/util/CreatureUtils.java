package thewrestler.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;
import java.util.stream.Collectors;

public class CreatureUtils {
  public static List<AbstractPower> getDebuffs(AbstractCreature creature) {
    return creature.powers.stream()
        .filter(pow -> pow.type == AbstractPower.PowerType.DEBUFF).collect(Collectors.toList());
  }

  public static List<AbstractMonster> getLivingMonsters() {
    return AbstractDungeon.getCurrRoom().monsters.monsters.stream()
        .filter(mo -> !mo.isDying && !mo.isDeadOrEscaped()).collect(Collectors.toList());
  }
}