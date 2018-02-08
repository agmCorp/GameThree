package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetShuriken {
    private static final String TAG = AssetShuriken.class.getName();

    private TextureRegion shurikenStand;
    private Animation shurikenAnimation;

    public AssetShuriken(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        shurikenStand = atlas.findRegion("shuriken", 1);

        // Animation
        regions = atlas.findRegions("shuriken");
        shurikenAnimation = new Animation(0.5f / 13.0f, regions);
        regions.clear();
    }

    public TextureRegion getShurikenStand() {
        return shurikenStand;
    }

    public Animation getShurikenAnimation() {
        return shurikenAnimation;
    }
}
