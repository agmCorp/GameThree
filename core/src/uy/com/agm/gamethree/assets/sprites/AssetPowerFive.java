package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetPowerFive {
    private static final String TAG = AssetPowerFive.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 120.0f * 0.7f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 120.0f * 0.7f / PlayScreen.PPM;

    private TextureRegion powerFiveStand;
    private Animation powerFiveAnimation;

    public AssetPowerFive(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        powerFiveStand = atlas.findRegion("powerFive", 1);

        // Animation
        regions = atlas.findRegions("powerFive");
        powerFiveAnimation = new Animation(1.0f / 26.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getPowerFiveStand() {
        return powerFiveStand;
    }

    public Animation getPowerFiveAnimation() {
        return powerFiveAnimation;
    }
}