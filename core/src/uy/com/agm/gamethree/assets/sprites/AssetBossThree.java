package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/30/2017.
 */
public class AssetBossThree {
    private static final String TAG = AssetBossThree.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 194.0f * 0.9f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 176.0f * 0.9f / PlayScreen.PPM;
    public static final float POWER_WIDTH_METERS = 192.0f * 1.6f / PlayScreen.PPM;
    public static final float POWER_HEIGHT_METERS = 192.0f * 1.6f / PlayScreen.PPM;

    private TextureRegion bossThreeStand;
    private TextureRegion bossThreePowerStand;
    private Animation bossThreeIdleAnimation;
    private Animation bossThreeWalkAnimation;
    private Animation bossThreeShootAnimation;
    private Animation bossThreeDeathAnimation;
    private Animation bossThreePowerAnimation;

    public AssetBossThree(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bossThreeStand = atlas.findRegion("bossThreeWalk", 1);
        bossThreePowerStand = atlas.findRegion("bossOnePower", 1);

        // Animation
        regions = atlas.findRegions("bossThreeWalk");
        bossThreeIdleAnimation = new Animation(1.0f / 50.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossThreeWalk");
        bossThreeWalkAnimation = new Animation(1.0f / 50.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossThreeWalk");
        bossThreeShootAnimation = new Animation(1.0f / 50.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossThreeDeath");
        bossThreeDeathAnimation = new Animation(1.0f / 50.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossThreePower");
        bossThreePowerAnimation = new Animation(0.5f / 20.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBossThreeStand() {
        return bossThreeStand;
    }

    public TextureRegion getBossThreePowerStand() {
        return bossThreePowerStand;
    }

    public Animation getBossThreeIdleAnimation() {
        return bossThreeIdleAnimation;
    }

    public Animation getBossThreeWalkAnimation() {
        return bossThreeWalkAnimation;
    }

    public Animation getBossThreeShootAnimation() {
        return bossThreeShootAnimation;
    }

    public Animation getBossThreeDeathAnimation() {
        return bossThreeDeathAnimation;
    }

    public Animation getBossThreePowerAnimation() {
        return bossThreePowerAnimation;
    }
}
