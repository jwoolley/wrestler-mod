package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thewrestler.WrestlerMod;

public class BearHugPower extends AbstractWrestlerPower implements CloneablePowerInterface {
    public static final String POWER_ID = WrestlerMod.makeID("BearHugPower");
    public static final String IMG = "bearhug.png";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final PowerType POWER_TYPE = PowerType.DEBUFF;

    public BearHugPower(AbstractCreature owner, int amount) {
      super(POWER_ID, NAME, IMG, owner, AbstractDungeon.player, amount, POWER_TYPE);
    }

    @Override
    public void updateDescription() {
      this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
      if (card.type != AbstractCard.CardType.ATTACK) {
        this.flash();
        this.applyTrigger();
      }
    }

    @Override
    public void atStartOfTurn() {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.source, POWER_ID));
    }

    private void applyTrigger() {
      flash();
      AbstractDungeon.actionManager.addToTop(new SFXAction("GRUNT_SHORT_1"));

      AbstractDungeon.actionManager.addToBottom(
          new GainBlockAction(this.source, this.source, this.amount));

      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(this.owner, this.source,
              new ConstrictedPower(this.owner, this.source, this.amount)));
    }

    @Override
    public AbstractPower makeCopy() {
      return new BearHugPower(owner, amount);
    }
  }