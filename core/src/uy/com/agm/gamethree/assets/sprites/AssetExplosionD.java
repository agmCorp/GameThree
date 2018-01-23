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

    private TextureRegion explosionDStand;
    private Animation explosionDAnimation;

    public AssetExplosionD(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionDStand = atlas.findRegion("explosionD", 1);

        // Animation
        regions = atlas.findRegions("explosionD");
        explosionDAnimation = new Animation(0.5f / 21.0f, regions);
        regions.clear();
    }

    public TextureRegion getExplosionDStand() {
        return explosionDStand;
    }

    public Animation getExplosionDAnimation() {
        return explosionDAnimation;
    }
}