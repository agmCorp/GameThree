package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetBulletG {
    private static final String TAG = AssetBulletG.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 74.0f * 1.3f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 153.0f  * 1.3f / PlayScreen.PPM;

    private TextureRegion bulletGStand;
    private Animation bulletGAnimation;

    public AssetBulletG(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bulletGStand = atlas.findRegion("bulletG", 1);

        // Animation
        regions = atlas.findRegions("bulletG");
        bulletGAnimation = new Animation(0.5f / 11.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBulletGStand() {
        return bulletGStand;
    }

    public Animation getBulletGAnimation() {
        return bulletGAnimation;
    }
}