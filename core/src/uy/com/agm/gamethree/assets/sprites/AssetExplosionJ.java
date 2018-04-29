package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionJ {
    private static final String TAG = AssetExplosionJ.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 256.0f * 1.5f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 256.0f * 1.5f / PlayScreen.PPM;

    private TextureRegion explosionJStand;
    private Animation explosionJAnimation;

    public AssetExplosionJ(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionJStand = atlas.findRegion("explosionJ", 1);

        // Animation
        regions = atlas.findRegions("explosionJ");
        explosionJAnimation = new Animation(0.5f / 14.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionJStand() {
        return explosionJStand;
    }

    public Animation getExplosionJAnimation() {
        return explosionJAnimation;
    }
}