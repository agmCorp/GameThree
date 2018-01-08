package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetShield {
    private static final String TAG = AssetShield.class.getName();

    public final TextureRegion shieldStand;
    public final Animation shieldAnimation;

    public AssetShield(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        shieldStand = atlas.findRegion("shield", 1);

        // Animation
        regions = atlas.findRegions("shield");
        shieldAnimation = new Animation(0.3f / 20.0f, regions);
        regions.clear();
    }
}