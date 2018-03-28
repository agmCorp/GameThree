package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionA {
    private static final String TAG = AssetExplosionA.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 167.0f * 0.8f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 167.0f * 0.8f / PlayScreen.PPM;

    private TextureRegion explosionAStand;
    private Animation explosionAAnimation;

    public AssetExplosionA(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionAStand = atlas.findRegion("explosionA", 1);

        // Animation
        regions = atlas.findRegions("explosionA");
        explosionAAnimation = new Animation(0.5f / 25.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionAStand() {
        return explosionAStand;
    }

    public Animation getExplosionAAnimation() {
        return explosionAAnimation;
    }
}