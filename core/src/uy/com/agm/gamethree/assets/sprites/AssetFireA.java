package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetFireA {
    private static final String TAG = AssetFireA.class.getName();

    public final TextureRegion fireAStand;
    public final Animation fireAAnimation;

    public AssetFireA(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        fireAStand = atlas.findRegion("fireA", 1);

        // Animation
        regions = atlas.findRegions("fireA");
        fireAAnimation = new Animation(0.5f / 4.0f, regions);
        regions.clear();
    }
}