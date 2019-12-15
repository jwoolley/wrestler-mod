package thewrestler;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thewrestler.cards.*;
import thewrestler.cards.attack.TakeToTheMat;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.enums.WrestlerCharEnum;
import thewrestler.events.IdentityCrisisEvent;
import thewrestler.potions.WrestlerPotion;
import thewrestler.relics.Headgear;
import thewrestler.util.IDCheckDontTouchPls;
import thewrestler.util.TextureLoader;
import thewrestler.variables.DefaultCustomVariable;
import thewrestler.variables.DefaultSecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "WrestlerCharacter" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 3 places.
// I comment those places below, under the place where you set your ID.

//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "WrestlerCharacter:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "WrestlerCharacter". You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class WrestlerMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(WrestlerMod.class.getName());
    public static final String MOD_ID = "WrestlerMod";
    public static final String RESOURCE_FOLDER_NAME = "WrestlerMod";
    private static final String IN_GAME_CHARACTER_NAME = "The Wrestler";
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theWrestlerDefaultSettings = new Properties();
    public static final String ENABLE_WRESTLER_SETTINGS = "enableWrestler";
    public static boolean enableWrestler = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "The Wrestler";
    private static final String AUTHOR = "Author";
    private static final String DESCRIPTION = "A base for Slay the Spire to my own mod from, featuring The Wrestler.";
    
    // =============== INPUT TEXTURE LOCATION =================



    // Colors (RGB)
    // Character Color
    public static final Color WRESTLER_GRAY = CardHelper.getColor(70.0f, 70.0f, 64.0f);
    
    // Potion Colors in RGB
    public static final Color WRESTLER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color WRESTLER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color WRESTLER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown
    
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!


    public static final String getAnimationResourcePath(String resourcePath) {
        return RESOURCE_FOLDER_NAME + "/animations/" + resourcePath;
    }

    public static final String getImageResourcePath(String resourcePath) {
        return RESOURCE_FOLDER_NAME + "/images/" + resourcePath;
    }

    public static final String getAudioResourcePath(String resourcePath) {
        return RESOURCE_FOLDER_NAME + "/sounds/" + resourcePath;
    }

    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_WRESTLER_GRAY = getImageResourcePath("512/attack_wrestler.png");
    private static final String SKILL_WRESTLER_GRAY = getImageResourcePath("512/skill_wrestler.png");
    private static final String POWER_WRESTLER_GRAY = getImageResourcePath("512/power_wrestler.png");
    
    private static final String ENERGY_ORB_DEFAULT_GRAY = getImageResourcePath("512/card_default_gray_orb.png");
    private static final String CARD_ENERGY_ORB = getImageResourcePath("512/card_small_orb.png");
    
    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = getImageResourcePath("1024/attack_wrestler.png");
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = getImageResourcePath("1024/skill_wrestler.png");
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = getImageResourcePath("1024/power_wrestler.png");
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = getImageResourcePath("1024/card_default_gray_orb.png");
    
    // Character assets
    private static final String THE_WRESTLER_BUTTON = getImageResourcePath("charSelect/WrestlerCharacterButton.png");
    private static final String THE_WRESTLER_PORTRAIT = getImageResourcePath("charSelect/WrestlerCharacterPortraitBG.png");
    public static final String THE_WRESTLER_SHOULDER_1 = getImageResourcePath("char/wrestler/shoulder.png");
    public static final String THE_WRESTLER_SHOULDER_2 = getImageResourcePath("char/wrestler/shoulder2.png");
    public static final String THE_WRESTLER_CORPSE = getImageResourcePath("char/wrestler/corpse.png");

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = getImageResourcePath("Badge.png");
    
    // Atlas and JSON files for the Animations
    public static final String THE_WRESTLER_SKELETON_ATLAS = getImageResourcePath("char/wrestler/skeleton.atlas");
    public static final String THE_WRESTLER_SKELETON_JSON = getImageResourcePath("/char/wrestler/skeleton.json");
    public static final String THE_WRESTLER_STATIC_CHARACTER_SPRITE = getImageResourcePath("char/wrestler/main3.png");

    private static Map<String, Keyword> keywords;

    // =============== MAKE IMAGE PATHS =================

    public static String getCardResourcePath(String resourcePath) {
        return getImageResourcePath("cards/" + resourcePath);
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getImageResourcePath("relics/" + resourcePath);
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getImageResourcePath("relics/outline/" + resourcePath);
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getImageResourcePath("orb/" + resourcePath);
    }
    
    public static String makePowerPath(String resourcePath) {
        return getImageResourcePath("powers/" + resourcePath);
    }
    
    public static String makeEventPath(String resourcePath) {
        return getImageResourcePath("events/" + resourcePath);
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public WrestlerMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */
      
        setModID(MOD_ID);
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename theWrestlerResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of WrestlerCharacter with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        
        // 3. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than WrestlerCharacter. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");
        
        logger.info("Creating the color " + AbstractCardEnum.THE_WRESTLER_GRAY);
        
        BaseMod.addColor(AbstractCardEnum.THE_WRESTLER_GRAY, WRESTLER_GRAY, WRESTLER_GRAY, WRESTLER_GRAY,
            WRESTLER_GRAY, WRESTLER_GRAY, WRESTLER_GRAY, WRESTLER_GRAY,
            ATTACK_WRESTLER_GRAY, SKILL_WRESTLER_GRAY, POWER_WRESTLER_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theWrestlerDefaultSettings.setProperty(ENABLE_WRESTLER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("WrestlerCharacter", "theWrestlerConfig", theWrestlerDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableWrestler = config.getBool(ENABLE_WRESTLER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        modID = ID;

        /*
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        InputStream in = WrestlerMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
        */
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        InputStream in = WrestlerMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = WrestlerMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(RESOURCE_FOLDER_NAME + "/Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) {
            if (!packageName.equals(getModID())) {
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID());
            }
            if (!resourcePathExists.exists()) {
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }
        }
    }
    
    // ====== YOU CAN EDIT AGAIN ======
    
    // ======= REGISTER ASSETS ========

    @SuppressWarnings("unchecked")
    private HashMap<String, Sfx> getSoundsMap() {
        return (HashMap<String, Sfx>) ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
    }

    private void registerSfx() {
        HashMap<String, Sfx> reflectedMap = getSoundsMap();
        reflectedMap.put("THUD_MEDIUM_1", new Sfx(getAudioResourcePath("TheWrestler_ThudMedium1.ogg")));
    }

    // ======= /REGISTER ASSETS/ ========


    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        WrestlerMod defaultmod = new WrestlerMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================


    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + WrestlerCharEnum.THE_WRESTLER);
        
        BaseMod.addCharacter(new WrestlerCharacter(IN_GAME_CHARACTER_NAME, WrestlerCharEnum.THE_WRESTLER),
            THE_WRESTLER_BUTTON, THE_WRESTLER_PORTRAIT, WrestlerCharEnum.THE_WRESTLER);
        
        receiveEditPotions();
        logger.info("Added " +  WrestlerCharEnum.THE_WRESTLER);
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");

        registerSfx();

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableWrestler, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            enableWrestler = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("wrestlerMod", "theWrestlerConfig", theWrestlerDefaultSettings);
                config.setBool(ENABLE_WRESTLER_SETTINGS, enableWrestler);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
        
        // =============== EVENTS =================
        
        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to h ave a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"
        BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        
        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");
        
        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "theWrestlerEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(WrestlerPotion.class, WRESTLER_POTION_LIQUID, WRESTLER_POTION_HYBRID,
            WRESTLER_POTION_SPOTS, WrestlerPotion.POTION_ID, WrestlerCharEnum.THE_WRESTLER);
        
        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");
        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new Headgear(), AbstractCardEnum.THE_WRESTLER_GRAY);

        // This adds a relic to the Shared pool. Every character can find this relic.
        //  BaseMod.addRelic(new WrestlerRelic2(), RelicType.SHARED);
        
        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        // UnlockTracker.markRelicAsSeen(BottledWrestlerRelic.ID);
        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
//        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variabls");
        // Add the Custom Dynamic variabls
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
        
        logger.info("Adding cards");
        // Add the cards
        // Don't comment out/delete these cards (yet). You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        BaseMod.addCard(new TakeToTheMat());


        BaseMod.addCard(new OrbSkill());
        BaseMod.addCard(new DefaultSecondMagicNumberSkill());
        BaseMod.addCard(new DefaultCommonAttack());
        BaseMod.addCard(new DefaultAttackWithVariable());
        BaseMod.addCard(new DefaultCommonSkill());
        BaseMod.addCard(new DefaultCommonPower());
        BaseMod.addCard(new DefaultUncommonSkill());
        BaseMod.addCard(new DefaultUncommonAttack());
        BaseMod.addCard(new DefaultUncommonPower());
        BaseMod.addCard(new DefaultRareAttack());
        BaseMod.addCard(new DefaultRareSkill());
        BaseMod.addCard(new DefaultRarePower());

        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        // This is so that they are all "seen" in the library, for people who like to look at the card list
        // before playing your mod.

        // TODO: create "addCardToBasePool" method for cards that begin unlocked
        UnlockTracker.unlockCard(OrbSkill.ID);
        UnlockTracker.unlockCard(DefaultSecondMagicNumberSkill.ID);
        UnlockTracker.unlockCard(DefaultCommonAttack.ID);
        UnlockTracker.unlockCard(DefaultAttackWithVariable.ID);
        UnlockTracker.unlockCard(DefaultCommonSkill.ID);
        UnlockTracker.unlockCard(DefaultCommonPower.ID);
        UnlockTracker.unlockCard(DefaultUncommonSkill.ID);
        UnlockTracker.unlockCard(DefaultUncommonAttack.ID);
        UnlockTracker.unlockCard(DefaultUncommonPower.ID);
        UnlockTracker.unlockCard(DefaultRareAttack.ID);
        UnlockTracker.unlockCard(DefaultRareSkill.ID);
        UnlockTracker.unlockCard(DefaultRarePower.ID);
        UnlockTracker.unlockCard(TakeToTheMat.ID);
        
        logger.info("Done adding cards!");
    }
    
    // There are better ways to do this than listing every single individual card, but I do not want to complicate things
    // in a "tutorial" mod. This will do and it's completely ok to use. If you ever want to clean up and
    // shorten all the imports, go look take a look at other mods, such as Hubris.
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================


    private static String getLanguageString() {
        // Note to translators - add your language here (by alphabetical order).
        switch (Settings.language) {
            default:
                return "eng";
        }
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning editing strings for mod with ID: " + getModID());

        String language = getLanguageString();
        String l10nPath = RESOURCE_FOLDER_NAME +"/localization/";
        BaseMod.loadCustomStringsFile(RelicStrings.class, l10nPath + language + "/RelicStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, l10nPath + language + "/CardStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, l10nPath + language + "/PowerStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, l10nPath + language + "/UIStrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, l10nPath + language + "/CharacterStrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, l10nPath + language + "/PotionStrings.json");
        BaseMod.loadCustomStringsFile(OrbStrings.class, l10nPath + language + "/OrbStrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, l10nPath + language + "/EventStrings.json");

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
            RESOURCE_FOLDER_NAME + "/localization/eng/CardStrings.json");
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
            RESOURCE_FOLDER_NAME + "/localization/eng/PowerStrings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
            RESOURCE_FOLDER_NAME + "/localization/eng/RelicStrings.json");
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
            RESOURCE_FOLDER_NAME + "/localization/eng/EventStrings.json");
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
            RESOURCE_FOLDER_NAME + "/localization/eng/PotionStrings.json");
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
            RESOURCE_FOLDER_NAME + "/localization/eng/CharacterStrings.json");
        
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
            RESOURCE_FOLDER_NAME + "/localization/eng/OrbStrings.json");
        
        logger.info("Done editing strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword


        final Gson gson = new Gson();
        String language = getLanguageString();

        String keywordStrings =
            Gdx.files.internal(RESOURCE_FOLDER_NAME + "/localization/" + language + "/KeywordStrings.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));

        Type typeToken = new TypeToken<Map<String, Keyword>>(){}.getType();

        keywords = gson.fromJson(keywordStrings, typeToken);
        keywords.forEach((k, v) -> {
            BaseMod.addKeyword(v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });

//        Gson gson = new Gson();
//        String language = getLanguageString();
//
//        String json = Gdx.files.internal(RESOURCE_FOLDER_NAME + "/localization/" + language + "/KeywordStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
//        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
//
//        if (keywords != null) {
//            for (Keyword keyword : keywords) {
//                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
//            }
//        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String id) {
        return getModID() + ":" + id;
    }
}
