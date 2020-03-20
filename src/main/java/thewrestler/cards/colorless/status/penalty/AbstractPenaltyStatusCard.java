package thewrestler.cards.colorless.status.penalty;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.powers.enqueuedpenaltycard.EnqueuedPenaltyCardPower;

import static thewrestler.WrestlerMod.getCardResourcePath;

public abstract class AbstractPenaltyStatusCard extends CustomCard {
  private static final CardType TYPE = CardType.STATUS;
  private static final CardRarity RARITY = CardRarity.SPECIAL;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final String IMG_PATH_PREFIX = "penaltycards/";

  public AbstractPenaltyStatusCard(String id, String name, String imgPath, String description) {
    super(id, name, imgPath,1, description, TYPE, CardColor.COLORLESS, RARITY, TARGET);
    this.tags.add(WrestlerCardTags.PENALTY);
  }

  protected abstract EnqueuedPenaltyCardPower getEneueuedCardPower(int amount);

  public abstract void triggerOnCardGained();

  public void applyEnqueuedCardPower(int amount) {
    EnqueuedPenaltyCardPower power = getEneueuedCardPower(amount);
    AbstractDungeon.actionManager.addToBottom(new EnqueuedPenaltyCardPower.GainEnqueuedPenaltyCardPowerAction(power));
  }

  protected class EnqueueCardPower extends EnqueuedPenaltyCardPower {
    public EnqueueCardPower(int amount, String baseID, String cardName, String imgFilePath) {
      super(AbstractDungeon.player, amount, baseID, cardName, imgFilePath);
    }

    @Override
    protected AbstractPenaltyStatusCard getCard() {
      return (AbstractPenaltyStatusCard) AbstractPenaltyStatusCard.this.makeCopy();
    }

    @Override
    public AbstractPower makeCopy() {
      return new EnqueueCardPower(this.amount, this.baseID, this.cardName, this.imgFilename);
    }
  }

  static String getPenaltyCardImgPath(String imgFilename) {
    return getCardResourcePath(IMG_PATH_PREFIX + imgFilename);
  }
}