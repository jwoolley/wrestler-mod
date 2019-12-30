package thewrestler.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thewrestler.WrestlerMod;
import thewrestler.cards.attack.CobraClutch;

public class CobraPotion extends AbstractPotion {
    public static final String POTION_ID = WrestlerMod.makeID("CobraPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public static final Color LIQUID_COLOR = Color.OLIVE.cpy();
    public static final Color HYBRID_COLOR = Color.OLIVE.cpy();
    public static final Color SPOTS_COLOR = Color.CLEAR.cpy();

    private static final PotionRarity RARITY = PotionRarity.UNCOMMON;
    private static final PotionSize SIZE = PotionSize.SNECKO;
    private static final PotionColor COLOR_SHAPE = PotionColor.SNECKO;

    public CobraPotion() {
      super(NAME, POTION_ID, RARITY, SIZE, COLOR_SHAPE);

      potency = getPotency();

      description = DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1];

      isThrown = false;
      this.targetRequired = false;

      // potion tip
      tips.add(new PowerTip(name, description));

      // keyword tip
      final String COBRA_CLUTCH_KEYWORD_KEY = (WrestlerMod.getModKeywordPrefix() + "Cobra_Clutch").toLowerCase();
      if (GameDictionary.keywords.containsKey(COBRA_CLUTCH_KEYWORD_KEY)) {
        PotionStrings cobraClutchNameStrings =
            CardCrawlGame.languagePack.getPotionString("WrestlerMod:CobraPotionCobraClutchName");
        tips.add(new PowerTip(cobraClutchNameStrings.DESCRIPTIONS[0],
            GameDictionary.keywords.get(COBRA_CLUTCH_KEYWORD_KEY)));
      }
    }

    @Override
    public void use(AbstractCreature target) {
      if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
        for (int i = 0; i < this.potency; i++) {
          AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new CobraClutch(), 1));
        }
      }
    }

    @Override
    public AbstractPotion makeCopy() {
      return new CobraPotion();
    }

    @Override
    public int getPotency(final int potency) {
      return 3;
    }

    public void upgradePotion() {
      potency += 1;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
  }
