package thewrestler.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
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

  public static int getAllMonstersCenterOfMass() {
    if (!BasicUtils.isPlayerInCombat()) {
      return Settings.WIDTH / 2;
    }

    int totalXCenter = 0;

    final List<AbstractMonster> monsters = getLivingMonsters();

    if (monsters.size() > 0) {
      totalXCenter = monsters.stream().mapToInt(m -> (int) m.hb.cX).sum();
    }

    return totalXCenter / monsters.size();
  }
}