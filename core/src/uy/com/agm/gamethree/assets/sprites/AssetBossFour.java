package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/30/2017.
 */

public class AssetBossFour {
    private static final String TAG = AssetBossFour.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 286.0f * 1.1f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 260.0f * 1.1f / PlayScreen.PPM;
    public static final float POWER_WIDTH_METERS = 260.0f * 1.2f / PlayScreen.PPM;
    public static final float POWER_HEIGHT_METERS = 260.0f * 1.2f / PlayScreen.PPM;

    private TextureRegion bossFourStand;
    private TextureRegion bossFourPowerStand;
    private Animation bossFourIdleAnimation;
    private Animation bossFourWalkAnimation;
    private Animation bossFourShootAnimation;
    private Animation bossFourDeathAnimation;
    private Animation bossFourPowerAnimation;

    public AssetBossFour(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bossFourStand = atlas.findRegion("bossFourIdle", 1);
        bossFourPowerStand = atlas.findRegion("bossFourPower", 1);

        // Animation
        regions = atlas.findRegions("bossFourIdle");
        bossFourIdleAnimation = new Animation(1.5f / 15.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossFourWalk");
        bossFourWalkAnimation = new Animation(1.5f / 15.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossFourShoot");
        bossFourShootAnimation = new Animation(1.5f / 14.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossFourDeath");
        bossFourDeathAnimation = new Animation(3.0f / 22.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bossFourPower");
        bossFourPowerAnimation = new Animation(1.5f / 20.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBossFourStand() {
        return bossFourStand;
    }

    public TextureRegion getBossFourPowerStand() {
        return bossFourPowerStand;
    }

    public Animation getBossFourIdleAnimation() {
        return bossFourIdleAnimation;
    }

    public Animation getBossFourWalkAnimation() {
        return bossFourWalkAnimation;
    }

    public Animation getBossFourShootAnimation() {
        return bossFourShootAnimation;
    }

    public Animation getBossFourDeathAnimation() {
        return bossFourDeathAnimation;
    }

    public Animation getBossFourPowerAnimation() {
        return bossFourPowerAnimation;
    }
}
