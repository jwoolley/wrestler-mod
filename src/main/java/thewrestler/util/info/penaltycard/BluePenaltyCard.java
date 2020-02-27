package thewrestler.util.info.penaltycard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.GainBlockRandomMonsterAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thewrestler.WrestlerMod;
import thewrestler.glyphs.custom.CustomGlyph;
import thewrestler.powers.WrestlerShackled;

import java.util.Arrays;

public class BluePenaltyCard extends AbstractPenaltyCard {
  
  private static final UIStrings uiStrings;

  public static final String ID = "WrestlerMod:BluePenaltyCard";
  public static final String IMG_FILENAME = "blue.png";
  public static final String GLYPH_IMG_FILENAME = "blue.png";
  public static CustomGlyph.CustomGlyphEnum GLYPH_ENUM = CustomGlyph.CustomGlyphEnum.PENALTY_CARD_BLUE;
  public static final String NAME;
  public static final String[] TEXT;

  private static final int BLOCK_GAIN = 3;

  public BluePenaltyCard() {
    super(ID, NAME, getDescription(), IMG_FILENAME);
  }

  @Override
  public AbstractPenaltyCard makeCopy() {
    return new BluePenaltyCard();
  }

  public static CustomGlyph.CustomGlyphEnum getGlyphEnum() {
    return GLYPH_ENUM;
  }

  @Override
  public CustomGlyph getTooltipGlyph() {
    return CustomGlyph.getGlyph(getGlyphEnum());
  }

  @Override
  public void onGained() {

  }

  @Override
  public void onRemoved() {

  }

  @Override
  public void atStartOfTurn() {
    this.flash();
    playTriggerSfx();
    WrestlerMod.logger.info("Triggered penalty card [" + getDebugDescription() + "]");
    AbstractDungeon.actionManager.addToBottom(new GainBlockRandomMonsterAction(AbstractDungeon.player, BLOCK_GAIN));
  }

  @Override
  public void atEndOfTurn() {


  }

  @Override
  public void onCardUsed(AbstractCard card) {

  }

  @Override
  public void onCardExhausted(AbstractCard card) {

  }

  private static String getDescription() {
    return TEXT[0] + BLOCK_GAIN + TEXT[1];
  }

  static {
    uiStrings =  CardCrawlGame.languagePack.getUIString(ID);
    NAME = uiStrings.TEXT[0];
    TEXT = Arrays.copyOfRange(uiStrings.TEXT, 1, uiStrings.TEXT.length);
  }
}
