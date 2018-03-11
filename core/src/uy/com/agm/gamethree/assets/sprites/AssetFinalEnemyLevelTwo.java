package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/30/2017.
 */
// todo revisar todos los assets y sus tiempos
public class AssetFinalEnemyLevelTwo {
    private static final String TAG = AssetFinalEnemyLevelTwo.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 192.0f * 0.9f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 192.0f * 0.9f / PlayScreen.PPM;
    public static final float POWER_WIDTH_METERS = 192.0f * 1.6f / PlayScreen.PPM;
    public static final float POWER_HEIGHT_METERS = 192.0f * 1.6f / PlayScreen.PPM;

    private TextureRegion finalEnemyLevelTwoStand;
    private TextureRegion finalEnemyLevelTwoPowerStand;
    private Animation finalEnemyLevelTwoIdleAnimation;
    private Animation finalEnemyLevelTwoMovingUpAnimation;
    private Animation finalEnemyLevelTwoMovingDownAnimation;
    private Animation finalEnemyLevelTwoMovingLeftRightAnimation;
    private Animation finalEnemyLevelTwoShootingUpAnimation;
    private Animation finalEnemyLevelTwoShootingDownAnimation;
    private Animation finalEnemyLevelTwoShootingLeftRightAnimation;
    private Animation finalEnemyLevelTwoDeathAnimation;
    private Animation finalEnemyLevelTwoPowerAnimation;

    public AssetFinalEnemyLevelTwo(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        finalEnemyLevelTwoStand = atlas.findRegion("finalEnemyLevelTwoIdle", 1);
        finalEnemyLevelTwoPowerStand = atlas.findRegion("finalEnemyLevelOnePower", 1);

        // Animation
        regions = atlas.findRegions("finalEnemyLevelTwoIdle");
        finalEnemyLevelTwoIdleAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelTwoUp");
        finalEnemyLevelTwoMovingUpAnimation = new Animation(0.5f / 18.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelTwoDown");
        finalEnemyLevelTwoMovingDownAnimation = new Animation(0.5f / 18.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelTwoUp");
        finalEnemyLevelTwoMovingLeftRightAnimation = new Animation(0.5f / 18.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelTwoSlashingUp");
        finalEnemyLevelTwoShootingUpAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelTwoSlashingDown");
        finalEnemyLevelTwoShootingDownAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelTwoSlashingDown");
        finalEnemyLevelTwoShootingLeftRightAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelTwoDead");
        finalEnemyLevelTwoDeathAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelTwoPower");
        finalEnemyLevelTwoPowerAnimation = new Animation(0.5f / 24.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getFinalEnemyLevelTwoStand() {
        return finalEnemyLevelTwoStand;
    }

    public TextureRegion getFinalEnemyLevelTwoPowerStand() {
        return finalEnemyLevelTwoPowerStand;
    }

    public Animation getFinalEnemyLevelTwoIdleAnimation() {
        return finalEnemyLevelTwoIdleAnimation;
    }

    public Animation getFinalEnemyLevelTwoMovingUpAnimation() {
        return finalEnemyLevelTwoMovingUpAnimation;
    }

    public Animation getFinalEnemyLevelTwoMovingDownAnimation() {
        return finalEnemyLevelTwoMovingDownAnimation;
    }

    public Animation getFinalEnemyLevelTwoMovingLeftRightAnimation() {
        return finalEnemyLevelTwoMovingLeftRightAnimation;
    }

    public Animation getFinalEnemyLevelTwoShootingUpAnimation() {
        return finalEnemyLevelTwoShootingUpAnimation;
    }

    public Animation getFinalEnemyLevelTwoShootingDownAnimation() {
        return finalEnemyLevelTwoShootingDownAnimation;
    }

    public Animation getFinalEnemyLevelTwoShootingLeftRightAnimation() {
        return finalEnemyLevelTwoShootingLeftRightAnimation;
    }

    public Animation getFinalEnemyLevelTwoDeathAnimation() {
        return finalEnemyLevelTwoDeathAnimation;
    }

    public Animation getFinalEnemyLevelTwoPowerAnimation() {
        return finalEnemyLevelTwoPowerAnimation;
    }
}
