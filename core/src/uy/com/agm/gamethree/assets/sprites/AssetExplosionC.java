package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionC {
    private static final String TAG = AssetExplosionC.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 234.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 189.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion explosionCStand;
    private Animation explosionCAnimation;

    public AssetExplosionC(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionCStand = atlas.findRegion("explosionC", 1);

        // Animation
        regions = atlas.findRegions("explosionC");
        explosionCAnimation = new Animation(1.0f / 24.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionCStand() {
        return explosionCStand;
    }

    public Animation getExplosionCAnimation() {
        return explosionCAnimation;
    }
}