package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Pendulum extends CustomCard {
  public static final String ID = "WrestlerMod:Pendulum";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "pendulum.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = -1;

  public Pendulum() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(true), TYPE,
      AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
      this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new PendulumAction(p, this.energyOnUse, this.freeToPlayOnce));
  }

  private static class PendulumAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_XFAST;
    private final AbstractPlayer player;
    private final boolean freeToPlayOnce;
    private final int energyOnUse;

    PendulumAction(AbstractPlayer player, int energyOnUse, boolean freeToPlayOnce) {
      this.duration = DURATION;
      this.actionType = ActionType.DAMAGE;
      this.source = this.player = player;
      this.energyOnUse = energyOnUse;
      this.freeToPlayOnce = freeToPlayOnce;
    }

    @Override
    public void update() {
      if (this.duration <= DURATION) {
        AbstractDungeon.actionManager.addToBottom(
            new DiscardAction(this.player, this.player, this.player.hand.size(), true));

        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
          effect = this.energyOnUse;
        }
        if (this.player.hasRelic("Chemical X")) {
          effect += 2;
          this.player.getRelic("Chemical X").flash();
        }

        if (effect > 0) {
          AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(effect));
          AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.player, effect));
          AbstractDungeon.actionManager.addToBottom(
              new ApplyPowerAction(this.player, this.player, new NoDrawPower(this.player)));
          if (!this.freeToPlayOnce) {
            this.player.energy.use(EnergyPanel.totalCount);
          }
        }
        this.isDone = true;
      }
      this.tickDuration();
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Pendulum();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.exhaust = false;
      this.rawDescription = getDescription(false);
      initializeDescription();
    }
  }
  public static String getDescription(boolean hasExhaust) {
    return DESCRIPTION + (hasExhaust ? EXTENDED_DESCRIPTION[0] : "");
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}