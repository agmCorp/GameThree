package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionM {
    private static final String TAG = AssetExplosionM.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 228.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 228.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion explosionMStand;
    private Animation explosionMAnimation;

    public AssetExplosionM(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionMStand = atlas.findRegion("explosionM", 1);

        // Animation
        regions = atlas.findRegions("explosionM");
        explosionMAnimation = new Animation(1.0f / 24.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionMStand() {
        return explosionMStand;
    }

    public Animation getExplosionMAnimation() {
        return explosionMAnimation;
    }
}