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

        public AssetPowerBox(TextureAtlas atlas) {
            powerBoxStand = atlas.findRegion("powerBox");
        }
    }

    public class AssetHero {
        public final TextureRegion heroStand;
        public final Animation heroMovingUp;
        public final Animation heroMovingDown;

        public AssetHero(TextureAtlas atlas) {
            heroStand = atlas.findRegion("heroUp", 1);

            Array<TextureAtlas.AtlasRegion> regions = null;
            TextureAtlas.AtlasRegion region = null;

            // Animation: HeroMovingUp
            regions = atlas.findRegions("heroUp");
            heroMovingUp = new Animation(1.0f / 6.0f, regions);
            regions.clear();

            // Animation: HeroMovingDown
            regions = atlas.findRegions("heroDown");
            heroMovingDown = new Animation(1.0f / 6.0f, regions);
        }
    }

    public class AssetEnemyOne {
        public final TextureRegion enemyOneStand;
        public final Animation enemyOneAnimation;
        public final Animation explosionAnimation;

        public AssetEnemyOne(TextureAtlas atlas) {
            enemyOneStand = atlas.findRegion("enemyOne", 1);

            Array<TextureAtlas.AtlasRegion> regions = null;
            TextureAtlas.AtlasRegion region = null;

            regions = atlas.findRegions("enemyOne");
            enemyOneAnimation = new Animation(1.0f / 8.0f, regions);
            regions.clear();

            regions = atlas.findRegions("explosion");
            explosionAnimation = new Animation(1.0f / 25.0f, regions);
        }
    }

    public class AssetPowerOne {
        public final TextureRegion powerOneStand;
        public final Animation powerOneAnimation;

        public AssetPowerOne(TextureAtlas atlas) {
            powerOneStand = atlas.findRegion("powerOne", 1);

            Array<TextureAtlas.AtlasRegion> regions = null;
            TextureAtlas.AtlasRegion region = null;

            // TODO PARAMETRIZAR LAS DURACIONES DE LAS ANIMACIONES
            regions = atlas.findRegions("powerOne");
            powerOneAnimation = new Animation(1.0f / 16.0f, regions);
            regions.clear();
        }
    }

    public class AssetSounds {
        public final Sound hit;
        public final Sound pickUpPowerOne;
        public final Sound openPowerBox;

        public AssetSounds(AssetManager am) {
            hit = am.get("audio/sounds/hit.ogg", Sound.class);
            pickUpPowerOne = am.get("audio/sounds/pickUpPowerOne.ogg", Sound.class);
            openPowerBox = am.get("audio/sounds/openPowerBox.ogg", Sound.class);
        }
    }

    public class AssetMusic {
        public final Music songLevelOne;

        public AssetMusic(AssetManager am) {
            songLevelOne = am.get("audio/music/levelOne.mp3", Music.class);
        }
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // load sounds
        assetManager.load("audio/sounds/hit.ogg", Sound.class);
        assetManager.load("audio/sounds/pickUpPowerOne.ogg", Sound.class);
        assetManager.load("audio/sounds/openPowerBox.ogg", Sound.class);
        // load music
        assetManager.load("audio/music/levelOne.mp3", Music.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "Number of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        // create game resource objects
        fonts = new AssetFonts();
        hero = new AssetHero(atlas);
        enemyOne = new AssetEnemyOne(atlas);
        powerBox = new AssetPowerBox(atlas);
        powerOne = new AssetPowerOne(atlas);
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }

    @Override
    public void error(AssetDescriptor assetDescriptor, Throwable throwable) {
        Gdx.app.error(TAG, "Error al cargar asset: '" + assetDescriptor.fileName + "'", (Exception) throwable);
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
