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
import uy.com.agm.gamethree.assets.sprites.AssetColOne;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyBullet;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyEight;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyFive;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyFour;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyNine;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyOne;
import uy.com.agm.gamethree.assets.sprites.AssetEnemySeven;
import uy.com.agm.gamethree.assets.sprites.AssetEnemySix;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyThree;
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
import uy.com.agm.gamethree.assets.sprites.AssetFinalEnemyLevelOne;
import uy.com.agm.gamethree.assets.sprites.AssetFinalEnemyLevelThree;
import uy.com.agm.gamethree.assets.sprites.AssetFinalEnemyLevelTwo;
import uy.com.agm.gamethree.assets.sprites.AssetGhostMode;
import uy.com.agm.gamethree.assets.sprites.AssetHero;
import uy.com.agm.gamethree.assets.sprites.AssetHeroBullet;
import uy.com.agm.gamethree.assets.sprites.AssetMaps;
import uy.com.agm.gamethree.assets.sprites.AssetPowerBox;
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
    public static final String FX_FILE_HERO_SHOOT_EMPTY = "audio/sounds/gunReload.ogg";
    public static final String FX_FILE_HERO_SHOOT_SWISH = "audio/sounds/multiSwish.ogg";
    public static final String FX_FILE_HIT = "audio/sounds/hit.ogg";
    public static final String FX_FILE_OPEN_POWER_BOX = "audio/sounds/openPowerBox.ogg";
    public static final String FX_FILE_CLOCK = "audio/sounds/clock.ogg";
    public static final String FX_FILE_PICK_UP_COL_ONE = "audio/sounds/pickUpColOne.ogg";
    public static final String FX_FILE_PICK_UP_COL_SILVER_BULLET = "audio/sounds/pickUpColSilverBullet.ogg";
    public static final String FX_FILE_PICK_UP_POWER_ONE = "audio/sounds/pickUpPowerOne.ogg";
    public static final String FX_FILE_PICK_UP_POWER_TWO = "audio/sounds/pickUpPowerTwo.ogg";
    public static final String FX_FILE_PICK_UP_POWER_THREE = "audio/sounds/pickUpPowerThree.ogg";
    public static final String FX_FILE_PICK_UP_POWER_FOUR = "audio/sounds/pickUpPowerFour.ogg";
    public static final String FX_FILE_POWER_DOWN = "audio/sounds/powerDown.ogg";
    public static final String FX_FILE_BEEP_A = "audio/sounds/beepA.ogg";
    public static final String FX_FILE_BEEP_B = "audio/sounds/beepB.ogg";
    public static final String FX_FILE_SHOW_UP_COL_ONE = "audio/sounds/showUpColOne.ogg";
    public static final String FX_FILE_SHOW_UP_COL_SILVER_BULLET = "audio/sounds/showUpColSilverBullet.ogg";
    public static final String FX_FILE_SHOW_UP_POWER_ONE = "audio/sounds/showUpPowerOne.ogg";
    public static final String FX_FILE_SHOW_UP_POWER_TWO = "audio/sounds/showUpPowerTwo.ogg";
    public static final String FX_FILE_SHOW_UP_POWER_THREE = "audio/sounds/showUpPowerThree.ogg";
    public static final String FX_FILE_SHOW_UP_POWER_FOUR = "audio/sounds/showUpPowerFour.ogg";
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
    public static final String FX_FILE_POP = "audio/sounds/pop.ogg";
    public static final String FX_FILE_GROWL = "audio/sounds/growl.ogg";

    // Music
    public static final String MUSIC_FILE_MAIN_MENU = "audio/music/mainMenu.ogg";
    public static final String MUSIC_FILE_FINAL_ENEMY_FIGHT = "audio/music/finalEnemyFight.ogg";
    public static final String MUSIC_FILE_LEVEL_ONE = "audio/music/levelOne.ogg";
    public static final String MUSIC_FILE_LEVEL_TWO = "audio/music/levelTwo.ogg";
    public static final String MUSIC_FILE_LEVEL_THREE = "audio/music/levelThree.ogg";

    // Location of description file for texture atlas (dinamic game objects)
    private static final String TEXTURE_ATLAS_OBJECTS = "atlas/dinamicObjects/dinamicObjects.atlas";

    // Location of description file for texture atlas (GUI)
    private static final String TEXTURE_ATLAS_UI = "atlas/scene2d/scene2d.atlas";

    // Location of the map file for each level
    public static final String MAP_FILE_LEVEL_ONE = "levels/levelOne/levelOne.tmx";
    public static final String MAP_FILE_LEVEL_TWO = "levels/levelTwo/levelTwo.tmx";
    public static final String MAP_FILE_LEVEL_THREE = "levels/levelThree/levelThree.tmx";

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
    private AssetBridge kinematicBridge;
    private AssetSplat splat;
    private AssetPowerBox powerBox;
    private AssetColOne colOne;
    private AssetPowerOne powerOne;
    private AssetPowerTwo powerTwo;
    private AssetPowerThree powerThree;
    private AssetPowerFour powerFour;
    private AssetExplosionA explosionA;
    private AssetExplosionB explosionB;
    private AssetExplosionC explosionC;
    private AssetExplosionD explosionD;
    private AssetExplosionE explosionE;
    private AssetExplosionF explosionF;
    private AssetExplosionG explosionG;
    private AssetExplosionH explosionH;
    private AssetExplosionI explosionI;
    private AssetHeroBullet heroBullet;
    private AssetEnemyBullet enemyBullet;
    private AssetSilverBullet silverBullet;
    private AssetGhostMode ghostMode;
    private AssetShield shield;
    private AssetBulletA bulletA;
    private AssetBulletB bulletB;
    private AssetBulletC bulletC;
    private AssetBulletD bulletD;
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
    }

    private void loadSounds() {
        assetManager.load(FX_FILE_GAME_OVER, Sound.class);
        assetManager.load(FX_FILE_BUMP, Sound.class);
        assetManager.load(FX_FILE_CRACK, Sound.class);
        assetManager.load(FX_FILE_DEAD, Sound.class);
        assetManager.load(FX_FILE_ENEMY_SHOOT, Sound.class);
        assetManager.load(FX_FILE_HERO_SHOOT, Sound.class);
        assetManager.load(FX_FILE_HERO_SHOOT_EMPTY, Sound.class);
        assetManager.load(FX_FILE_HERO_SHOOT_SWISH, Sound.class);
        assetManager.load(FX_FILE_HIT, Sound.class);
        assetManager.load(FX_FILE_OPEN_POWER_BOX, Sound.class);
        assetManager.load(FX_FILE_CLOCK, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_COL_ONE, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_COL_SILVER_BULLET, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_POWER_ONE, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_POWER_TWO, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_POWER_THREE, Sound.class);
        assetManager.load(FX_FILE_PICK_UP_POWER_FOUR, Sound.class);
        assetManager.load(FX_FILE_POWER_DOWN, Sound.class);
        assetManager.load(FX_FILE_BEEP_A, Sound.class);
        assetManager.load(FX_FILE_BEEP_B, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_COL_ONE, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_COL_SILVER_BULLET, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_POWER_ONE, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_POWER_TWO, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_POWER_THREE, Sound.class);
        assetManager.load(FX_FILE_SHOW_UP_POWER_FOUR, Sound.class);
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
        assetManager.load(FX_FILE_POP, Sound.class);
        assetManager.load(FX_FILE_GROWL, Sound.class);
    }

    private void loadMusic() {
        assetManager.load(MUSIC_FILE_MAIN_MENU, Music.class);
        assetManager.load(MUSIC_FILE_FINAL_ENEMY_FIGHT, Music.class);
        assetManager.load(MUSIC_FILE_LEVEL_ONE, Music.class);
        assetManager.load(MUSIC_FILE_LEVEL_TWO, Music.class);
        assetManager.load(MUSIC_FILE_LEVEL_THREE, Music.class);
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;

        // Set asset manager error handler
        assetManager.setErrorListener(this);

        // Load i18n
        loadI18NGameThree();

        // Load texture atlas (dinamic game objects)
        loadTextureAtlasObjects();

        // Load texture atlas (UI)
        loadTextureAtlasUI();

        // Load map for each level
        loadMaps();

        // Load all sounds
        loadSounds();

        // Load all music
        loadMusic();

        // Start loading assets and wait until finished
        assetManager.finishLoading();

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

        // Enable texture filtering for pixel smoothing (dinamic game objects)
        TextureAtlas atlasDinamicObjects = assetManager.get(TEXTURE_ATLAS_OBJECTS);
        for (Texture texture : atlasDinamicObjects.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        i18NGameThree = new AssetI18NGameThree(assetManager);
        scene2d = new AssetScene2d(atlasUI);
        fonts = new AssetFonts();
        hero = new AssetHero(atlasDinamicObjects);
        enemyOne = new AssetEnemyOne(atlasDinamicObjects);
        enemyTwo = new AssetEnemyTwo(atlasDinamicObjects);
        enemyThree = new AssetEnemyThree(atlasDinamicObjects);
        enemyFour = new AssetEnemyFour(atlasDinamicObjects);
        enemyFive = new AssetEnemyFive(atlasDinamicObjects);
        enemySix = new AssetEnemySix(atlasDinamicObjects);
        enemySeven = new AssetEnemySeven(atlasDinamicObjects);
        enemyEight = new AssetEnemyEight(atlasDinamicObjects);
        enemyNine = new AssetEnemyNine(atlasDinamicObjects);
        kinematicBridge = new AssetBridge(atlasDinamicObjects);
        splat = new AssetSplat(atlasDinamicObjects);
        powerBox = new AssetPowerBox(atlasDinamicObjects);
        colOne = new AssetColOne(atlasDinamicObjects);
        powerOne = new AssetPowerOne(atlasDinamicObjects);
        powerTwo = new AssetPowerTwo(atlasDinamicObjects);
        powerThree = new AssetPowerThree(atlasDinamicObjects);
        powerFour = new AssetPowerFour(atlasDinamicObjects);
        explosionA = new AssetExplosionA(atlasDinamicObjects);
        explosionB = new AssetExplosionB(atlasDinamicObjects);
        explosionC = new AssetExplosionC(atlasDinamicObjects);
        explosionD = new AssetExplosionD(atlasDinamicObjects);
        explosionE = new AssetExplosionE(atlasDinamicObjects);
        explosionF = new AssetExplosionF(atlasDinamicObjects);
        explosionG = new AssetExplosionG(atlasDinamicObjects);
        explosionH = new AssetExplosionH(atlasDinamicObjects);
        explosionI = new AssetExplosionI(atlasDinamicObjects);
        heroBullet = new AssetHeroBullet(atlasDinamicObjects);
        enemyBullet = new AssetEnemyBullet(atlasDinamicObjects);
        silverBullet = new AssetSilverBullet(atlasDinamicObjects);
        ghostMode = new AssetGhostMode(atlasDinamicObjects);
        shield = new AssetShield(atlasDinamicObjects);
        bulletA = new AssetBulletA(atlasDinamicObjects);
        bulletB = new AssetBulletB(atlasDinamicObjects);
        bulletC = new AssetBulletC(atlasDinamicObjects);
        bulletD = new AssetBulletD(atlasDinamicObjects);
        toughMode = new AssetToughMode(atlasDinamicObjects);
        finalEnemyLevelOne = new AssetFinalEnemyLevelOne(atlasDinamicObjects);
        finalEnemyLevelTwo = new AssetFinalEnemyLevelTwo(atlasDinamicObjects);
        finalEnemyLevelThree = new AssetFinalEnemyLevelThree(atlasDinamicObjects);
        maps = new AssetMaps(assetManager);
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }

    @Override
    public void error(AssetDescriptor assetDescriptor, Throwable throwable) {
        Gdx.app.error(TAG, "Error loding asset: '" + assetDescriptor.fileName + "'", (Exception) throwable);
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
}
