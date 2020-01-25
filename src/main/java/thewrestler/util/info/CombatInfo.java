package thewrestler.util.info;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CombatInfo {
  public static final CardsPlayedCounts RESET_CARDS_PLAYED_COUNTS = new CardsPlayedCounts();
  public static final CardsPlayedCounts UNINITIALIZED_CARDS_PLAYED_COUNTS = new CardsPlayedCounts(-1, -1, -1, -1);

  public static class CardsPlayedCounts {
    public CardsPlayedCounts() {
      this(0, 0, 0, 0);
    }

    private CardsPlayedCounts(int attacks, int skills, int powers, int debuffs) {
      this.attacks = attacks;
      this.skills = skills;
      this.powers = powers;
      this.debuffs = debuffs;
    }
    public final int attacks;
    public final int skills;
    public final int powers;
    public final int debuffs;
  }

  private static int debuffsAppliedThisTurn = 0;

  public static void incrementDebuffsAppliedCount() {
    debuffsAppliedThisTurn++;
  }

  public static void atStartOfTurn() {
    debuffsAppliedThisTurn = 0;
  }

  public static void atStartOfCombat() {
    debuffsAppliedThisTurn = 0;
  }

  public static void atEndOfCombat() {
    debuffsAppliedThisTurn = 0;
  }

  public static int getNumAttacksPlayed() {
    return (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
        .filter(c -> c.type == AbstractCard.CardType.ATTACK).count();
  }

  public static int getNumSkillsPlayed() {
    return (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
        .filter(c -> c.type == AbstractCard.CardType.SKILL).count();
  }

  public static int getNumPowersPlayed() {
    return (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
        .filter(c -> c.type == AbstractCard.CardType.POWER).count();
  }

  public static int getDebuffsAppliedThisTurn() {
    return debuffsAppliedThisTurn;
  }

  public static CardsPlayedCounts getCardsPlayedCounts() {
    return new CardsPlayedCounts(getNumAttacksPlayed(), getNumSkillsPlayed(), getNumPowersPlayed(),
        getDebuffsAppliedThisTurn());
  }
}
