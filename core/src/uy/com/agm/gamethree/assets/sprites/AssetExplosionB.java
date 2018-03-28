package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionB {
    private static final String TAG = AssetExplosionB.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 98.0f * 1.3f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 125.0f * 1.3f / PlayScreen.PPM;

    private TextureRegion explosionBStand;
    private Animation explosionBAnimation;

    public AssetExplosionB(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionBStand = atlas.findRegion("explosionB", 1);

        // Animation
        regions = atlas.findRegions("explosionB");
        explosionBAnimation = new Animation(0.5f / 7.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionBStand() {
        return explosionBStand;
    }

    public Animation getExplosionBAnimation() {
        return explosionBAnimation;
    }
}