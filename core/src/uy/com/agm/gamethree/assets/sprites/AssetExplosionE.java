package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionE {
    private static final String TAG = AssetExplosionE.class.getName();

    public final TextureRegion explosionEStand;
    public final Animation explosionEAnimation;

    public AssetExplosionE(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionEStand = atlas.findRegion("explosionE", 1);

        // Animation
        regions = atlas.findRegions("explosionE");
        explosionEAnimation = new Animation(0.5f / 21.0f, regions);
        regions.clear();
    }
}