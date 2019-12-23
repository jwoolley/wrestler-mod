package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.WrestlerMod;
import thewrestler.actions.GainGoldAction;
import thewrestler.cards.power.WrestlerShackled;
import thewrestler.effects.utils.combat.CleanFinishEffect;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.CardUtil;

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
  private static final int UPGRADED_COST = 2;


  public CleanFinish() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    List<AbstractCard> cards = p.hand.group.stream()
        .filter(c -> c.type != CardType.STATUS && c.type != CardType.CURSE).collect(Collectors.toList());

    WrestlerMod.logger.info("CleanFinish::use updating " + cards.size() + " cards");

    AbstractDungeon.actionManager.addToBottom(new VFXAction(new CleanFinishEffect()));

    cards.forEach(c -> {
      if (c.cost > 0) {
        c.freeToPlayOnce = true;
      }
      if (!c.exhaust && !c.exhaustOnUseOnce) {
        c.exhaustOnUseOnce = true;
        c.rawDescription += EXTENDED_DESCRIPTION[0];
      }
      c.initializeDescription();
      // AbstractDungeon.player.hand.refreshHandLayout();
    });
  }

  @Override
  public AbstractCard makeCopy() {
    return new CleanFinish();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBaseCost(UPGRADED_COST);
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