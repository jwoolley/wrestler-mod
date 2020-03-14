package thewrestler.util.info;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CombatInfo {
  public static final CardsPlayedCounts RESET_CARDS_PLAYED_COUNTS = new CardsPlayedCounts();
  public static final CardsPlayedCounts UNINITIALIZED_CARDS_PLAYED_COUNTS = new CardsPlayedCounts(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1);

  public static class CardsPlayedCounts {
    public CardsPlayedCounts() {
      this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    private CardsPlayedCounts(int attacks, int skills, int powers, int statuses, int curses, int debuffs, int dirtyCards, int nonAttacks, int dirtyCardThisCombat,
                              int penaltyCardsGainedThisCombat) {
      this.attacks = attacks;
      this.skills = skills;
      this.powers = powers;
      this.statuses = statuses;
      this.curses = curses;
      this.debuffs = debuffs;
      this.dirtyCards = dirtyCards;
      this.nonAttacks = nonAttacks;
      this.dirtyCardsThisCombat = dirtyCardThisCombat;
      this.penaltyCardsGainedThisCombat = penaltyCardsGainedThisCombat;
    }
    public final int attacks;
    public final int skills;
    public final int powers;
    public final int statuses;
    public final int curses;
    public final int debuffs;
    public final int dirtyCards;
    public final int nonAttacks;
    public int dirtyCardsThisCombat;
    public int penaltyCardsGainedThisCombat;
  }

  private static int debuffsAppliedThisTurn = 0;
  private static int dirtyCardsPlayedThisTurn = 0;

  private static int dirtyCardsPlayedThisCombat = 0;

  private static int penaltyCardsGainedThisCombat = 0;

  public static void incrementDebuffsAppliedCount() {
    debuffsAppliedThisTurn++;
  }

  public static void incrementDirtyCardsPlayedCount() {
    dirtyCardsPlayedThisTurn++;
    dirtyCardsPlayedThisCombat++;
  }

  public static void incrementPenaltyCardsGainedThisCombatCount() {
    penaltyCardsGainedThisCombat++;
  }

  public static void atStartOfTurn() {
    debuffsAppliedThisTurn = 0;
    dirtyCardsPlayedThisTurn = 0;
  }

  public static void atStartOfCombat() {
    debuffsAppliedThisTurn = 0;
    dirtyCardsPlayedThisTurn = 0;
    dirtyCardsPlayedThisCombat = 0;
  }

  public static void atEndOfCombat() {
    debuffsAppliedThisTurn = 0;
    dirtyCardsPlayedThisTurn = 0;
    dirtyCardsPlayedThisCombat = 0;
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

  public static int getNumStatusesPlayed() {
    return (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
        .filter(c -> c.type == AbstractCard.CardType.STATUS).count();
  }

  public static int getNumCursesPlayed() {
    return (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
        .filter(c -> c.type == AbstractCard.CardType.CURSE).count();
  }

  public static int getNumNonAttacksPlayed() {
    return (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.size()
        - getNumAttacksPlayed();
  }

  public static int getNumDebuffsAppliedThisTurn() {
    return debuffsAppliedThisTurn;
  }

  public static int getNumDirtyCardsPlayed() {
    return dirtyCardsPlayedThisTurn;
  }

  public static int getNumDirtyCardsPlayedThisCombat() {
    return dirtyCardsPlayedThisCombat;
  }

  public static int getNumPenaltyCardsGainedThisCombat() {
    return penaltyCardsGainedThisCombat;
  }


  public static CardsPlayedCounts getCardsPlayedCounts() {
    return new CardsPlayedCounts(
        getNumAttacksPlayed(), getNumSkillsPlayed(), getNumPowersPlayed(),
        getNumStatusesPlayed(), getNumCursesPlayed(),
        getNumDebuffsAppliedThisTurn(), getNumDirtyCardsPlayed(),
        getNumNonAttacksPlayed(),
        getNumDirtyCardsPlayedThisCombat(), getNumPenaltyCardsGainedThisCombat());
  }
}
