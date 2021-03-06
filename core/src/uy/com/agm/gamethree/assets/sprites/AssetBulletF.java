package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetBulletF {
    private static final String TAG = AssetBulletF.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 40.0f * 0.8f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 100.0f  * 0.8f / PlayScreen.PPM;

    private TextureRegion bulletFStand;
    private Animation bulletFAnimation;

    public AssetBulletF(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bulletFStand = atlas.findRegion("bulletF", 1);

        // Animation
        regions = atlas.findRegions("bulletF");
        bulletFAnimation = new Animation(0.3f / 3.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBulletFStand() {
        return bulletFStand;
    }

    public Animation getBulletFAnimation() {
        return bulletFAnimation;
    }
}