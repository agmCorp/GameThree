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

    private TextureRegion colSilverBulletStand;
    private Animation colSilverBulletAnimation;

    public AssetSilverBullet(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        silverBulletStand = atlas.findRegion("shuriken", 1);
        colSilverBulletStand = atlas.findRegion("colTwo", 1);

        // Animation
        regions = atlas.findRegions("shuriken");
        silverBulletAnimation = new Animation(0.5f / 13.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("colTwo");
        colSilverBulletAnimation = new Animation(0.5f / 25.0f, regions);
        regions.clear();
    }

    public TextureRegion getSilverBulletStand() {
        return silverBulletStand;
    }

    public TextureRegion getColSilverBulletStand() {
        return colSilverBulletStand;
    }

    public Animation getSilverBulletAnimation() {
        return silverBulletAnimation;
    }

    public Animation getColSilverBulletAnimation() {
        return colSilverBulletAnimation;
    }
}
