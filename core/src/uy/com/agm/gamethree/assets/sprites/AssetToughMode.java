package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetToughMode {
    private static final String TAG = AssetToughMode.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 192.0f * 1.2f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 192.0f * 1.2f / PlayScreen.PPM;

    private TextureRegion toughModeStand;
    private Animation toughModeAnimation;

    public AssetToughMode(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        toughModeStand = atlas.findRegion("toughMode", 1);

        // Animation
        regions = atlas.findRegions("toughMode");
        toughModeAnimation = new Animation(0.5f / 20.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getToughModeStand() {
        return toughModeStand;
    }

    public Animation getToughModeAnimation() {
        return toughModeAnimation;
    }
}