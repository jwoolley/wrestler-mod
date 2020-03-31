package thewrestler.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thewrestler.WrestlerMod;
import thewrestler.actions.power.ApplyGrappledAction;

public class GrapplePotion extends AbstractPotion {
    public static final String POTION_ID = WrestlerMod.makeID("GrapplePotion");

    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public static final Color LIQUID_COLOR = Color.PURPLE.cpy();
    public static final Color HYBRID_COLOR = Color.PURPLE.cpy();
    public static final Color SPOTS_COLOR = Color.GOLDENROD.cpy();

    private static final PotionRarity RARITY = PotionRarity.COMMON;
    private static final PotionSize SIZE = PotionSize.H;
    private static final PotionColor COLOR_SHAPE = PotionColor.EXPLOSIVE;

    public GrapplePotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main WrestlerMod.java
        super(NAME, POTION_ID, RARITY, SIZE, COLOR_SHAPE);

        // Potency is the damage/magic number equivalent of potions.
        this.potency = getPotency();

        this.description = DESCRIPTIONS[0];

        // Do you throw this potion at an enemy or do you just consume it?
        this.isThrown = true;
        this.targetRequired = true;

        // potion tip
        tips.add(new PowerTip(name, description));

        // keyword tip
        final String GRAPPLED_KEYWORD_KEY = (WrestlerMod.getModKeywordPrefix() + "Grapple").toLowerCase();
        if (GameDictionary.keywords.containsKey(GRAPPLED_KEYWORD_KEY)) {
            PotionStrings grappleNameStrings =
                CardCrawlGame.languagePack.getPotionString("WrestlerMod:GrapplePotionGrappledName");
            tips.add(new PowerTip(grappleNameStrings.DESCRIPTIONS[0],
                GameDictionary.keywords.get(GRAPPLED_KEYWORD_KEY)));
        }
    }

    @Override
    public void use(AbstractCreature target) {
        // If you are in combat, gain strength and the "lose strength at the end of your turn" power, equal to the potency of this potion.
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new ApplyGrappledAction(target, AbstractDungeon.player));

        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new GrapplePotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 0;
    }
}
