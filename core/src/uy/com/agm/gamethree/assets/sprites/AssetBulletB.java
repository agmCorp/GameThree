package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetBulletB {
    private static final String TAG = AssetBulletB.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 40.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 100.0f * 1.0f / PlayScreen.PPM;
    public static final float CIRCLE_SHAPE_RADIUS_METERS = 30.0f / PlayScreen.PPM;

    private TextureRegion bulletBStand;
    private Animation bulletBAnimation;

    public AssetBulletB(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bulletBStand = atlas.findRegion("bulletB", 1);

        // Animation
        regions = atlas.findRegions("bulletB");
        bulletBAnimation = new Animation(0.5f / 13.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBulletBStand() {
        return bulletBStand;
    }

    public Animation getBulletBAnimation() {
        return bulletBAnimation;
    }
}