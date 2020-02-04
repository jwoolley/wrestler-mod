package thewrestler.cards.curse;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import thewrestler.actions.cards.skill.RopewalkAction;
import thewrestler.cards.skill.Ropewalk;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.CreatureUtils;
import thewrestler.util.info.CombatInfo;

import java.util.List;
import java.util.stream.Collectors;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Predictable extends CustomCard {
  public static final String ID = "WrestlerMod:Predictable";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "predictable.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.CURSE;
  private static final CardRarity RARITY = CardRarity.CURSE;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int ARTIFACT_AMOUNT = 1;
  private static final int COST = -2;

  public Predictable() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, CardColor.CURSE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = ARTIFACT_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (this.dontTriggerOnUseCard) {
      if (CreatureUtils.getLivingMonsters().size() > 0) {
        this.superFlash(Color.SCARLET.cpy());

        final AbstractCreature monster = AbstractDungeon.getRandomMonster();
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(monster, monster, new ArtifactPower(monster, magicNumber), magicNumber));
      }
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Predictable();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.rawDescription = getDescription();
      initializeDescription();
    }
  }


  public void triggerOnEndOfTurnForPlayingCard() {
    this.dontTriggerOnUseCard = true;
    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
  }

  public static String getDescription() {
    return DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}