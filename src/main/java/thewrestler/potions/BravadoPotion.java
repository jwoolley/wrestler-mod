package thewrestler.potions;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.purple.SignatureMove;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.KeywordStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thewrestler.WrestlerMod;
import thewrestler.cards.attack.CobraClutch;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.signaturemoves.moveinfos.AbstractSignatureMoveInfo;
import thewrestler.util.BasicUtils;

import java.util.regex.Pattern;

public class BravadoPotion extends AbstractPotion {
  public static final String POTION_ID = WrestlerMod.makeID("BravadoPotion");
  private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
  private static final String SIGNATURE_MOVE_KEYWORD_ID = WrestlerMod.makeID("SignatureMove");
  private static final String SIGNATURE_MOVE_CARD_KEYWORD_ID = WrestlerMod.makeID("SignatureMoveCardIndicator");
  private static final String SIGNATURE_MOVE_CARD_PUNCTUATION_ID = WrestlerMod.makeID("SentenceEndPunctuation");

  public static final String NAME = potionStrings.NAME;
  public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

  public static final Color LIQUID_COLOR = Color.MAROON.cpy();
  public static final Color HYBRID_COLOR = Color.BLUE.cpy();
  public static final Color SPOTS_COLOR = Color.WHITE.cpy();

  private static final PotionRarity RARITY = PotionRarity.UNCOMMON;
  private static final PotionSize SIZE = PotionSize.BOTTLE;
  private static final PotionColor COLOR_SHAPE = PotionColor.ELIXIR;

  public BravadoPotion() {
    super(NAME, POTION_ID, RARITY, SIZE, COLOR_SHAPE);

    potency = getPotency();

    isThrown = false;
    this.targetRequired = false;

    // potion tip
    tips.add(new PowerTip(name, DESCRIPTIONS[0]));

    // keyword tip
    final String BRAVADO_POTION_KEYWORD_KEY = (WrestlerMod.getModKeywordPrefix() + "Trademark_Move").toLowerCase();
    if (GameDictionary.keywords.containsKey(BRAVADO_POTION_KEYWORD_KEY)) {
      PotionStrings bravadoPotionKeywordStrings =
          CardCrawlGame.languagePack.getPotionString("WrestlerMod:BravadoPotionTrademarkMove");

      final String cardname =
          WrestlerCharacter.hasSignatureMoveInfo()
              && WrestlerCharacter.getSignatureMoveInfo().getSignatureMoveCard() != null
              ? "#y" + WrestlerCharacter.getSignatureMoveInfo().getSignatureMoveCard().name.replaceAll("\\s", " #y")
              : "";

      final Keyword moveKeywordText = WrestlerMod.getKeyword(SIGNATURE_MOVE_KEYWORD_ID);
      final Keyword cardNameKeywordText = WrestlerMod.getKeyword(SIGNATURE_MOVE_CARD_KEYWORD_ID);
      final Keyword lineEndPunctuation = WrestlerMod.getKeyword(SIGNATURE_MOVE_CARD_PUNCTUATION_ID);
      final String tooltipKeywordText = moveKeywordText.DESCRIPTION
            + (cardname.length() > 0 ? cardNameKeywordText.DESCRIPTION + cardname + lineEndPunctuation.DESCRIPTION : "");

      tips.add(new PowerTip(bravadoPotionKeywordStrings.DESCRIPTIONS[0], tooltipKeywordText));
    }
  }

  @Override
  public void use(AbstractCreature target) {
   if (BasicUtils.isPlayingAsWrestler() && WrestlerCharacter.hasSignatureMoveInfo()
       && WrestlerCharacter.getSignatureMoveInfo().canStillTriggerCardGain()) {
     for (int i = 0; i < this.potency; i++) {
       WrestlerCharacter.getSignatureMoveInfo().manuallyTriggerCardGain();
     }
   }
  }

  @Override
  public boolean canUse() {
    return BasicUtils.isPlayingAsWrestler() && WrestlerCharacter.hasSignatureMoveInfo()
        && WrestlerCharacter.getSignatureMoveInfo().canStillTriggerCardGain();
  }

  @Override
  public AbstractPotion makeCopy() {
    return new CobraPotion();
  }

  @Override
  public int getPotency(final int potency) {
    return 1;
  }

  public void upgradePotion() {
    potency += 1;
    tips.clear();
    tips.add(new PowerTip(name, description));
  }
}
