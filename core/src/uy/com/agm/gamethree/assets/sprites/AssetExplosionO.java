package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionO {
    private static final String TAG = AssetExplosionO.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 230.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 230.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion explosionOStand;
    private Animation explosionOAnimation;

    public AssetExplosionO(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionOStand = atlas.findRegion("explosionO", 1);

        // Animation
        regions = atlas.findRegions("explosionO");
        explosionOAnimation = new Animation(0.5f / 10.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionOStand() {
        return explosionOStand;
    }

    public Animation getExplosionOAnimation() {
        return explosionOAnimation;
    }
}