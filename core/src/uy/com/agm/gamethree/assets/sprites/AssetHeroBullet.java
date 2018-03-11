package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetHeroBullet {
    private static final String TAG = AssetHeroBullet.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 192.0f * 0.6f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 192.0f * 0.6f / PlayScreen.PPM;

    private TextureRegion heroBulletStand;
    private Animation heroBulletAnimation;

    public AssetHeroBullet(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        heroBulletStand = atlas.findRegion("heroBullet", 1);

        // Animation
        regions = atlas.findRegions("heroBullet");
        heroBulletAnimation = new Animation(0.5f / 10.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getHeroBulletStand() {
        return heroBulletStand;
    }

    public Animation getHeroBulletAnimation() {
        return heroBulletAnimation;
    }
}
