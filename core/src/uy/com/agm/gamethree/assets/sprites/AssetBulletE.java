package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetBulletE {
    private static final String TAG = AssetBulletE.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 64.0f * 0.8f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 128.0f * 0.8f / PlayScreen.PPM;

    private TextureRegion bulletEStand;
    private Animation bulletEAnimation;

    public AssetBulletE(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bulletEStand = atlas.findRegion("bulletE", 1);

        // Animation
        regions = atlas.findRegions("bulletE");
        bulletEAnimation = new Animation(0.5f / 16.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBulletEStand() {
        return bulletEStand;
    }

    public Animation getBulletEAnimation() {
        return bulletEAnimation;
    }
}