package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionN {
    private static final String TAG = AssetExplosionN.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 228.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 190.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion explosionNStand;
    private Animation explosionNAnimation;

    public AssetExplosionN(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionNStand = atlas.findRegion("explosionN", 1);

        // Animation
        regions = atlas.findRegions("explosionN");
        explosionNAnimation = new Animation(1.0f / 37.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionNStand() {
        return explosionNStand;
    }

    public Animation getExplosionNAnimation() {
        return explosionNAnimation;
    }
}