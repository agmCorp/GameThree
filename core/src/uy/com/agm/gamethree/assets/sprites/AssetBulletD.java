package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetBulletD {
    private static final String TAG = AssetBulletD.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 50.0f * 0.8f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 86.0f * 0.8f / PlayScreen.PPM;

    private TextureRegion bulletDStand;
    private Animation bulletDAnimation;

    public AssetBulletD(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bulletDStand = atlas.findRegion("bulletD", 1);

        // Animation
        regions = atlas.findRegions("bulletD");
        bulletDAnimation = new Animation(0.5f / 5.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBulletDStand() {
        return bulletDStand;
    }

    public Animation getBulletDAnimation() {
        return bulletDAnimation;
    }
}