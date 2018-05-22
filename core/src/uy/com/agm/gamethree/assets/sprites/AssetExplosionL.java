package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionL {
    private static final String TAG = AssetExplosionL.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 320.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 320.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion explosionLStand;
    private Animation explosionLAnimation;

    public AssetExplosionL(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionLStand = atlas.findRegion("explosionL", 1);

        // Animation
        regions = atlas.findRegions("explosionL");
        explosionLAnimation = new Animation(0.6f / 21.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionLStand() {
        return explosionLStand;
    }

    public Animation getExplosionLAnimation() {
        return explosionLAnimation;
    }
}