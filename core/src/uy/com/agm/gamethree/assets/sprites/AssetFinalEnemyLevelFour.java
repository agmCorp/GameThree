package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/30/2017.
 */

public class AssetFinalEnemyLevelFour {
    private static final String TAG = AssetFinalEnemyLevelFour.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 286.0f * 1.1f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 260.0f * 1.1f / PlayScreen.PPM;
    public static final float POWER_WIDTH_METERS = 260.0f * 1.2f / PlayScreen.PPM;
    public static final float POWER_HEIGHT_METERS = 260.0f * 1.2f / PlayScreen.PPM;

    private TextureRegion finalEnemyLevelFourStand;
    private TextureRegion finalEnemyLevelFourPowerStand;
    private Animation finalEnemyLevelFourIdleAnimation;
    private Animation finalEnemyLevelFourWalkAnimation;
    private Animation finalEnemyLevelFourShootAnimation;
    private Animation finalEnemyLevelFourDeathAnimation;
    private Animation finalEnemyLevelFourPowerAnimation;

    public AssetFinalEnemyLevelFour(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        finalEnemyLevelFourStand = atlas.findRegion("finalEnemyLevelFourIdle", 1);
        finalEnemyLevelFourPowerStand = atlas.findRegion("finalEnemyLevelFourPower", 1);

        // Animation
        regions = atlas.findRegions("finalEnemyLevelFourIdle");
        finalEnemyLevelFourIdleAnimation = new Animation(1.5f / 15.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelFourWalk");
        finalEnemyLevelFourWalkAnimation = new Animation(1.5f / 15.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelFourShoot");
        finalEnemyLevelFourShootAnimation = new Animation(1.5f / 14.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelFourDeath");
        finalEnemyLevelFourDeathAnimation = new Animation(1.8f / 19.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelFourPower");
        finalEnemyLevelFourPowerAnimation = new Animation(1.5f / 20.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getFinalEnemyLevelFourStand() {
        return finalEnemyLevelFourStand;
    }

    public TextureRegion getFinalEnemyLevelFourPowerStand() {
        return finalEnemyLevelFourPowerStand;
    }

    public Animation getFinalEnemyLevelFourIdleAnimation() {
        return finalEnemyLevelFourIdleAnimation;
    }

    public Animation getFinalEnemyLevelFourWalkAnimation() {
        return finalEnemyLevelFourWalkAnimation;
    }

    public Animation getFinalEnemyLevelFourShootAnimation() {
        return finalEnemyLevelFourShootAnimation;
    }

    public Animation getFinalEnemyLevelFourDeathAnimation() {
        return finalEnemyLevelFourDeathAnimation;
    }

    public Animation getFinalEnemyLevelFourPowerAnimation() {
        return finalEnemyLevelFourPowerAnimation;
    }
}
