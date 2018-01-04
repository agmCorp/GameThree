package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionD {
    private static final String TAG = AssetExplosionD.class.getName();

    public final TextureRegion explosionDStand;
    public final Animation explosionDAnimation;

    public AssetExplosionD(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionDStand = atlas.findRegion("explosionD", 1);

        // Animation
        regions = atlas.findRegions("explosionD");
        explosionDAnimation = new Animation(0.5f / 21.0f, regions);
        regions.clear();
    }
}