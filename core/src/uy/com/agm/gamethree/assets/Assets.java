package uy.com.agm.gamethree.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

import uy.com.agm.gamethree.assets.audio.music.AssetMusic;
import uy.com.agm.gamethree.assets.audio.sound.AssetSounds;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyBullet;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyOne;
import uy.com.agm.gamethree.assets.sprites.AssetEnergyBall;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionA;
import uy.com.agm.gamethree.assets.sprites.AssetExplosionB;
import uy.com.agm.gamethree.assets.sprites.AssetFonts;
import uy.com.agm.gamethree.assets.sprites.AssetGhostMode;
import uy.com.agm.gamethree.assets.sprites.AssetHero;
import uy.com.agm.gamethree.assets.sprites.AssetPowerBox;
import uy.com.agm.gamethree.assets.sprites.AssetPowerOne;
import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by amorales on 14/12/2017.
 */

public class Assets implements Disposable, AssetErrorListener {
    private static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    public AssetFonts fonts;
    public AssetHero hero;
    public AssetEnemyOne enemyOne;
    public AssetPowerBox powerBox;
    public AssetPowerOne powerOne;
    public AssetExplosionA explosionA;
    public AssetExplosionB explosionB;
    public AssetEnergyBall energyBall;
    public AssetEnemyBullet enemyBullet;
    public AssetGhostMode ghostMode;

    public AssetSounds sounds;
    public AssetMusic music;

    // Singleton: prevent instantiation from other classes
    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;

        // Set asset manager error handler
        assetManager.setErrorListener(this);

        // Load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

        // Load sounds
        assetManager.load("audio/sounds/bump.ogg", Sound.class);
        assetManager.load("audio/sounds/crack.ogg", Sound.class);
        assetManager.load("audio/sounds/dead.ogg", Sound.class);
        assetManager.load("audio/sounds/enemyShoot.ogg", Sound.class);
        assetManager.load("audio/sounds/heroShoot.ogg", Sound.class);
        assetManager.load("audio/sounds/hit.ogg", Sound.class);
        assetManager.load("audio/sounds/openPowerBox.ogg", Sound.class);
        assetManager.load("audio/sounds/pickUpPowerOne.ogg", Sound.class);
        assetManager.load("audio/sounds/powerDown.ogg", Sound.class);
        assetManager.load("audio/sounds/powerTimer.ogg", Sound.class);
        assetManager.load("audio/sounds/showUpPowerOne.ogg", Sound.class);

        // Load music
        assetManager.load("audio/music/levelOne.ogg", Music.class);

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

        // Create game resource objects
        fonts = new AssetFonts();
        hero = new AssetHero(atlas);
        enemyOne = new AssetEnemyOne(atlas);
        powerBox = new AssetPowerBox(atlas);
        powerOne = new AssetPowerOne(atlas);
        explosionA = new AssetExplosionA(atlas);
        explosionB = new AssetExplosionB(atlas);
        energyBall = new AssetEnergyBall(atlas);
        enemyBullet = new AssetEnemyBullet(atlas);
        ghostMode = new AssetGhostMode(atlas);
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
        /*
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
        */
    }
}
