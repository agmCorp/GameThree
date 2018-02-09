package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetSilverBullet {
    private static final String TAG = AssetSilverBullet.class.getName();

    private TextureRegion silverBulletStand;
    private Animation silverBulletAnimation;

    public AssetSilverBullet(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        silverBulletStand = atlas.findRegion("shuriken", 1);

        // Animation
        regions = atlas.findRegions("shuriken");
        silverBulletAnimation = new Animation(0.5f / 13.0f, regions);
        regions.clear();
    }

    public TextureRegion getSilverBulletStand() {
        return silverBulletStand;
    }

    public Animation getSilverBulletAnimation() {
        return silverBulletAnimation;
    }
}
