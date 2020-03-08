package thewrestler;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.helpers.RelicType;
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
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.cards.attack.*;
import thewrestler.cards.curse.Predictable;
import thewrestler.cards.power.*;
import thewrestler.cards.skill.*;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.enums.WrestlerCharEnum;
import thewrestler.patches.powers.OnApplyPowerPatchInsert;
import thewrestler.potions.BravadoPotion;
import thewrestler.potions.CobraPotion;
import thewrestler.potions.GrapplePotion;
import thewrestler.relics.*;
import thewrestler.signaturemoves.cards.Chokeslam;
import thewrestler.signaturemoves.cards.DragonGate;
import thewrestler.signaturemoves.cards.Piledriver;
import thewrestler.signaturemoves.moveinfos.AbstractSignatureMoveInfo;
import thewrestler.ui.WrestlerPenaltyCardInfoPanel;
import thewrestler.ui.WrestlerCombatInfoPanel;
import thewrestler.ui.WrestlerSignatureMovePanel;
import thewrestler.util.BasicUtils;
import thewrestler.util.IDCheckDontTouchPls;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.CombatInfo;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;
import thewrestler.variables.DefaultCustomVariable;
import thewrestler.variables.DefaultSecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@SpireInitializer
public class WrestlerMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostCreateStartingDeckSubscriber,
        PostDungeonInitializeSubscriber,
        PostInitializeSubscriber,
        StartGameSubscriber,
        OnStartBattleSubscriber,
        PostEnergyRechargeSubscriber,
        PreMonsterTurnSubscriber,
        PostBattleSubscriber,
        OnCardUseSubscriber {

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
    public static final Color WRESTLER_ORANGE = CardHelper.getColor(220, 140, 0);

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
    private static final String ATTACK_WRESTLER_ORANGE = getImageResourcePath("512/attack_wrestler.png");
    private static final String SKILL_WRESTLER_ORANGE = getImageResourcePath("512/skill_wrestler.png");
    private static final String POWER_WRESTLER_ORANGE = getImageResourcePath("512/power_wrestler.png");

    private static final String ENERGY_ORB_DEFAULT_ORANGE = getImageResourcePath("512/card_default_gray_orb.png");
    private static final String CARD_ENERGY_ORB = getImageResourcePath("512/card_small_orb.png");

    private static final String ATTACK_DEFAULT_ORANGE_PORTRAIT = getImageResourcePath("1024/attack_wrestler.png");
    private static final String SKILL_DEFAULT_ORANGE_PORTRAIT = getImageResourcePath("1024/skill_wrestler.png");
    private static final String POWER_DEFAULT_ORANGE_PORTRAIT = getImageResourcePath("1024/power_wrestler.png");
    private static final String ENERGY_ORB_DEFAULT_ORANGE_PORTRAIT = getImageResourcePath("1024/card_default_gray_orb.png");

    // Character assets
    private static final String THE_WRESTLER_BUTTON = getImageResourcePath("charSelect/WrestlerCharacterButton.png");
    private static final String THE_WRESTLER_PORTRAIT = getImageResourcePath("charSelect/WrestlerCharacterPortraitBG.png");
    public static final String THE_WRESTLER_SHOULDER_1 = getImageResourcePath("char/wrestler/shoulder.png");
    public static final String THE_WRESTLER_SHOULDER_2 = getImageResourcePath("char/wrestler/shoulder2.png");
    public static final String THE_WRESTLER_CORPSE = getImageResourcePath("char/wrestler/corpse.png");

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = getImageResourcePath("Badge.png");

    private static Map<String, Keyword> keywords;


    public static WrestlerPenaltyCardInfoPanel penaltyCardInfoPanel;
    public static WrestlerCombatInfoPanel combatInfoPanel;
    public static WrestlerSignatureMovePanel signatureMovePanel;

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

        setModID(MOD_ID);

        logger.info("Done subscribing");

        logger.info("Creating the color " + AbstractCardEnum.THE_WRESTLER_ORANGE);

        BaseMod.addColor(AbstractCardEnum.THE_WRESTLER_ORANGE, WRESTLER_ORANGE, WRESTLER_ORANGE, WRESTLER_ORANGE,
            WRESTLER_ORANGE, WRESTLER_ORANGE, WRESTLER_ORANGE, WRESTLER_ORANGE,
            ATTACK_WRESTLER_ORANGE, SKILL_WRESTLER_ORANGE, POWER_WRESTLER_ORANGE, ENERGY_ORB_DEFAULT_ORANGE,
            ATTACK_DEFAULT_ORANGE_PORTRAIT, SKILL_DEFAULT_ORANGE_PORTRAIT, POWER_DEFAULT_ORANGE_PORTRAIT,
            ENERGY_ORB_DEFAULT_ORANGE_PORTRAIT, CARD_ENERGY_ORB);

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
    }

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
        reflectedMap.put("BOMB_DROP_EXPLODE_1", new Sfx(getAudioResourcePath("TheWrestler_BombDropExplode1.ogg")));
        reflectedMap.put("BONE_CRUNCH_1", new Sfx(getAudioResourcePath("TheWrestler_BoneCrunch1.ogg")));
        reflectedMap.put("BONE_CRUNCH_2", new Sfx(getAudioResourcePath("TheWrestler_BoneCrunch2.ogg")));
        reflectedMap.put("BOO_CROWD_1", new Sfx(getAudioResourcePath("TheWrestler_BooCrowd1.ogg")));
        reflectedMap.put("BOOM_LOWFREQ_1", new Sfx(getAudioResourcePath("TheWrestler_ExplosionBombLowFrequency1.ogg")));
        reflectedMap.put("BOUNCE_METALLIC_1", new Sfx(getAudioResourcePath("TheWrestler_BounceMetallic1.ogg")));
        reflectedMap.put("BUBBLE_SHORT_1", new Sfx(getAudioResourcePath("TheWrestler_BubbleShort1.ogg")));
        reflectedMap.put("CAMERA_SHUTTER_1", new Sfx(getAudioResourcePath("TheWrestler_CameraShutter1.ogg")));
        reflectedMap.put("CHALK_WRITING_1", new Sfx(getAudioResourcePath("TheWrestler_ChalkWriting1.ogg")));
        reflectedMap.put("CHEER_CROWD_1", new Sfx(getAudioResourcePath("TheWrestler_CheerCrowd1.ogg")));
        reflectedMap.put("COWBELL_1", new Sfx(getAudioResourcePath("TheWrestler_Cowbell1.ogg")));
        reflectedMap.put("DOOR_HATCH_OPEN_1", new Sfx(getAudioResourcePath("TheWrestler_DoorHatchOpen1.ogg")));
        reflectedMap.put("DRILL_SPIN_1", new Sfx(getAudioResourcePath("TheWrestler_DrillSpin1.ogg")));
        reflectedMap.put("ELECTRO_INTERFERENCE_1", new Sfx(getAudioResourcePath("TheWrestler_ElectroInterference1.ogg")));
        reflectedMap.put("GATE_OPEN_RUSTY_1", new Sfx(getAudioResourcePath("TheWrestler_GateRustyOpen1.ogg")));
        reflectedMap.put("GONG_STRIKE_1", new Sfx(getAudioResourcePath("TheWrestler_GongStrike1.ogg")));
        reflectedMap.put("GONG_STRIKE_2", new Sfx(getAudioResourcePath("TheWrestler_GongStrike2.ogg")));
        reflectedMap.put("GRUNT_SHORT_1", new Sfx(getAudioResourcePath("TheWrestler_GruntShort1.ogg")));
        reflectedMap.put("GRUNT_SHORT_2", new Sfx(getAudioResourcePath("TheWrestler_GruntShort2.ogg")));
        reflectedMap.put("GUILLOTINE_1", new Sfx(getAudioResourcePath("TheWrestler_Guillotine1.ogg")));
        reflectedMap.put("LASER_SHORT_1", new Sfx(getAudioResourcePath("TheWrestler_LaserShort1.ogg")));
        reflectedMap.put("SNAP_LIGAMENT_1", new Sfx(getAudioResourcePath("TheWrestler_SnapLigament1.ogg")));
        reflectedMap.put("SPLAT_WET_1", new Sfx(getAudioResourcePath("TheWrestler_SplatWet1.ogg")));
        reflectedMap.put("SPRINGBOARD_1", new Sfx(getAudioResourcePath("TheWrestler_Springboard1.ogg")));
        reflectedMap.put("THUD_MEDIUM_1", new Sfx(getAudioResourcePath("TheWrestler_ThudMedium1.ogg")));
        reflectedMap.put("TONE_ELECTRONIC_1", new Sfx(getAudioResourcePath("TheWrestler_ToneElectronic1.ogg")));
        reflectedMap.put("WHISTLE_BLOW_1", new Sfx(getAudioResourcePath("TheWrestler_WhistleBlow1.ogg")));
        reflectedMap.put("WHISTLE_BLOW_SHORT_1", new Sfx(getAudioResourcePath("TheWrestler_WhistleBlowShort1.ogg")));
        reflectedMap.put("WHISTLE_STEAM_1", new Sfx(getAudioResourcePath("TheWrestler_WhistleSteam1.ogg")));
        reflectedMap.put("WHOOSH_ROCKET_1", new Sfx(getAudioResourcePath("TheWrestler_WhooshRocket1.ogg")));
        reflectedMap.put("WHOOSH_ROPE_1", new Sfx(getAudioResourcePath("TheWrestler_WhooshRope1.ogg")));
        reflectedMap.put("WING_FLUTTER_1", new Sfx(getAudioResourcePath("TheWrestler_WingFlutter1.ogg")));
        reflectedMap.put("YELL_PAIN_1", new Sfx(getAudioResourcePath("TheWrestler_YellPain1.ogg")));
        reflectedMap.put("METAL_MAN_RIFF_1", new Sfx(getAudioResourcePath("music/TheWrestler_MetalManRiff1.ogg")));
        reflectedMap.put("METAL_MAN_RIFF_2", new Sfx(getAudioResourcePath("music/TheWrestler_MetalManRiff2.ogg")));
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
//        BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }

    // =============== / POST-INITIALIZE/ =================


    // ================ ADD POTIONS ===================

    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // If you want your potion to be class-specific, add the player class argument
        BaseMod.addPotion(BravadoPotion.class, BravadoPotion.LIQUID_COLOR, BravadoPotion.HYBRID_COLOR,
            BravadoPotion.SPOTS_COLOR, BravadoPotion.POTION_ID);

        BaseMod.addPotion(CobraPotion.class, CobraPotion.LIQUID_COLOR, CobraPotion.HYBRID_COLOR,
            CobraPotion.SPOTS_COLOR, CobraPotion.POTION_ID);

        BaseMod.addPotion(GrapplePotion.class, GrapplePotion.LIQUID_COLOR, GrapplePotion.HYBRID_COLOR,
            GrapplePotion.SPOTS_COLOR, GrapplePotion.POTION_ID);

        logger.info("Done editing potions");
    }

    // ================ /ADD POTIONS/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");
        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new Headgear(), AbstractCardEnum.THE_WRESTLER_ORANGE);
        BaseMod.addRelicToCustomPool(new ImprovedHeadgear(), AbstractCardEnum.THE_WRESTLER_ORANGE);
        BaseMod.addRelicToCustomPool(new PenaltyCardRelic(), AbstractCardEnum.THE_WRESTLER_ORANGE);

        BaseMod.addRelicToCustomPool(new BrutesTrophy(), AbstractCardEnum.THE_WRESTLER_ORANGE);

        // TODO: fix this or remove it
        //  BaseMod.addRelicToCustomPool(new FightCard(), AbstractCardEnum.THE_WRESTLER_ORANGE);
        BaseMod.addRelicToCustomPool(new PeoplesCrown(), AbstractCardEnum.THE_WRESTLER_ORANGE);

        BaseMod.addRelicToCustomPool(new LuckyTrunks(), AbstractCardEnum.THE_WRESTLER_ORANGE);

        // TODO: reflavor and reenable this (since its flavor was appropriated for the starter relic)
//        BaseMod.addRelicToCustomPool(new RefereesWhistle(), AbstractCardEnum.THE_WRESTLER_ORANGE);

        // This adds a relic to the Shared pool. Every character can find this relic.
        // TODO: put this behind a Config flag, a la Hayseed
        BaseMod.addRelic(new FancyFootgear(), RelicType.SHARED);

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        // UnlockTracker.markRelicAsSeen(Headgear.ID);
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

        // TODO: rename these
        BaseMod.addCard(new WrestlerStrike());
        BaseMod.addCard(new WrestlerDirtyStrike());
        BaseMod.addCard(new WrestlerDefend());

        BaseMod.addCard(new AlleyOop());
        BaseMod.addCard(new AtomicDrop());
        BaseMod.addCard(new Backfist());
        BaseMod.addCard(new BearHug());
        BaseMod.addCard(new BlowOff());
        BaseMod.addCard(new Butterfly());
        BaseMod.addCard(new Brainbuster());
        BaseMod.addCard(new CageMatch());
        BaseMod.addCard(new CheapHeat());
        BaseMod.addCard(new CheapShot());
        BaseMod.addCard(new CleanFinish());
        BaseMod.addCard(new CloverleafAttack());
        BaseMod.addCard(new CobraClutch());
        BaseMod.addCard(new CurtainJerker());
        BaseMod.addCard(new DivingStomp());
        BaseMod.addCard(new ElbowDrop());
        BaseMod.addCard(new EyePoke());
        BaseMod.addCard(new Facewash());
        BaseMod.addCard(new FanFavorite());
        BaseMod.addCard(new Feud());
        BaseMod.addCard(new FloatOver());
        BaseMod.addCard(new FrogSplash());
        BaseMod.addCard(new Guillotine());
        BaseMod.addCard(new HairPull());
        BaseMod.addCard(new Hammerlock());
        BaseMod.addCard(new Hardway());
        BaseMod.addCard(new Headlock());
        BaseMod.addCard(new HeartPunch());
        BaseMod.addCard(new HeelTurn());
        BaseMod.addCard(new HotShot());
        BaseMod.addCard(new IronMan());
        BaseMod.addCard(new Kayfabe());
        BaseMod.addCard(new LowBlow());
        BaseMod.addCard(new Matrix());
        BaseMod.addCard(new Neckbreaker());
        BaseMod.addCard(new Octopus());
        BaseMod.addCard(new OffTheRopes());
        BaseMod.addCard(new Opportunist());
        BaseMod.addCard(new Pendulum());
        BaseMod.addCard(new PhantomBump());
        BaseMod.addCard(new Pinfall());
        BaseMod.addCard(new PlayToTheCrowd());
        BaseMod.addCard(new Powerbomb());
        BaseMod.addCard(new ProvenTactics());
        BaseMod.addCard(new Redemption());
        BaseMod.addCard(new Ropewalk());
        BaseMod.addCard(new RunTheRing());
        BaseMod.addCard(new Sandbag());
        BaseMod.addCard(new Scrapper());
        BaseMod.addCard(new Screwjob());
        BaseMod.addCard(new Sharpshooter());
        BaseMod.addCard(new Shortarm());
        BaseMod.addCard(new Showboat());
        BaseMod.addCard(new SideRoll());
        BaseMod.addCard(new Springboard());
        BaseMod.addCard(new SquareOff());
        BaseMod.addCard(new Squeeze());
        BaseMod.addCard(new StomachClaw());
        BaseMod.addCard(new Swerve());
        BaseMod.addCard(new TagIn());
        BaseMod.addCard(new TakeToTheMat());
        BaseMod.addCard(new Technician());
        BaseMod.addCard(new TriangleChoke());
        BaseMod.addCard(new TripleThreat());
        BaseMod.addCard(new WindUpKick());

        //        BaseMod.addCard(new HalfNelson());
        //        BaseMod.addCard(new SleeperHold());
        //        BaseMod.addCard(new NearFall());
        //        BaseMod.addCard(new MainEvent());


        //        BaseMod.addCard(new SafetyTag());
        //        BaseMod.addCard(new TakeAPowder());


        // NEED REWORK BECAUSE OF PENALTY CARD REWORK
        // BaseMod.addCard(new CannedHeat());



        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        // This is so that they are all "seen" in the library, for people who like to look at the card list
        // before playing your mod.

        // TODO: create "addCardToBasePool" method for cards that begin unlocked

        UnlockTracker.unlockCard(WrestlerStrike.ID);
        UnlockTracker.unlockCard(WrestlerDirtyStrike.ID);
        UnlockTracker.unlockCard(WrestlerDefend.ID);

        UnlockTracker.unlockCard(AlleyOop.ID);
        UnlockTracker.unlockCard(AtomicDrop.ID);
        UnlockTracker.unlockCard(Backfist.ID);
        UnlockTracker.unlockCard(BearHug.ID);
        UnlockTracker.unlockCard(BlowOff.ID);
        UnlockTracker.unlockCard(Brainbuster.ID);
        UnlockTracker.unlockCard(CageMatch.ID);;
        UnlockTracker.unlockCard(CannedHeat.ID);
        UnlockTracker.unlockCard(CheapHeat.ID);
        UnlockTracker.unlockCard(CheapShot.ID);
        UnlockTracker.unlockCard(CleanFinish.ID);
        UnlockTracker.unlockCard(Cloverleaf.ID);
        UnlockTracker.unlockCard(CloverleafAttack.ID);
        UnlockTracker.unlockCard(CobraClutch.ID);
        UnlockTracker.unlockCard(CurtainJerker.ID);
        UnlockTracker.unlockCard(DivingStomp.ID);
        UnlockTracker.unlockCard(ElbowDrop.ID);
        UnlockTracker.unlockCard(EyePoke.ID);
        UnlockTracker.unlockCard(Facewash.ID);
        UnlockTracker.unlockCard(FanFavorite.ID);
        UnlockTracker.unlockCard(Feud.ID);
        UnlockTracker.unlockCard(FloatOver.ID);
        UnlockTracker.unlockCard(FrogSplash.ID);
        UnlockTracker.unlockCard(HalfNelson.ID);
        UnlockTracker.unlockCard(Guillotine.ID);
        UnlockTracker.unlockCard(HairPull.ID);
        UnlockTracker.unlockCard(Hammerlock.ID);
        UnlockTracker.unlockCard(Hardway.ID);
        UnlockTracker.unlockCard(Headlock.ID);
        UnlockTracker.unlockCard(HeartPunch.ID);
        UnlockTracker.unlockCard(HeelTurn.ID);
        UnlockTracker.unlockCard(HotShot.ID);
        UnlockTracker.unlockCard(IronMan.ID);
        UnlockTracker.unlockCard(Kayfabe.ID);
        UnlockTracker.unlockCard(LowBlow.ID);
        UnlockTracker.unlockCard(MainEvent.ID);
        UnlockTracker.unlockCard(Matrix.ID);
        UnlockTracker.unlockCard(NearFall.ID);
        UnlockTracker.unlockCard(Neckbreaker.ID);
        UnlockTracker.unlockCard(Octopus.ID);
        UnlockTracker.unlockCard(OffTheRopes.ID);
        UnlockTracker.unlockCard(Opportunist.ID);
        UnlockTracker.unlockCard(Pendulum.ID);
        UnlockTracker.unlockCard(PhantomBump.ID);
        UnlockTracker.unlockCard(Pinfall.ID);
        UnlockTracker.unlockCard(PlayToTheCrowd.ID);
        UnlockTracker.unlockCard(Powerbomb.ID);
        UnlockTracker.unlockCard(ProvenTactics.ID);
        UnlockTracker.unlockCard(Redemption.ID);
        UnlockTracker.unlockCard(Ropewalk.ID);
        UnlockTracker.unlockCard(RunTheRing.ID);
        UnlockTracker.unlockCard(Sandbag.ID);
        UnlockTracker.unlockCard(Scrapper.ID);
        UnlockTracker.unlockCard(Screwjob.ID);
        UnlockTracker.unlockCard(Sharpshooter.ID);
        UnlockTracker.unlockCard(Shortarm.ID);
        UnlockTracker.unlockCard(Showboat.ID);
        UnlockTracker.unlockCard(SideRoll.ID);
        UnlockTracker.unlockCard(SleeperHold.ID);
        UnlockTracker.unlockCard(Springboard.ID);
        UnlockTracker.unlockCard(SquareOff.ID);
        UnlockTracker.unlockCard(Squeeze.ID);
        UnlockTracker.unlockCard(StomachClaw.ID);
        UnlockTracker.unlockCard(Swerve.ID);
        UnlockTracker.unlockCard(TagIn.ID);
        UnlockTracker.unlockCard(TakeAPowder.ID);
        UnlockTracker.unlockCard(TakeToTheMat.ID);
        UnlockTracker.unlockCard(Technician.ID);
        UnlockTracker.unlockCard(TriangleChoke.ID);
        UnlockTracker.unlockCard(TripleThreat.ID);
        UnlockTracker.unlockCard(WindUpKick.ID);

        BaseMod.addCard(new Predictable());
        UnlockTracker.unlockCard(Predictable.ID);

        // FOR PLAYTESTING
        BaseMod.addCard(new Chokeslam());
        UnlockTracker.unlockCard(Chokeslam.ID);

        BaseMod.addCard(new DragonGate());
        UnlockTracker.unlockCard(DragonGate.ID);

        BaseMod.addCard(new Piledriver());
        UnlockTracker.unlockCard(Piledriver.ID);

//        UnlockTracker.unlockCard(SafetyTag.ID);

        logger.info("Done adding cards!");

        BaseMod.addSaveField(AbstractSignatureMoveInfo.SIGNATURE_CARD_SAVABLE_KEY,
            AbstractSignatureMoveInfo.getCardSavable());
        BaseMod.addSaveField(AbstractSignatureMoveInfo.SIGNATURE_UPGRADE_SAVABLE_KEY,
            AbstractSignatureMoveInfo.getUpgradeSavable());
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
        // Multiword thewrestler.keywords on cards are done With_Underscores
        //
        // If you're using multiword thewrestler.keywords, the first element in your NAMES array in your thewrestler.keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        final Gson gson = new Gson();
        String language = getLanguageString();

        String keywordStrings =
            Gdx.files.internal(RESOURCE_FOLDER_NAME + "/localization/" + language + "/KeywordStrings.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));

        Type typeToken = new TypeToken<Map<String, Keyword>>(){}.getType();

        keywords = gson.fromJson(keywordStrings, typeToken);

        Map<String, Keyword> unscopedKeywords = keywords.entrySet().stream()
            .filter(entry -> !startsWithModPrefix(entry.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

        Map<String, Keyword> modScopedKeywords = keywords.entrySet().stream()
            .filter(entry -> startsWithModPrefix(entry.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

        unscopedKeywords.forEach((k, v) -> {
            BaseMod.addKeyword(v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });

        modScopedKeywords.forEach((k, v) -> {
            BaseMod.addKeyword(getModKeywordPrefix(), v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    public static String getModKeywordPrefix() {
        return getModID().toLowerCase() + ":";
    }

    // the prefix isn't lowercased in KeywordStrings.json, so don't lowercase when doing this comparison
    public static boolean startsWithModPrefix(String string) {
        return string.startsWith(getModID() + ":");
    }

    // ================ /LOAD THE KEYWORDS/ ===================

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String id) {
        return getModID() + ":" + id;
    }

    @Override
    public void receiveStartGame() {
        penaltyCardInfoPanel = new WrestlerPenaltyCardInfoPanel();
        combatInfoPanel = new WrestlerCombatInfoPanel();
        signatureMovePanel = new WrestlerSignatureMovePanel();
        logger.info("WresterMod:receiveStartGame called");
        if (AbstractSignatureMoveInfo.isSaveDataValid()) {
            AbstractSignatureMoveInfo.loadSaveData();
            WrestlerMod.logger.info("WrestlerCharacter:setSignatureMoveInfo set info from save: " + WrestlerCharacter.getSignatureMoveInfo());
        } else {
            WrestlerMod.logger.info("WrestlerMod::receiveOnBattleStart initializing signatureMoveInfo");
            WrestlerCharacter.setSignatureMoveInfo(WrestlerCharacter.initializeSignatureMoveInfo());
        }

        if (BasicUtils.isPlayingAsWrestler()) {
            WrestlerCharacter.initializePenaltyCardInfo();
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        if (BasicUtils.isPlayingAsWrestler()) {
            OnApplyPowerPatchInsert.powerActionList.clear();
            combatInfoPanel.atStartOfCombat();
            penaltyCardInfoPanel.atEndOfCombat();
            signatureMovePanel.atStartOfCombat();
            WrestlerCharacter.getSignatureMoveInfo().atStartOfCombat();
            WrestlerCharacter.getPenaltyCardInfo().atStartOfCombat();
            CombatInfo.atStartOfCombat();
        }

        StartOfCombatListener.triggerStartOfCombatPowers();
        StartOfCombatListener.triggerStartOfCombatCards();
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        combatInfoPanel.updateCardCounts();
        signatureMovePanel.onCardUsed(abstractCard);
        WrestlerCharacter.getPenaltyCardInfo().onCardUsed(abstractCard);
        penaltyCardInfoPanel.onCardUsed(abstractCard);
    }

    @Override
    public void receivePostEnergyRecharge() {
        penaltyCardInfoPanel.atStartOfTurn();
        combatInfoPanel.atStartOfTurn();
        signatureMovePanel.atStartOfTurn();
        WrestlerCharacter.getSignatureMoveInfo().atStartOfTurn();
        WrestlerCharacter.getPenaltyCardInfo().atStartOfTurn();
        CombatInfo.atStartOfTurn();
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        penaltyCardInfoPanel.atEndOfCombat();
        combatInfoPanel.atEndOfCombat();
        signatureMovePanel.atEndOfCombat();
        WrestlerCharacter.getSignatureMoveInfo().atEndOfCombat();
        WrestlerCharacter.getPenaltyCardInfo().atEndOfCombat();
        CombatInfo.atEndOfCombat();
    }

    public static void atEndOfPlayerTurn() {
        WrestlerCharacter.getSignatureMoveInfo().atEndOfTurn();
        WrestlerCharacter.getPenaltyCardInfo().atEndOfTurn();
    }

    @Override
    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster) {
        penaltyCardInfoPanel.atEndOfTurn();
        combatInfoPanel.atEndOfTurn();
        signatureMovePanel.atEndOfTurn();
        return true;
    }

    static public void onExhaustCardHook(AbstractCard card) {
        WrestlerCharacter.getSignatureMoveInfo().onCardExhausted(card);
        WrestlerCharacter.getPenaltyCardInfo().onCardExhausted(card);
    }

    public static Keyword getKeyword(String key) {
        return keywords.get(key);
    }

    @Override
    public void receivePostCreateStartingDeck(AbstractPlayer.PlayerClass playerClass, CardGroup cardGroup) {
        AbstractSignatureMoveInfo.resetForNewRun();
        PenaltyCardInfo.resetForNewCombat();
        WrestlerCharacter.resetPenaltyCardInfo();
    }

    @Override
    public void receivePostDungeonInitialize() {

    }
}
