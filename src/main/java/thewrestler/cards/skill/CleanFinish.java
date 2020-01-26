package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.WrestlerMod;
import thewrestler.effects.utils.combat.CleanFinishEffect;
import thewrestler.enums.AbstractCardEnum;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class CleanFinish extends CustomCard {
  public static final String ID = "WrestlerMod:CleanFinish";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cleanfinish.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 3;

  private static final int NUM_DISCOUNTED_CARDS = 3;
  private static final int NUM_DISCOUNTED_CARDS_UPGRADE = 1;


  public CleanFinish() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = NUM_DISCOUNTED_CARDS;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    List<AbstractCard> cards = p.hand.group.stream()
        .filter(c -> c.type != CardType.CURSE)
        .filter(c -> c.cost > 0)
        .filter(c -> c.uuid != this.uuid)
        .collect(Collectors.toList());

    WrestlerMod.logger.info("CleanFinish:use filtered cards:" + cards.stream()
        .map(c -> c.name).collect(Collectors.joining(", ")));

    AbstractDungeon.actionManager.addToBottom(new VFXAction(new CleanFinishEffect()));

    Collections.shuffle(cards);

    if (cards.size() > this.magicNumber) {
      cards = cards.subList(0, this.magicNumber);
    }

    WrestlerMod.logger.info("CleanFinish:use suffled and truncated cards:" + cards.stream()
        .map(c -> c.name).collect(Collectors.joining(", ")));

    if (!cards.isEmpty()) {
      cards.stream().forEach(c -> {
        if (c.cost > 0) {
          c.freeToPlayOnce = true;
        }
        if (!c.exhaust && !c.exhaustOnUseOnce) {
          c.exhaustOnUseOnce = true;
          c.rawDescription += EXTENDED_DESCRIPTION[0];
        }
        c.initializeDescription();
      });
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new CleanFinish();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_DISCOUNTED_CARDS_UPGRADE);
      initializeDescription();
    }
  }

  public static String getDescription() {
    return DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}