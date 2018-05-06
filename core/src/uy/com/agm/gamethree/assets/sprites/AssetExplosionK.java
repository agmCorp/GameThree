package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionK {
    private static final String TAG = AssetExplosionK.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 314.0f * 1.3f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 255.0f * 1.3f / PlayScreen.PPM;

    private TextureRegion explosionKStand;
    private Animation explosionKAnimation;

    public AssetExplosionK(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionKStand = atlas.findRegion("explosionK", 1);

        // Animation
        regions = atlas.findRegions("explosionK");
        explosionKAnimation = new Animation(0.5f / 18.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionKStand() {
        return explosionKStand;
    }

    public Animation getExplosionKAnimation() {
        return explosionKAnimation;
    }
}