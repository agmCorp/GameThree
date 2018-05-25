package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetSilverBullet {
    private static final String TAG = AssetSilverBullet.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 72.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 65.0f * 1.0f / PlayScreen.PPM;
    public static final float COLLECTIBLE_WIDTH_METERS = 192.0f * 0.4f / PlayScreen.PPM;
    public static final float COLLECTIBLE_HEIGHT_METERS = 192.0f * 0.4f / PlayScreen.PPM;

    private TextureRegion silverBulletStand;
    private Animation silverBulletAnimation;

    private TextureRegion colSilverBulletStand;
    private Animation colSilverBulletAnimation;

    public AssetSilverBullet(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        silverBulletStand = atlas.findRegion("shuriken", 1);
        colSilverBulletStand = atlas.findRegion("colSilverBullet", 1);

        // Animation
        regions = atlas.findRegions("shuriken");
        silverBulletAnimation = new Animation(0.5f / 13.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("colSilverBullet");
        colSilverBulletAnimation = new Animation(0.5f / 25.0f, regions, Animation.PlayMode.LOOP);
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
