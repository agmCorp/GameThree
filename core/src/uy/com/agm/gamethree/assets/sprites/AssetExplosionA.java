package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetExplosionA {
    private static final String TAG = AssetExplosionA.class.getName();

    public final TextureRegion explosionAStand;
    public final Animation explosionAAnimation;

    public AssetExplosionA(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionAStand = atlas.findRegion("explosionA", 1);

        // Animation
        regions = atlas.findRegions("explosionA");
        explosionAAnimation = new Animation(0.5f / 25.0f, regions);
    }
}