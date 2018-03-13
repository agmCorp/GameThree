package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionG {
    private static final String TAG = AssetExplosionG.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 110.0f * 2.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 110.0f * 2.0f / PlayScreen.PPM;

    private TextureRegion explosionGStand;
    private Animation explosionGAnimation;

    public AssetExplosionG(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionGStand = atlas.findRegion("explosionG", 1);

        // Animation
        regions = atlas.findRegions("explosionG");
        explosionGAnimation = new Animation(0.5f / 12.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionGStand() {
        return explosionGStand;
    }

    public Animation getExplosionGAnimation() {
        return explosionGAnimation;
    }
}