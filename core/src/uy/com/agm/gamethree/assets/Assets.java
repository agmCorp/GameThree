package uy.com.agm.gamethree.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.audio.music.AssetMusic;
import uy.com.agm.gamethree.assets.audio.sound.AssetSounds;
import uy.com.agm.gamethree.assets.fonts.AssetFonts;
import uy.com.agm.gamethree.assets.i18n.AssetI18NGameThree;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.assets.sprites.AssetBridge;
import uy.com.agm.gamethree.assets.sprites.AssetBulletA;
import uy.com.agm.gamethree.assets.sprites.AssetBulletB;
import uy.com.agm.gamethree.assets.sprites.AssetBulletC;
import uy.com.agm.gamethree.assets.sprites.AssetBulletD;
import uy.com.agm.gamethree.assets.sprites.AssetBulletE;
import uy.com.agm.gamethree.assets.sprites.AssetColOne;
import uy.com.agm.gamethree.assets.sprites.AssetColThree;
import uy.com.agm.gamethree.assets.sprites.AssetColTwo;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyBullet;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyEight;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyEleven;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyFive;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyFour;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyNine;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyOne;
import uy.com.agm.gamethree.assets.sprites.AssetEnemySeven;
import uy.com.agm.gamethree.assets.sprites.AssetEnemySix;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyTen;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyThree;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyTwelve;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyTwo;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionA;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionB;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionC;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionD;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionE;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionF;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionG;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionH;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionI;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionJ;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionK;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionL;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionM;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionN;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionO;
import uy.com.agm.gamethree.assets.sprites.AssetFinalEnemyLevelOne;
import uy.com.agm.gamethree.assets.sprites.AssetFinalEnemyLevelThree;
import uy.com.agm.gamethree.assets.sprites.AssetFinalEnemyLevelTwo;
import uy.com.agm.gamethree.assets.sprites.AssetGhostMode;
import uy.com.agm.gamethree.assets.sprites.AssetHero;
import uy.com.agm.gamethree.assets.sprites.AssetHeroBullet;
import uy.com.agm.gamethree.assets.sprites.AssetMaps;
import uy.com.agm.gamethree.assets.sprites.AssetPowerBox;
import uy.com.agm.gamethree.assets.sprites.AssetPowerFive;
import uy.com.agm.gamethree.assets.sprites.AssetPowerFour;
import uy.com.agm.gamethree.assets.sprites.AssetPowerOne;
import uy.com.agm.gamethree.assets.sprites.AssetPowerThree;
import uy.com.agm.gamethree.assets.sprites.AssetPowerTwo;
import uy.com.agm.gamethree.assets.sprites.AssetShield;
import uy.com.agm.gamethree.assets.sprites.AssetSilverBullet;
import uy.com.agm.gamethree.assets.sprites.AssetSplat;
import uy.com.agm.gamethree.assets.sprites.AssetToughMode;

/**
 * Created by amorales on 14/12/2017.
 */

public class Assets implements Disposable, AssetErrorListener {
    private static final String TAG = Assets.class.getName();

    // Constants

    // Sound FXs
    public static final String FX_FILE_GAME_OVER = "audio/sounds/gameOver.ogg";
    public static final String FX_FILE_BUMP = "audio/sounds/bump.ogg";
    public static final String FX_FILE_CRACK = "audio/sounds/crack.ogg";
    public static final String FX_FILE_DEAD = "audio/sounds/dead.ogg";
    public static final String FX_FILE_ENEMY_SHOOT = "audio/sounds/enemyShoot.ogg";
    public static final String FX_FILE_HERO_SHOOT = "audio/sounds/heroShoot.ogg";
    public static final String FX_FILE_SHOOT = "audio/sounds/shoot.ogg";
    public static final String FX_FILE_HERO_SHOOT_EMPTY = "audio/sounds/ohoh.ogg";
    public static final String FX_FILE_HERO_SHOOT_SWISH = "audio/sounds/multiSwish.ogg";
    public static final String FX_FILE_HIT = "audio/sounds/hit.ogg";
    public static final String FX_FILE_OPEN_POWER_BOX = "audio/sounds/openPowerBox.ogg";
    public static final String FX_FILE_CLOCK = "audio/sounds/clock.ogg";
    public static final String FX_FILE_PICK_UP_COL_ONE = "audio/sounds/pickUpColOne.ogg";
    public static final String FX_FILE_PICK_UP_COL_TWO = "audio/sounds/pickUpColTwo.ogg";
    public static final String FX_FILE_PICK_UP_COL_THREE = "audio/sounds/pickUpColThree.ogg";
    public static final String FX_FILE_PICK_UP_COL_SILVER_BULLET = "audio/sounds/pickUpColSilverBullet.ogg";
    public static final String FX_FILE_PICK_UP_POWER_ONE = "audio/sounds/pickUpPowerOne.ogg";
    public static final String FX_FILE_PICK_UP_POWER_TWO = "audio/sounds/pickUpPowerTwo.ogg";
    public static final String FX_FILE_PICK_UP_POWER_THREE = "audio/sounds/pickUpPowerThree.ogg";
    public static final String FX_FILE_PICK_UP_POWER_FOUR = "audio/sounds/pickUpPowerFour.ogg";
    public static final String FX_FILE_PICK_UP_POWER_FIVE = "audio/sounds/pickUpPowerFive.ogg";
    public static final String FX_FILE_ABILITY_POWER_DOWN = "audio/sounds/abilityPowerDown.ogg";
    public static final String FX_FILE_WEAPON_POWER_DOWN = "audio/sounds/weaponPowerDown.ogg";
    public static final String FX_FILE_BEEP_A = "audio/sounds/beepA.ogg";
    public static final String FX_FILE_BEEP_B = "audio/sounds/beepB.ogg";
    public static final String FX_FILE_BEEP_C = "audio/sounds/beepC.ogg";
    public static final String FX_FILE_SHOW_UP_COL_ONE = "audio/sounds/showUpColOne.ogg";
    public static final String FX_FILE_SHOW_UP_COL_TWO = "audio/sounds/showUpColTwo.ogg";
    public static final String FX_FILE_SHOW_UP_COL_THREE = "audio/sounds/showUpColThree.ogg";
    public static final String FX_FILE_SHOW_UP_COL_SILVER_BULLET = "audio/sounds/showUpColSilverBullet.ogg";
    public static final String FX_FILE_SHOW_UP_POWER_ONE = "audio/sounds/showUpPowerOne.ogg";
    public static final String FX_FILE_SHOW_UP_POWER_TWO = "audio/sounds/showUpPowerTwo.ogg";
    public static final String FX_FILE_SHOW_UP_POWER_THREE = "audio/sounds/showUpPowerThree.ogg";
    public static final String FX_FILE_SHOW_UP_POWER_FOUR = "audio/sounds/showUpPowerFour.ogg";
    public static final String FX_FILE_SHOW_UP_POWER_FIVE = "audio/sounds/showUpPowerFive.ogg";
    public static final String FX_FILE_TIME_IS_UP = "audio/sounds/timeIsUp.ogg";
    public static final String FX_FILE_FINAL_ENEMY_POWER_UP = "audio/sounds/finalEnemyPowerUp.ogg";
    public static final String FX_FILE_FINAL_ENEMY_POWER_DOWN = "audio/sounds/finalEnemyPowerDown.ogg";
    public static final String FX_FILE_FINAL_ENEMY_EXPLOSION = "audio/sounds/finalEnemyExplosion.ogg";
    public static final String FX_FILE_FINAL_ENEMY_HIT = "audio/sounds/finalEnemyHit.ogg";
    public static final String FX_FILE_FINAL_ENEMY_INTRO = "audio/sounds/fear.ogg";
    public static final String FX_FILE_LEVEL_COMPLETED = "audio/sounds/levelCompleted.ogg";
    public static final String FX_FILE_BOUNCE = "audio/sounds/boing.ogg";
    public static final String FX_FILE_CLICK = "audio/sounds/click.ogg";
    public static final String FX_FILE_APPLAUSE = "audio/sounds/applause.ogg";
    public static final String FX_FILE_SQUISH = "audio/sounds/squish.ogg";
    public static final String FX_FILE_FROZEN = "audio/sounds/frozen.ogg";
    public static final String FX_FILE_BEAM = "audio/sounds/finalEnemyPowerUp.ogg";
    public static final String FX_FILE_PUM = "audio/sounds/pum.ogg";
    public static final String FX_FILE_CHIRP = "audio/sounds/chirp.ogg";
    public static final String FX_FILE_SQUEAK = "audio/sounds/squeak.ogg";
    public static final String FX_FILE_SPRING = "audio/sounds/spring.ogg";
    public static final String FX_FILE_GROWL = "audio/sounds/growl.ogg";
    public static final String FX_FILE_WARBLE = "audio/sounds/warble.ogg";
    public static final String FX_FILE_DRAGON = "audio/sounds/dragon.ogg";
    public static final String FX_FILE_BUZZ = "audio/sounds/buzz.ogg";
    public static final String FX_FILE_WHISTLE = "audio/sounds/whistle.ogg";
    public static final String FX_FILE_JUICY = "audio/sounds/juicy.ogg";
    public static final String FX_FILE_MARCHING = "audio/sounds/marching.ogg";
    public static final String FX_FILE_FLAP = "audio/sounds/flap.ogg";
    public static final String FX_FILE_BITE = "audio/sounds/bite.ogg";
    public static final String FX_FILE_ROCK_SCRAPE = "audio/sounds/rockScrape.ogg";
    public static final String FX_FILE_PEBBLES = "audio/sounds/pebbles.ogg";
    public static final String FX_FILE_BLADE = "audio/sounds/blade.ogg";
    public static final String FX_FILE_FLUTTER = "audio/sounds/flutter.ogg";

    // Music
    public static final String MUSIC_FILE_MAIN_MENU = "audio/music/mainMenu.ogg";
    public static final String MUSIC_FILE_FINAL_ENEMY_FIGHT = "audio/music/finalEnemyFight.ogg";
    public static final String MUSIC_FILE_LEVEL_ONE = "audio/music/levelOne.ogg";
    public static final String MUSIC_FILE_LEVEL_TWO = "audio/music/levelTwo.ogg";
    public static final String MUSIC_FILE_LEVEL_THREE = "audio/music/levelThree.ogg";

    // Location of description file for texture atlas (dynamic game objects)
    private static final String TEXTURE_ATLAS_OBJECTS = "atlas/dynamicObjects/dynamicObjects.atlas";

    // Location of description file for texture atlas (GUI)
    private static final String TEXTURE_ATLAS_UI = "atlas/scene2d/scene2d.atlas";

    // Location of the map file for each level
    public static final String MAP_FILE_LEVEL_ONE = "levels/levelOne/levelOne.tmx";
    public static final String MAP_FILE_LEVEL_TWO = "levels/levelTwo/levelTwo.tmx";
    public static final String MAP_FILE_LEVEL_THREE = "levels/levelThree/levelThree.tmx";
    public static final String MAP_FILE_LEVEL_FOUR = "levels/levelFour/levelFour.tmx";

    private static Assets instance;
    private AssetManager assetManager;
    private AssetI18NGameThree i18NGameThree;
    private AssetScene2d scene2d;
    private AssetFonts fonts;
    private AssetHero hero;
    private AssetEnemyOne enemyOne;
    private AssetEnemyTwo enemyTwo;
    private AssetEnemyThree enemyThree;
    private AssetEnemyFour enemyFour;
    private AssetEnemyFive enemyFive;
    private AssetEnemySix enemySix;
    private AssetEnemySeven enemySeven;
    private AssetEnemyEight enemyEight;
    private AssetEnemyNine enemyNine;
    private AssetEnemyTen enemyTen;
    private AssetEnemyEleven enemyEleven;
    private AssetEnemyTwelve enemyTwelve;
    private AssetBridge kinematicBridge;
    private AssetSplat splat;
    private AssetPowerBox powerBox;
    private AssetColOne colOne;
    private AssetColTwo colTwo;
    private AssetColThree colThree;
    private AssetPowerOne powerOne;
    private AssetPowerTwo powerTwo;
    private AssetPowerThree powerThree;
    private AssetPowerFour powerFour;
    private AssetPowerFive powerFive;
    private AssetExplosionA explosionA;
    private AssetExplosionB explosionB;
    private AssetExplosionC explosionC;
    private AssetExplosionD explosionD;
    private AssetExplosionE explosionE;
    private AssetExplosionF explosionF;
    private AssetExplosionG explosionG;
    private AssetExplosionH explosionH;
    private AssetExplosionI explosionI;
    private AssetExplosionJ explosionJ;
    private AssetExplosionK explosionK;
    private AssetExplosionL explosionL;
    private AssetExplosionM explosionM;
    private AssetExplosionN explosionN;
    private AssetExplosionO explosionO;
    private AssetHeroBullet heroBullet;
    private AssetEnemyBullet enemyBullet;
    private AssetSilverBullet silverBullet;
    private AssetGhostMode ghostMode;
    private AssetShield shield;
    private AssetBulletA bulletA;
    private AssetBulletB bulletB;
    private AssetBulletC bulletC;
    private AssetBulletD bulletD;
    private AssetBulletE bulletE;
    private AssetToughMode toughMode;
    private AssetFinalEnemyLevelOne finalEnemyLevelOne;
    private AssetFinalEnemyLevelTwo finalEnemyLevelTwo;
    private AssetFinalEnemyLevelThree finalEnemyLevelThree;

    private AssetMaps maps;
    private AssetSounds sounds;
    private AssetMusic music;

    // Singleton: prevent instantiation from other classes
    private Assets() {
    }

    // Singleton: retrieve instance
    public static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;

        // Set asset manager error handler
        assetManager.setErrorListener(this);

        // Load i18n
        loadI18NGameThree();

        // Load texture atlas (dynamic game objects)
        loadTextureAtlasObjects();

        // Load texture atlas (UI)
        loadTextureAtlasUI();

        // Load map for each level
        loadMaps();

        // Load all sounds
        loadSounds();

        // Load all music
        loadMusic();
    }

    public void finishLoading() {
        Gdx.app.debug(TAG, "***************************");
        Gdx.app.debug(TAG, "***** Number of assets loaded: " + assetManager.getAssetNames().size);
        for (String assetName : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "***** Asset: " + assetName);
        }
        Gdx.app.debug(TAG, "***************************");

        // Enable texture filtering for pixel smoothing (UI)
        TextureAtlas atlasUI = assetManager.get(TEXTURE_ATLAS_UI);
        for (Texture texture : atlasUI.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        // Enable texture filtering for pixel smoothing (dynamic game objects)
        TextureAtlas atlasDynamicObjects = assetManager.get(TEXTURE_ATLAS_OBJECTS);
        for (Texture texture : atlasDynamicObjects.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        i18NGameThree = new AssetI18NGameThree(assetManager);
        scene2d = new AssetScene2d(atlasUI);
        fonts = new AssetFonts();
        hero = new AssetHero(atlasDynamicObjects);
        enemyOne = new AssetEnemyOne(atlasDynamicObjects);
        enemyTwo = new AssetEnemyTwo(atlasDynamicObjects);
        enemyThree = new AssetEnemyThree(atlasDynamicObjects);
        enemyFour = new AssetEnemyFour(atlasDynamicObjects);
        enemyFive = new AssetEnemyFive(atlasDynamicObjects);
        enemySix = new AssetEnemySix(atlasDynamicObjects);
        enemySeven = new AssetEnemySeven(atlasDynamicObjects);
        enemyEight = new AssetEnemyEight(atlasDynamicObjects);
        enemyNine = new AssetEnemyNine(atlasDynamicObjects);
        enemyTen = new AssetEnemyTen(atlasDynamicObjects);
        enemyEleven = new AssetEnemyEleven(atlasDynamicObjects);
        enemyTwelve = new AssetEnemyTwelve(atlasDynamicObjects);
        kinematicBridge = new AssetBridge(atlasDynamicObjects);
        splat = new AssetSplat(atlasDynamicObjects);
        powerBox = new AssetPowerBox(atlasDynamicObjects);
        colOne = new AssetColOne(atlasDynamicObjects);
        colTwo = new AssetColTwo(atlasDynamicObjects);
        colThree = new AssetColThree(atlasDynamicObjects);
        powerOne = new AssetPowerOne(atlasDynamicObjects);
        powerTwo = new AssetPowerTwo(atlasDynamicObjects);
        powerThree = new AssetPowerThree(atlasDynamicObjects);
        powerFour = new AssetPowerFour(atlasDynamicObjects);
        powerFive = new AssetPowerFive(atlasDynamicObjects);
        explosionA = new AssetExplosionA(atlasDynamicObjects);
        explosionB = new AssetExplosionB(atlasDynamicObjects);
        explosionC = new AssetExplosionC(atlasDynamicObjects);
        explosionD = new AssetExplosionD(atlasDynamicObjects);
        explosionE = new AssetExplosionE(atlasDynamicObjects);
        explosionF = new AssetExplosionF(atlasDynamicObjects);
        explosionG = new AssetExplosionG(atlasDynamicObjects);
        explosionH = new AssetExplosionH(atlasDynamicObjects);
        explosionI = new AssetExplosionI(atlasDynamicObjects);
        explosionJ = new AssetExplosionJ(atlasDynamicObjects);
        explosionK = new AssetExplosionK(atlasDynamicObjects);
        explosionL = new AssetExplosionL(atlasDynamicObjects);
        explosionM = new AssetExplosionM(atlasDynamicObjects);
        explosionN = new AssetExplosionN(atlasDynamicObjects);
        explosionO = new AssetExplosionO(atlasDynamicObjects);
        heroBullet = new AssetHeroBullet(atlasDynamicObjects);
        enemyBullet = new AssetEnemyBullet(atlasDynamicObjects);
        silverBullet = new AssetSilverBullet(atlasDynamicObjects);
        ghostMode = new AssetGhostMode(atlasDynamicObjects);
        shield = new AssetShield(atlasDynamicObjects);
        bulletA = new AssetBulletA(atlasDynamicObjects);
        bulletB = new AssetBulletB(atlasDynamicObjects);
        bulletC = new AssetBulletC(atlasDynamicObjects);
        bulletD = new AssetBulletD(atlasDynamicObjects);
        bulletE = new AssetBulletE(atlasDynamicObjects);
        toughMode = new AssetToughMode(atlasDynamicObjects);
        finalEnemyLevelOne = new AssetFinalEnemyLevelOne(atlasDynamicObjects);
        finalEnemyLevelTwo = new AssetFinalEnemyLevelTwo(atlasDynamicObjects);
        finalEnemyLevelThree = new AssetFinalEnemyLevelThree(atlasDynamicObjects);
        maps = new AssetMaps(assetManager);
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }

    private void loadI18NGameThree() {
        assetManager.load("i18n/I18NGameThreeBundle", I18NBundle.class);
    }

    private void loadTextureAtlasObjects() {
        assetManager.load(TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
    }

    private void loadTextureAtlasUI() {
        assetManager.load(TEXTURE_ATLAS_UI, TextureAtlas.class);
    }

    private void loadMaps() {
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load(MAP_FILE_LEVEL_ONE, TiledMap.class);
        assetManager.load(MAP_FILE_LEVEL_TWO, TiledMap.class);
        assetManager.load(MAP_FILE_LEVEL_THREE, TiledMap.class);
        assetManager.load(MAP_FILE_LEVEL_FOUR, TiledMap.class);
    }

    private void loadSounds() {
        assetManager.load(FX_FILE_GAME_OVER, Sound.class);
        assetManager.load(FX_FILE_BUMP, Sound.class);
        assetManager.load(FX_FILE_CRACK, Sound.class);
        assetManager.load(FX_FILE_DEAD, Sound.class);
        assetManager.load(FX_FILE_ENEMY_SHOOT, Sound.class);
        assetManager.load(FX_FILE_HERO_SHOOT, Sound.class);
        assetManager.load(FX_FILE_SHOOT, Sound.class);
        assetManager.load(FX_FILE_HERO_SHOOT_EMPTY, Sound.class);
        assetManager.load(FX_FILE_HERO_SHOOT_SWISH, Sound.class);
        assetManager.load(FX_FILE_HIT, Sound.class);
        assetManager.load(FX_FILE_OPEN_POWER_BOX, Sound.class);
        assetManager.load(FX_FILE_CLOCK, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_COL_ONE, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_COL_TWO, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_COL_THREE, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_COL_SILVER_BULLET, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_POWER_ONE, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_POWER_TWO, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_POWER_THREE, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_POWER_FOUR, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_POWER_FIVE, Sound.class);
        assetManager.load(FX_FILE_ABILITY_POWER_DOWN, Sound.class);
        assetManager.load(FX_FILE_WEAPON_POWER_DOWN, Sound.class);
        assetManager.load(FX_FILE_BEEP_A, Sound.class);
        assetManager.load(FX_FILE_BEEP_B, Sound.class);
        assetManager.load(FX_FILE_BEEP_C, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_COL_ONE, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_COL_TWO, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_COL_THREE, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_COL_SILVER_BULLET, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_POWER_ONE, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_POWER_TWO, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_POWER_THREE, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_POWER_FOUR, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_POWER_FIVE, Sound.class);
        assetManager.load(FX_FILE_TIME_IS_UP, Sound.class);
        assetManager.load(FX_FILE_FINAL_ENEMY_POWER_UP, Sound.class);
        assetManager.load(FX_FILE_FINAL_ENEMY_POWER_DOWN, Sound.class);
        assetManager.load(FX_FILE_FINAL_ENEMY_EXPLOSION, Sound.class);
        assetManager.load(FX_FILE_FINAL_ENEMY_HIT, Sound.class);
        assetManager.load(FX_FILE_FINAL_ENEMY_INTRO, Sound.class);
        assetManager.load(FX_FILE_LEVEL_COMPLETED, Sound.class);
        assetManager.load(FX_FILE_BOUNCE, Sound.class);
        assetManager.load(FX_FILE_CLICK, Sound.class);
        assetManager.load(FX_FILE_APPLAUSE, Sound.class);
        assetManager.load(FX_FILE_SQUISH, Sound.class);
        assetManager.load(FX_FILE_FROZEN, Sound.class);
        assetManager.load(FX_FILE_PUM, Sound.class);
        assetManager.load(FX_FILE_CHIRP, Sound.class);
        assetManager.load(FX_FILE_SQUEAK, Sound.class);
        assetManager.load(FX_FILE_SPRING, Sound.class);
        assetManager.load(FX_FILE_GROWL, Sound.class);
        assetManager.load(FX_FILE_WARBLE, Sound.class);
        assetManager.load(FX_FILE_DRAGON, Sound.class);
        assetManager.load(FX_FILE_BUZZ, Sound.class);
        assetManager.load(FX_FILE_WHISTLE, Sound.class);
        assetManager.load(FX_FILE_JUICY, Sound.class);
        assetManager.load(FX_FILE_MARCHING, Sound.class);
        assetManager.load(FX_FILE_FLAP, Sound.class);
        assetManager.load(FX_FILE_BITE, Sound.class);
        assetManager.load(FX_FILE_ROCK_SCRAPE, Sound.class);
        assetManager.load(FX_FILE_PEBBLES, Sound.class);
        assetManager.load(FX_FILE_BLADE, Sound.class);
        assetManager.load(FX_FILE_FLUTTER, Sound.class);
    }

    private void loadMusic() {
        assetManager.load(MUSIC_FILE_MAIN_MENU, Music.class);
        assetManager.load(MUSIC_FILE_FINAL_ENEMY_FIGHT, Music.class);
        assetManager.load(MUSIC_FILE_LEVEL_ONE, Music.class);
        assetManager.load(MUSIC_FILE_LEVEL_TWO, Music.class);
        assetManager.load(MUSIC_FILE_LEVEL_THREE, Music.class);
    }

    @Override
    public void error(AssetDescriptor assetDescriptor, Throwable throwable) {
        Gdx.app.error(TAG, "Error loading asset: '" + assetDescriptor.fileName + "'", (Exception) throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.getDefaultSmall().dispose();
        fonts.getDefaultNormal().dispose();
        fonts.getDefaultBig().dispose();
    }

    public AssetI18NGameThree getI18NGameThree() {
        return i18NGameThree;
    }

    public AssetScene2d getScene2d() {
        return scene2d;
    }

    public AssetFonts getFonts() {
        return fonts;
    }

    public AssetHero getHero() {
        return hero;
    }

    public AssetEnemyOne getEnemyOne() {
        return enemyOne;
    }

    public AssetEnemyTwo getEnemyTwo() {
        return enemyTwo;
    }

    public AssetEnemyThree getEnemyThree() {
        return enemyThree;
    }

    public AssetEnemyFour getEnemyFour() {
        return enemyFour;
    }

    public AssetEnemyFive getEnemyFive() {
        return enemyFive;
    }

    public AssetEnemySix getEnemySix() {
        return enemySix;
    }

    public AssetEnemySeven getEnemySeven() {
        return enemySeven;
    }

    public AssetEnemyEight getEnemyEight() {
        return enemyEight;
    }

    public AssetEnemyNine getEnemyNine() {
        return enemyNine;
    }

    public AssetEnemyTen getEnemyTen() {
        return enemyTen;
    }

    public AssetEnemyEleven getEnemyEleven() {
        return enemyEleven;
    }

    public AssetEnemyTwelve getEnemyTwelve() {
        return enemyTwelve;
    }

    public AssetBridge getKinematicBridge() {
        return kinematicBridge;
    }

    public AssetSplat getSplat() {
        return splat;
    }

    public AssetPowerBox getPowerBox() {
        return powerBox;
    }

    public AssetColOne getColOne() {
        return colOne;
    }

    public AssetColTwo getColTwo() {
        return colTwo;
    }

    public AssetColThree getColThree() {
        return colThree;
    }

    public AssetPowerOne getPowerOne() {
        return powerOne;
    }

    public AssetPowerTwo getPowerTwo() {
        return powerTwo;
    }

    public AssetPowerThree getPowerThree() {
        return powerThree;
    }

    public AssetPowerFour getPowerFour() {
        return powerFour;
    }

    public AssetPowerFive getPowerFive() {
        return powerFive;
    }

    public AssetExplosionA getExplosionA() {
        return explosionA;
    }

    public AssetExplosionB getExplosionB() {
        return explosionB;
    }

    public AssetExplosionC getExplosionC() {
        return explosionC;
    }

    public AssetExplosionD getExplosionD() {
        return explosionD;
    }

    public AssetExplosionE getExplosionE() {
        return explosionE;
    }

    public AssetExplosionF getExplosionF() {
        return explosionF;
    }

    public AssetExplosionG getExplosionG() {
        return explosionG;
    }

    public AssetExplosionH getExplosionH() {
        return explosionH;
    }

    public AssetExplosionI getExplosionI() {
        return explosionI;
    }

    public AssetExplosionJ getExplosionJ() {
        return explosionJ;
    }

    public AssetExplosionK getExplosionK() {
        return explosionK;
    }

    public AssetExplosionL getExplosionL() {
        return explosionL;
    }

    public AssetExplosionM getExplosionM() {
        return explosionM;
    }

    public AssetExplosionN getExplosionN() {
        return explosionN;
    }

    public AssetExplosionO getExplosionO() {
        return explosionO;
    }

    public AssetHeroBullet getHeroBullet() {
        return heroBullet;
    }

    public AssetEnemyBullet getEnemyBullet() {
        return enemyBullet;
    }

    public AssetSilverBullet getSilverBullet() {
        return silverBullet;
    }

    public AssetGhostMode getGhostMode() {
        return ghostMode;
    }

    public AssetShield getShield() {
        return shield;
    }

    public AssetBulletA getBulletA() {
        return bulletA;
    }

    public AssetBulletB getBulletB() {
        return bulletB;
    }

    public AssetBulletC getBulletC() {
        return bulletC;
    }

    public AssetBulletD getBulletD() {
        return bulletD;
    }

    public AssetBulletE getBulletE() {
        return bulletE;
    }

    public AssetToughMode getToughMode() {
        return toughMode;
    }

    public AssetFinalEnemyLevelOne getFinalEnemyLevelOne() {
        return finalEnemyLevelOne;
    }

    public AssetFinalEnemyLevelTwo getFinalEnemyLevelTwo() {
        return finalEnemyLevelTwo;
    }

    public AssetFinalEnemyLevelThree getFinalEnemyLevelThree() {
        return finalEnemyLevelThree;
    }

    public AssetMaps getMaps() {
        return maps;
    }

    public AssetSounds getSounds() {
        return sounds;
    }

    public AssetMusic getMusic() {
        return music;
    }

    public <T> String getAssetFileName(T asset) {
        return assetManager.getAssetFileName(asset);
    }
}
