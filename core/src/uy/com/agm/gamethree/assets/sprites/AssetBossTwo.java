package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/30/2017.
 */
public class AssetBossTwo {
    private static final String TAG = AssetBossTwo.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 192.0f * 0.9f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 192.0f * 0.9f / PlayScreen.PPM;
    public static final float POWER_WIDTH_METERS = 192.0f * 1.6f / PlayScreen.PPM;
    public static final float POWER_HEIGHT_METERS = 192.0f * 1.6f / PlayScreen.PPM;

    private TextureRegion bossTwoStand;
    private TextureRegion bossTwoPowerStand;
    private Animation bossTwoIdleAnimation;
    private Animation bossTwoWalkUpAnimation;
    private Animation bossTwoWalkDownAnimation;
    private Animation bossTwoWalkLeftRightAnimation;
    private Animation bossTwoShootUpAnimation;
    private Animation bossTwoShootDownAnimation;
    private Animation bossTwoShootLeftRightAnimation;
    private Animation bossTwoDeathAnimation;
    private Animation bossTwoPowerAnimation;

    public AssetBossTwo(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bossTwoStand = atlas.findRegion("bossTwoIdle", 1);
        bossTwoPowerStand = atlas.findRegion("bossOnePower", 1);

        // Animation
        regions = atlas.findRegions("bossTwoIdle");
        bossTwoIdleAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoUp");
        bossTwoWalkUpAnimation = new Animation(0.5f / 18.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoDown");
        bossTwoWalkDownAnimation = new Animation(0.5f / 18.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoUp");
        bossTwoWalkLeftRightAnimation = new Animation(0.5f / 18.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoSlashingUp");
        bossTwoShootUpAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoSlashingDown");
        bossTwoShootDownAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoSlashingDown");
        bossTwoShootLeftRightAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoDeath");
        bossTwoDeathAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoPower");
        bossTwoPowerAnimation = new Animation(0.5f / 24.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBossTwoStand() {
        return bossTwoStand;
    }

    public TextureRegion getBossTwoPowerStand() {
        return bossTwoPowerStand;
    }

    public Animation getBossTwoIdleAnimation() {
        return bossTwoIdleAnimation;
    }

    public Animation getBossTwoWalkUpAnimation() {
        return bossTwoWalkUpAnimation;
    }

    public Animation getBossTwoWalkDownAnimation() {
        return bossTwoWalkDownAnimation;
    }

    public Animation getBossTwoWalkLeftRightAnimation() {
        return bossTwoWalkLeftRightAnimation;
    }

    public Animation getBossTwoShootUpAnimation() {
        return bossTwoShootUpAnimation;
    }

    public Animation getBossTwoShootDownAnimation() {
        return bossTwoShootDownAnimation;
    }

    public Animation getBossTwoShootLeftRightAnimation() {
        return bossTwoShootLeftRightAnimation;
    }

    public Animation getBossTwoDeathAnimation() {
        return bossTwoDeathAnimation;
    }

    public Animation getBossTwoPowerAnimation() {
        return bossTwoPowerAnimation;
    }
}
