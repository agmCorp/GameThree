package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionH {
    private static final String TAG = AssetExplosionH.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 192.0f * 1.5f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 192.0f * 1.5f / PlayScreen.PPM;

    private TextureRegion explosionHStand;
    private Animation explosionHAnimation;

    public AssetExplosionH(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionHStand = atlas.findRegion("explosionH", 1);

        // Animation
        regions = atlas.findRegions("explosionH");
        explosionHAnimation = new Animation(0.5f / 9.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionHStand() {
        return explosionHStand;
    }

    public Animation getExplosionHAnimation() {
        return explosionHAnimation;
    }
}