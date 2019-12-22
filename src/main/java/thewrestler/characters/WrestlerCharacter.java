package thewrestler.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thewrestler.WrestlerMod;
import thewrestler.cards.attack.WrestlerStrike;
import thewrestler.cards.skill.WrestlerDefend;
import thewrestler.cards.skill.EyePoke;
import thewrestler.cards.attack.TakeToTheMat;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.relics.Headgear;

import java.util.ArrayList;

import static thewrestler.WrestlerMod.*;

//Wiki-page https://github.com/daviscook477/BaseMod/wiki/Custom-Characters
//and https://github.com/daviscook477/BaseMod/wiki/Migrating-to-5.0
//All text (starting description and loadout, anything labeled TEXT[]) can be found in WrestlerMod-character-Strings.json in the resources

public class WrestlerCharacter extends CustomPlayer {
    public static final Color CARD_RENDER_COLOR = new Color(0.9F, 0.5F, 0.0F, 1.0F);
    public static final Color SLASH_ATTACK_COLOR = Color.ORANGE.cpy();

    public static final Logger logger = LogManager.getLogger(WrestlerMod.class.getName());

    // =============== CHARACTER ENUMERATORS =================
    // These are enums for your Characters color (both general color and for the card library) as well as
    // an enum for the name of the player class - IRONCLAD, THE_SILENT, DEFECT, YOUR_CLASS ...
    // These are all necessary for creating a character. If you want to find out where and how exactly they are used
    // in the basegame (for fun and education) Ctrl+click on the PlayerClass, CardColor and/or LibraryType below and go down the
    // Ctrl+click rabbit hole

    public static class Enums {
//        @SpireEnum
//        public static AbstractPlayer.PlayerClass THE_WRESTLER;
//        @SpireEnum(name = "THE_WRESTLER_ORANGE") // These two HAVE to have the same absolutely identical name.
//        public static AbstractCard.CardColor THE_WRESTLER_ORANGE;
        @SpireEnum(name = "THE_WRESTLER_ORANGE") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType THE_WRESTLER_GRAY;
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeID("TheWrestler"); // needs to be the key from CharacterStrings.json
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    private static String getCharacterOrbImagePath(String filename) {
        return getImageResourcePath("char/wrestler/orb/" + filename);
    }
    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
        getCharacterOrbImagePath("layer1.png"),
        getCharacterOrbImagePath("layer2.png"),
        getCharacterOrbImagePath("layer3.png"),
        getCharacterOrbImagePath("layer4.png"),
        getCharacterOrbImagePath("layer5.png"),
        getCharacterOrbImagePath("layer6.png"),
        getCharacterOrbImagePath("layer1d.png"),
        getCharacterOrbImagePath("layer2d.png"),
        getCharacterOrbImagePath("layer3d.png"),
        getCharacterOrbImagePath("layer4d.png"),
        getCharacterOrbImagePath("layer5d.png")
    };

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================
    private static final String ANIMATION_PATH = "character/wrestler.scml";

    public WrestlerCharacter(String name, PlayerClass setClass) {
//        super(name, setClass, orbTextures, getEnergyOrbFilePath(),null, (String) null);

        super(name, setClass, orbTextures,
            getCharacterOrbImagePath("vfx.png"), null,
                new SpriterAnimation(getAnimationResourcePath(ANIMATION_PATH)));

        // ENABLE IF USING CHARACTER ANIMATION
        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in WrestlerMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                THE_WRESTLER_SHOULDER_2, // campfire pose
                THE_WRESTLER_SHOULDER_1, // another campfire pose
                THE_WRESTLER_CORPSE, // dead corpse
                getLoadout(), 20.0F, -8.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager
                        // ENABLE IF USING CHARACTER ANIMATION

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================
        
        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

    }

    // =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");

        retVal.add(WrestlerStrike.ID);
        retVal.add(WrestlerStrike.ID);
        retVal.add(WrestlerStrike.ID);
        retVal.add(WrestlerStrike.ID);

        retVal.add(WrestlerDefend.ID);
        retVal.add(WrestlerDefend.ID);
        retVal.add(WrestlerDefend.ID);
        retVal.add(WrestlerDefend.ID);

        retVal.add(EyePoke.ID);
        retVal.add(TakeToTheMat.ID);

        return retVal;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Headgear.ID);
        UnlockTracker.markRelicAsSeen(Headgear.ID);
        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 0;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.THE_WRESTLER_ORANGE;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return CARD_RENDER_COLOR.cpy();
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new WrestlerStrike();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new WrestlerCharacter(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return CARD_RENDER_COLOR;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return SLASH_ATTACK_COLOR;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }



    @Override
    public Texture getEnergyImage() {
        final Texture energyImage = super.getEnergyImage();
        System.out.println("Energy image: " + energyImage.toString());
        return energyImage;
    }

    // Should return class name as it appears in run history screen.

}
