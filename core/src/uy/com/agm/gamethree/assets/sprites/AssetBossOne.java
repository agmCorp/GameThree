package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/30/2017.
 */

public class AssetBossOne {
    private static final String TAG = AssetBossOne.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 177.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 169.0f * 1.0f / PlayScreen.PPM;
    public static final float POWER_WIDTH_METERS = 192.0f * 1.6f / PlayScreen.PPM;
    public static final float POWER_HEIGHT_METERS = 192.0f * 1.6f / PlayScreen.PPM;

    private TextureRegion bossOneStand;
    private TextureRegion bossOnePowerStand;
    private Animation bossOneIdleAnimation;
    private Animation bossOneWalkAnimation;
    private Animation bossOneShootAnimation;
    private Animation bossOneDeathAnimation;
    private Animation bossOnePowerAnimation;

    public AssetBossOne(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bossOneStand = atlas.findRegion("bossOneIdle", 1);
        bossOnePowerStand = atlas.findRegion("bossOnePower", 1);

        // Animation
        regions = atlas.findRegions("bossOneIdle");
        bossOneIdleAnimation = new Animation(0.4f / 9.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossOneWalk");
        bossOneWalkAnimation = new Animation(0.7f / 17.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossOneShoot");
        bossOneShootAnimation = new Animation(0.5f / 13.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossOneDeath");
        bossOneDeathAnimation = new Animation(1.4f / 34.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossOnePower");
        bossOnePowerAnimation = new Animation(1.0f / 30.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBossOneStand() {
        return bossOneStand;
    }

    public TextureRegion getBossOnePowerStand() {
        return bossOnePowerStand;
    }

    public Animation getBossOneIdleAnimation() {
        return bossOneIdleAnimation;
    }

    public Animation getBossOneWalkAnimation() {
        return bossOneWalkAnimation;
    }

    public Animation getBossOneShootAnimation() {
        return bossOneShootAnimation;
    }

    public Animation getBossOneDeathAnimation() {
        return bossOneDeathAnimation;
    }

    public Animation getBossOnePowerAnimation() {
        return bossOnePowerAnimation;
    }
}
