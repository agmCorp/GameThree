package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionP {
    private static final String TAG = AssetExplosionP.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 230.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 240.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion explosionPStand;
    private Animation explosionPAnimation;

    public AssetExplosionP(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionPStand = atlas.findRegion("explosionP", 1);

        // Animation
        regions = atlas.findRegions("explosionP");
        explosionPAnimation = new Animation(0.5f / 7.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionPStand() {
        return explosionPStand;
    }

    public Animation getExplosionPAnimation() {
        return explosionPAnimation;
    }
}