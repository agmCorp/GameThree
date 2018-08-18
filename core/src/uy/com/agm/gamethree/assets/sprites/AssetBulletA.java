package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetBulletA {
    private static final String TAG = AssetBulletA.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 39.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 102.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion bulletAStand;
    private Animation bulletAAnimation;

    public AssetBulletA(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bulletAStand = atlas.findRegion("bulletA", 1);

        // Animation
        regions = atlas.findRegions("bulletA");
        bulletAAnimation = new Animation(0.5f / 6.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBulletAStand() {
        return bulletAStand;
    }

    public Animation getBulletAAnimation() {
        return bulletAAnimation;
    }
}