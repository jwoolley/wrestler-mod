package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.effects.utils.combat.CleanFinishEffect;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Redemption extends CustomCard {
  public static final String ID = "WrestlerMod:Redemption";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "redemption.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int BLOCK_AMOUNT = 16;
  private static final int BLOCK_AMOUNT_UPGRADE = 8;
  private static final int DAMAGE_PER_CARD = 5;
  private static final int DAMAGE_PER_CARD_UPGRADE = 1;
  private static final int COST = 2;

  public Redemption() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    this.baseDamage = this.damage = DAMAGE_PER_CARD;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    AbstractDungeon.actionManager.addToTop(
        new VFXAction(new CleanFinishEffect(Color.valueOf("FFFDCB"), "CHOIR_ANGELIC_1", Settings.ACTION_DUR_FAST)));
    AbstractDungeon.actionManager.addToBottom(new RedemptionAction(this.multiDamage, Settings.FAST_MODE));
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
      this.upgradeDamage(DAMAGE_PER_CARD_UPGRADE);
      initializeDescription();
    }
  }

  private static List<AbstractTooltipKeyword> EXTRA_KEYWORDS = Arrays.asList(
      CustomTooltipKeywords.getTooltipKeyword(CustomTooltipKeywords.PENALTY_CARD)
  );

  static class RedemptionAction extends AbstractGameAction {
    private static final float ACTION_DURATION = Settings.ACTION_DUR_MED;
    private static final float ACTION_DURATION_FASTER = Settings.ACTION_DUR_MED;
    private static final DamageInfo.DamageType DAMAGE_TYPE = DamageInfo.DamageType.NORMAL;
    private static final AttackEffect ATTACK_EFFECT = AbstractGameAction.AttackEffect.FIRE;
    private final int[] multiDamage;
    private final boolean exhaustAll;
    private final boolean isFast;
    private int numCardsExhausted;
    boolean tickedOnce = false;

    private RedemptionAction(int[] multiDamage, int amount, boolean exhaustAll, boolean isFast) {
      this.source = AbstractDungeon.player;
      this.actionType = ActionType.EXHAUST;
      this.multiDamage = multiDamage;
      this.amount = amount;
      this.isFast = isFast;
      this.duration = isFast ? ACTION_DURATION_FASTER : ACTION_DURATION;
      this.exhaustAll = exhaustAll;
    }

    RedemptionAction(int[] multiDamage, boolean isFast) {
      this(multiDamage, -1, true, isFast);
    }

    RedemptionAction(int[] multiDamage, int amount, boolean isFast) {
      this(multiDamage, amount, false, isFast);
    }

    @Override
    public void update() {
      if (this.duration <= ACTION_DURATION && !tickedOnce) {

        // final Predicate<AbstractCard> predicate = c -> c.hasTag(WrestlerCardTags.PENALTY);
        final Predicate<AbstractCard> predicate = c -> c.type == CardType.STATUS;

        final Map<CardGroup, List<AbstractCard>> cardsToExhaust = new HashMap<>();
        cardsToExhaust.put(AbstractDungeon.player.hand, AbstractDungeon.player.hand.group.stream()
            .filter(predicate::test).collect(Collectors.toList()));
        cardsToExhaust.put(AbstractDungeon.player.drawPile, AbstractDungeon.player.drawPile.group.stream()
            .filter(predicate::test).collect(Collectors.toList()));
        cardsToExhaust.put(AbstractDungeon.player.discardPile, AbstractDungeon.player.discardPile.group.stream()
            .filter(predicate::test).collect(Collectors.toList()));

        final List<Map.Entry<CardGroup, List<AbstractCard>>> entries = new ArrayList<>(cardsToExhaust.entrySet());

        if (this.exhaustAll || entries.stream().mapToLong(e -> e.getValue().size()).sum() <= this.amount) {
            numCardsExhausted = 0;
            entries.forEach(e -> {
              e.getValue().forEach(c ->  {
                e.getKey().moveToExhaustPile(c);
                numCardsExhausted++;
              });
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
          numCardsExhausted = this.amount;
        }

        if (numCardsExhausted == 0) {
          this.isDone = true;
        }

        this.tickedOnce = true;
      } else if (tickedOnce && this.duration <= 0.1f) {
        if (numCardsExhausted > 0) {
          for (int i = 0; i < numCardsExhausted; i++) {
            AbstractDungeon.actionManager.addToTop(
                new DamageAllEnemiesAction(this.source, this.multiDamage, DAMAGE_TYPE, ATTACK_EFFECT, this.isFast));
          }
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