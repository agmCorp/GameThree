package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetGhostMode {
    private static final String TAG = AssetGhostMode.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 192.0f * 1.2f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 192.0f * 1.2f / PlayScreen.PPM;

    private TextureRegion ghostModeStand;
    private Animation ghostModeAnimation;

    public AssetGhostMode(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        ghostModeStand = atlas.findRegion("ghostMode", 1);

        // Animation
        regions = atlas.findRegions("ghostMode");
        ghostModeAnimation = new Animation(0.5f / 24.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getGhostModeStand() {
        return ghostModeStand;
    }

    public Animation getGhostModeAnimation() {
        return ghostModeAnimation;
    }
}