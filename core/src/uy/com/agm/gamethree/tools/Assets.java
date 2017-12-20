package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

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
    public AssetEnergyBall energyBall;
    public AssetEnemyBullet enemyBullet;

    public AssetSounds sounds;
    public AssetMusic music;

    // Singleton: prevent instantiation from other classes
    private Assets() {
    }

    public class AssetFonts {
        /*
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;
        */

        public AssetFonts() {
            /*
            // create three fonts using Libgdx's built-in 15px bitmap font
            defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            // set font sizes
            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);
            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            */
        }
    }

    public class AssetPowerBox {
        public final TextureRegion powerBoxStand;
        public final TextureRegion powerBoxDamagedLittle;
        public final TextureRegion powerBoxDamagedMedium;
        public final TextureRegion powerBoxDamagedHard;

        public AssetPowerBox(TextureAtlas atlas) {
            powerBoxStand = atlas.findRegion("powerBox", 1);
            powerBoxDamagedLittle = atlas.findRegion("powerBox", 2);
            powerBoxDamagedMedium = atlas.findRegion("powerBox", 3);
            powerBoxDamagedHard = atlas.findRegion("powerBox", 4);
        }
    }

    public class AssetHero {
        public final TextureRegion heroStand;
        public final Animation heroMovingUp;
        public final Animation heroMovingDown;

        public AssetHero(TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> regions;

            heroStand = atlas.findRegion("heroUp", 1);

            // Animation
            regions = atlas.findRegions("heroUp");
            heroMovingUp = new Animation(0.5f / 6.0f, regions);
            regions.clear();

            // Animation
            regions = atlas.findRegions("heroDown");
            heroMovingDown = new Animation(0.5f / 6.0f, regions);
        }
    }

    public class AssetEnemyOne {
        public final TextureRegion enemyOneStand;
        public final Animation enemyOneAnimation;
        public final Animation explosionAnimation;

        public AssetEnemyOne(TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> regions;

            enemyOneStand = atlas.findRegion("enemyOne", 1);

            // Animation
            regions = atlas.findRegions("enemyOne");
            enemyOneAnimation = new Animation(0.5f / 8.0f, regions);
            regions.clear();

            // Animation
            regions = atlas.findRegions("explosion");
            explosionAnimation = new Animation(0.5f / 25.0f, regions);
        }
    }

    public class AssetPowerOne {
        public final TextureRegion powerOneStand;
        public final Animation powerOneAnimation;

        public AssetPowerOne(TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> regions;

            powerOneStand = atlas.findRegion("powerOne", 1);

            // Animation
            regions = atlas.findRegions("powerOne");
            powerOneAnimation = new Animation(1.0f / 16.0f, regions);
            regions.clear();
        }
    }

    public class AssetEnergyBall {
        public final TextureRegion energyBallStand;
        public final Animation energyBallAnimation;

        public AssetEnergyBall(TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> regions;

            energyBallStand = atlas.findRegion("energyBall", 1);

            // Animation
            regions = atlas.findRegions("energyBall");
            energyBallAnimation = new Animation(0.5f / 4.0f, regions);
            regions.clear();
        }
    }

    public class AssetEnemyBullet {
        public final TextureRegion enemyBulletStand;
        public final Animation enemyBulletAnimation;

        public AssetEnemyBullet(TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> regions;

            enemyBulletStand = atlas.findRegion("enemyBullet", 1);

            // Animation
            regions = atlas.findRegions("enemyBullet");
            enemyBulletAnimation = new Animation(0.2f / 4.0f, regions);
            regions.clear();
        }
    }

    public class AssetSounds {
        public final Sound dead;
        public final Sound hit;
        public final Sound pickUpPowerOne;
        public final Sound openPowerBox;

        public AssetSounds(AssetManager am) {
            dead = am.get("audio/sounds/dead.ogg", Sound.class);
            hit = am.get("audio/sounds/hit.ogg", Sound.class);
            pickUpPowerOne = am.get("audio/sounds/pickUpPowerOne.ogg", Sound.class);
            openPowerBox = am.get("audio/sounds/openPowerBox.ogg", Sound.class);
        }
    }

    public class AssetMusic {
        public final Music songLevelOne;

        public AssetMusic(AssetManager am) {
            songLevelOne = am.get("audio/music/levelOne.ogg", Music.class);
        }
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;

        // Set asset manager error handler
        assetManager.setErrorListener(this);

        // Load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

        // Load sounds
        assetManager.load("audio/sounds/dead.ogg", Sound.class);
        assetManager.load("audio/sounds/hit.ogg", Sound.class);
        assetManager.load("audio/sounds/pickUpPowerOne.ogg", Sound.class);
        assetManager.load("audio/sounds/openPowerBox.ogg", Sound.class);

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
        energyBall = new AssetEnergyBall(atlas);
        enemyBullet = new AssetEnemyBullet(atlas);
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
