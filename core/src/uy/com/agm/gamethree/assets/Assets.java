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

import uy.com.agm.gamethree.assets.audio.music.AssetMusic;
import uy.com.agm.gamethree.assets.audio.sound.AssetSounds;
import uy.com.agm.gamethree.assets.sprites.AssetBulletA;
import uy.com.agm.gamethree.assets.sprites.AssetColOne;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyBullet;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyOne;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyTwo;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionA;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionB;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionC;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionD;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionE;
import uy.com.agm.gamethree.assets.sprites.AssetFinalEnemyLevelOne;
import uy.com.agm.gamethree.assets.sprites.AssetFinalEnemyLevelOnePower;
import uy.com.agm.gamethree.assets.sprites.AssetFonts;
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
import uy.com.agm.gamethree.assets.sprites.AssetToughMode;
import uy.com.agm.gamethree.game.Constants;

/**
 * Created by amorales on 14/12/2017.
 */

public class Assets implements Disposable, AssetErrorListener {
    private static final String TAG = Assets.class.getName();

    private static Assets instance;

    private AssetManager assetManager;

    private AssetFonts fonts;
    private AssetHero hero;
    private AssetEnemyOne enemyOne;
    private AssetEnemyTwo enemyTwo;
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
    private AssetHeroBullet heroBullet;
    private AssetEnemyBullet enemyBullet;
    private AssetGhostMode ghostMode;
    private AssetShield shield;
    private AssetBulletA bulletA;
    private AssetToughMode toughMode;
    private AssetFinalEnemyLevelOne finalEnemyLevelOne;
    private AssetFinalEnemyLevelOnePower finalEnemyLevelOnePower;

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

        // Load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

        // Load map for each level
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load(Constants.MAP_FILE_LEVEL_ONE, TiledMap.class);

        // Load sounds
        assetManager.load(Constants.FX_FILE_BUMP, Sound.class);
        assetManager.load(Constants.FX_FILE_CRACK, Sound.class);
        assetManager.load(Constants.FX_FILE_DEAD, Sound.class);
        assetManager.load(Constants.FX_FILE_ENEMY_SHOOT, Sound.class);
        assetManager.load(Constants.FX_FILE_HERO_SHOOT, Sound.class);
        assetManager.load(Constants.FX_FILE_HIT, Sound.class);
        assetManager.load(Constants.FX_FILE_OPEN_POWERBOX, Sound.class);
        assetManager.load(Constants.FX_FILE_LEVEL_TIMER, Sound.class);
        assetManager.load(Constants.FX_FILE_PICK_UP_COLONE, Sound.class);
        assetManager.load(Constants.FX_FILE_PICK_UP_POWERONE, Sound.class);
        assetManager.load(Constants.FX_FILE_PICK_UP_POWERTWO, Sound.class);
        assetManager.load(Constants.FX_FILE_PICK_UP_POWERTHREE, Sound.class);
        assetManager.load(Constants.FX_FILE_PICK_UP_POWERFOUR, Sound.class);
        assetManager.load(Constants.FX_FILE_POWER_DOWN, Sound.class);
        assetManager.load(Constants.FX_FILE_POWER_TIMER, Sound.class);
        assetManager.load(Constants.FX_FILE_SHOW_UP_COLONE, Sound.class);
        assetManager.load(Constants.FX_FILE_SHOW_UP_POWERONE, Sound.class);
        assetManager.load(Constants.FX_FILE_SHOW_UP_POWERTWO, Sound.class);
        assetManager.load(Constants.FX_FILE_SHOW_UP_POWERTHREE, Sound.class);
        assetManager.load(Constants.FX_FILE_SHOW_UP_POWERFOUR, Sound.class);
        assetManager.load(Constants.FX_FILE_FINAL_LEVEL_ONE_POWER_UP, Sound.class);
        assetManager.load(Constants.FX_FILE_FINAL_LEVEL_ONE_POWER_DOWN, Sound.class);
        assetManager.load(Constants.FX_FILE_FINAL_LEVEL_ONE_EXPLOSION, Sound.class);
        assetManager.load(Constants.FX_FILE_FINAL_LEVEL_ONE_HIT, Sound.class);
        assetManager.load(Constants.FX_FILE_LEVEL_COMPLETED, Sound.class);
        assetManager.load(Constants.FX_FILE_BOUNCE, Sound.class);

        // Load music
        assetManager.load(Constants.MUSIC_FILE_MAIN_MENU, Music.class);
        assetManager.load(Constants.MUSIC_FILE_LEVEL_ONE, Music.class);

        // Start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "Number of assets loaded: " + assetManager.getAssetNames().size);
        for (String assetName : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "Asset: " + assetName);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // Enable texture filtering for pixel smoothing
        for (Texture texture : atlas.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        fonts = new AssetFonts();
        hero = new AssetHero(atlas);
        enemyOne = new AssetEnemyOne(atlas);
        enemyTwo = new AssetEnemyTwo(atlas);
        powerBox = new AssetPowerBox(atlas);
        colOne = new AssetColOne(atlas);
        powerOne = new AssetPowerOne(atlas);
        powerTwo = new AssetPowerTwo(atlas);
        powerThree = new AssetPowerThree(atlas);
        powerFour = new AssetPowerFour(atlas);
        explosionA = new AssetExplosionA(atlas);
        explosionB = new AssetExplosionB(atlas);
        explosionC = new AssetExplosionC(atlas);
        explosionD = new AssetExplosionD(atlas);
        explosionE = new AssetExplosionE(atlas);
        heroBullet = new AssetHeroBullet(atlas);
        enemyBullet = new AssetEnemyBullet(atlas);
        ghostMode = new AssetGhostMode(atlas);
        shield = new AssetShield(atlas);
        bulletA = new AssetBulletA(atlas);
        toughMode = new AssetToughMode(atlas);
        finalEnemyLevelOne = new AssetFinalEnemyLevelOne(atlas);
        finalEnemyLevelOnePower = new AssetFinalEnemyLevelOnePower(atlas);
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

    public AssetHeroBullet getHeroBullet() {
        return heroBullet;
    }

    public AssetEnemyBullet getEnemyBullet() {
        return enemyBullet;
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

    public AssetToughMode getToughMode() {
        return toughMode;
    }

    public AssetFinalEnemyLevelOne getFinalEnemyLevelOne() {
        return finalEnemyLevelOne;
    }

    public AssetFinalEnemyLevelOnePower getFinalEnemyLevelOnePower() {
        return finalEnemyLevelOnePower;
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
