package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionC {
    private static final String TAG = AssetExplosionC.class.getName();

    public final TextureRegion explosionCStand;
    public final Animation explosionCAnimation;

    public AssetExplosionC(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionCStand = atlas.findRegion("explosionC", 1);

        // Animation
        regions = atlas.findRegions("explosionC");
        explosionCAnimation = new Animation(1.0f / 24.0f, regions);
        regions.clear();
    }
}