package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.util.BasicUtils;
import thewrestler.util.info.CombatInfo;

import java.util.*;
import java.util.stream.Collectors;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Redemption extends CustomCard implements AbstractPenaltyCardListener, StartOfCombatListener {
  public static final String ID = "WrestlerMod:Redemption";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "redemption.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int BLOCK_AMOUNT = 30;
  private static final int BLOCK_AMOUNT_UPGRADE = 10;
  private static final int CARDS_TO_EXAHUST = 3;
  private static final int COST = 3;

  public Redemption() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    this.baseMagicNumber = this.magicNumber = CARDS_TO_EXAHUST;
    calculateCost();
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    AbstractDungeon.actionManager.addToBottom(new ExhaustPenaltyCardsAction(Settings.FAST_MODE));
  }

  private void calculateCost() {
    if (BasicUtils.isPlayerInCombat()) {
      modifyCostForCombat(-CombatInfo.getNumPenaltyCardsGainedThisCombat());
      this.update();
    }
  }

  @Override
  public void onGainedWarningCard() { }

  @Override
  public void onGainedPenaltyCard() {
    modifyCostForCombat(-1);
  }

  @Override
  public void atStartOfCombat() {
    calculateCost();
  }

  @Override
  public AbstractCard makeCopy() {
    return new Redemption();
  }

  public static String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_AMOUNT_UPGRADE);
      initializeDescription();
    }
  }

  private static List<AbstractTooltipKeyword> EXTRA_KEYWORDS = Arrays.asList(
      CustomTooltipKeywords.getTooltipKeyword(CustomTooltipKeywords.PENALTY_CARD)
  );

  static class ExhaustPenaltyCardsAction extends AbstractGameAction {
    private static final float ACTION_DURATION = Settings.ACTION_DUR_FASTER;
    private final boolean exhaustAll;
    private final boolean isFast;

    private ExhaustPenaltyCardsAction(int amount, boolean exhaustAll, boolean isFast) {
      this.source = AbstractDungeon.player;
      this.actionType = ActionType.EXHAUST;
      this.amount = amount;
      this.exhaustAll = exhaustAll;
      this.isFast = isFast;
      this.duration = ACTION_DURATION;
    }

    ExhaustPenaltyCardsAction(boolean isFast) {
      this(-1, true, isFast);
    }

    ExhaustPenaltyCardsAction(int amount, boolean isFast) {
      this(amount, false, isFast);
    }

    @Override
    public void update() {
      if (this.duration <= ACTION_DURATION) {
        final Map<CardGroup, List<AbstractCard>> cardsToExhaust = new HashMap<>();
        cardsToExhaust.put(AbstractDungeon.player.hand, AbstractDungeon.player.hand.group.stream()
            .filter(c -> c.hasTag(WrestlerCardTags.PENALTY)).collect(Collectors.toList()));
        cardsToExhaust.put(AbstractDungeon.player.drawPile, AbstractDungeon.player.drawPile.group.stream()
            .filter(c -> c.hasTag(WrestlerCardTags.PENALTY)).collect(Collectors.toList()));
        cardsToExhaust.put(AbstractDungeon.player.discardPile, AbstractDungeon.player.discardPile.group.stream()
            .filter(c -> c.hasTag(WrestlerCardTags.PENALTY)).collect(Collectors.toList()));

        final List<Map.Entry<CardGroup, List<AbstractCard>>> entries = new ArrayList<>(cardsToExhaust.entrySet());

        if (this.exhaustAll || entries.stream().mapToLong(e -> e.getValue().size()).sum() <= this.amount) {
            entries.forEach(e -> {
              e.getValue().forEach(c -> e.getKey().moveToExhaustPile(c));
            });
        } else {
          final List<Map.Entry<CardGroup, AbstractCard>> flattenedEntries = new ArrayList<>();
          entries.forEach(e -> {
             e.getValue().forEach(c -> {
               flattenedEntries.add(new AbstractMap.SimpleEntry<>(e.getKey(), c));
             });
          });
          Collections.shuffle(flattenedEntries);
          flattenedEntries.stream().limit(this.amount).forEach(e -> e.getKey().moveToExhaustPile(e.getValue()));
        }

        this.isDone = true;
      }
      this.tickDuration();
    }
  }

  @Override
  public List<TooltipInfo> getCustomTooltips() {
    return TooltipKeywords.getTooltipInfos(EXTRA_KEYWORDS);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}