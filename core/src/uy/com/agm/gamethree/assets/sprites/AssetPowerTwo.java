package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetPowerTwo {
    private static final String TAG = AssetPowerTwo.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 150.0f * 0.4f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 158.0f * 0.4f / PlayScreen.PPM;

    private TextureRegion powerTwoStand;
    private Animation powerTwoAnimation;

    public AssetPowerTwo(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        powerTwoStand = atlas.findRegion("powerTwo", 1);

        // Animation
        regions = atlas.findRegions("powerTwo");
        powerTwoAnimation = new Animation(1.5f / 31.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getPowerTwoStand() {
        return powerTwoStand;
    }

    public Animation getPowerTwoAnimation() {
        return powerTwoAnimation;
    }
}