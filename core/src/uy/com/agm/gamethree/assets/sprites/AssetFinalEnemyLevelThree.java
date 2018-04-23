package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/30/2017.
 */
public class AssetFinalEnemyLevelThree {
    private static final String TAG = AssetFinalEnemyLevelThree.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 194.0f * 0.9f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 176.0f * 0.9f / PlayScreen.PPM;
    public static final float POWER_WIDTH_METERS = 192.0f * 1.6f / PlayScreen.PPM;
    public static final float POWER_HEIGHT_METERS = 192.0f * 1.6f / PlayScreen.PPM;

    private TextureRegion finalEnemyLevelThreeStand;
    private TextureRegion finalEnemyLevelThreePowerStand;
    private Animation finalEnemyLevelThreeIdleAnimation;
    private Animation finalEnemyLevelThreeWalkAnimation;
    private Animation finalEnemyLevelThreeShootAnimation;
    private Animation finalEnemyLevelThreeDeathAnimation;
    private Animation finalEnemyLevelThreePowerAnimation;

    public AssetFinalEnemyLevelThree(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        finalEnemyLevelThreeStand = atlas.findRegion("finalEnemyLevelThreeWalk", 1);
        finalEnemyLevelThreePowerStand = atlas.findRegion("finalEnemyLevelOnePower", 1);

        // Animation
        regions = atlas.findRegions("finalEnemyLevelThreeWalk");
        finalEnemyLevelThreeIdleAnimation = new Animation(1.0f / 50.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelThreeWalk");
        finalEnemyLevelThreeWalkAnimation = new Animation(1.0f / 50.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelThreeWalk");
        finalEnemyLevelThreeShootAnimation = new Animation(1.0f / 50.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelThreeDead");
        finalEnemyLevelThreeDeathAnimation = new Animation(1.0f / 50.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelThreePower");
        finalEnemyLevelThreePowerAnimation = new Animation(0.5f / 20.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getFinalEnemyLevelThreeStand() {
        return finalEnemyLevelThreeStand;
    }

    public TextureRegion getFinalEnemyLevelThreePowerStand() {
        return finalEnemyLevelThreePowerStand;
    }

    public Animation getFinalEnemyLevelThreeIdleAnimation() {
        return finalEnemyLevelThreeIdleAnimation;
    }

    public Animation getFinalEnemyLevelThreeWalkAnimation() {
        return finalEnemyLevelThreeWalkAnimation;
    }

    public Animation getFinalEnemyLevelThreeShootAnimation() {
        return finalEnemyLevelThreeShootAnimation;
    }

    public Animation getFinalEnemyLevelThreeDeathAnimation() {
        return finalEnemyLevelThreeDeathAnimation;
    }

    public Animation getFinalEnemyLevelThreePowerAnimation() {
        return finalEnemyLevelThreePowerAnimation;
    }
}
