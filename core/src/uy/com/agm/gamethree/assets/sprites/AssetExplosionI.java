package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionI {
    private static final String TAG = AssetExplosionI.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 279.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 298.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion explosionIStand;
    private Animation explosionIAnimation;

    public AssetExplosionI(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionIStand = atlas.findRegion("explosionI", 1);

        // Animation
        regions = atlas.findRegions("explosionI");
        explosionIAnimation = new Animation(0.5f / 11.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionIStand() {
        return explosionIStand;
    }

    public Animation getExplosionIAnimation() {
        return explosionIAnimation;
    }
}