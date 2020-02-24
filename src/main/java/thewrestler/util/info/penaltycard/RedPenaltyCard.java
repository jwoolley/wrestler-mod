package thewrestler.util.info.penaltycard;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

public class RedPenaltyCard extends AbstractPenaltyCard {
  private static final UIStrings uiStrings;

  public static final String ID = "WrestlerMod:RedPenaltyCard";
  public static final String IMG_FILENAME = "red.png";
  public static final String NAME;
  public static final String DESCRIPTION;

  public RedPenaltyCard() {
    super(ID, NAME, DESCRIPTION, IMG_FILENAME);
  }

  @Override
  public Texture getTexture() {
    return null;
  }

  @Override
  public void onGained() {

  }

  @Override
  public void onRemoved() {

  }

  @Override
  public void atStartOfTurn() {

  }

  @Override
  public void atEndOfTurn() {

  }

  @Override
  public void onCardPlayed(AbstractCard card) {

  }

  @Override
  public void onCardExhausted(AbstractCard card) {

  }

  static {
    uiStrings =  CardCrawlGame.languagePack.getUIString(ID);
    NAME = uiStrings.TEXT[0];
    DESCRIPTION = uiStrings.TEXT[1];
  }
}
