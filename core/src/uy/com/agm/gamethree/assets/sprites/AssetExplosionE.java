package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionE {
    private static final String TAG = AssetExplosionE.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 196.0f * 3.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 178.0f * 3.0f / PlayScreen.PPM;
    
    private TextureRegion explosionEStand;
    private Animation explosionEAnimation;

    public AssetExplosionE(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionEStand = atlas.findRegion("explosionE", 1);

        // Animation
        regions = atlas.findRegions("explosionE");
        explosionEAnimation = new Animation(0.5f / 21.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionEStand() {
        return explosionEStand;
    }

    public Animation getExplosionEAnimation() {
        return explosionEAnimation;
    }
}