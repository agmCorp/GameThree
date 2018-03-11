package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionF {
    private static final String TAG = AssetExplosionF.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 179.0f * 1.5f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 162.0f * 1.5f / PlayScreen.PPM;

    private TextureRegion explosionFStand;
    private Animation explosionFAnimation;

    public AssetExplosionF(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionFStand = atlas.findRegion("explosionF", 1);

        // Animation
        regions = atlas.findRegions("explosionF");
        explosionFAnimation = new Animation(0.5f / 8.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionFStand() {
        return explosionFStand;
    }

    public Animation getExplosionFAnimation() {
        return explosionFAnimation;
    }
}