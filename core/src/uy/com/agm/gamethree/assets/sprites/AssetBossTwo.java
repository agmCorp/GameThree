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
    private Animation bossTwoMovingUpAnimation;
    private Animation bossTwoMovingDownAnimation;
    private Animation bossTwoMovingLeftRightAnimation;
    private Animation bossTwoShootingUpAnimation;
    private Animation bossTwoShootingDownAnimation;
    private Animation bossTwoShootingLeftRightAnimation;
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
        bossTwoMovingUpAnimation = new Animation(0.5f / 18.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoDown");
        bossTwoMovingDownAnimation = new Animation(0.5f / 18.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoUp");
        bossTwoMovingLeftRightAnimation = new Animation(0.5f / 18.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoSlashingUp");
        bossTwoShootingUpAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoSlashingDown");
        bossTwoShootingDownAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossTwoSlashingDown");
        bossTwoShootingLeftRightAnimation = new Animation(0.5f / 12.0f, regions, Animation.PlayMode.LOOP);
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

    public Animation getBossTwoMovingUpAnimation() {
        return bossTwoMovingUpAnimation;
    }

    public Animation getBossTwoMovingDownAnimation() {
        return bossTwoMovingDownAnimation;
    }

    public Animation getBossTwoMovingLeftRightAnimation() {
        return bossTwoMovingLeftRightAnimation;
    }

    public Animation getBossTwoShootingUpAnimation() {
        return bossTwoShootingUpAnimation;
    }

    public Animation getBossTwoShootingDownAnimation() {
        return bossTwoShootingDownAnimation;
    }

    public Animation getBossTwoShootingLeftRightAnimation() {
        return bossTwoShootingLeftRightAnimation;
    }

    public Animation getBossTwoDeathAnimation() {
        return bossTwoDeathAnimation;
    }

    public Animation getBossTwoPowerAnimation() {
        return bossTwoPowerAnimation;
    }
}
