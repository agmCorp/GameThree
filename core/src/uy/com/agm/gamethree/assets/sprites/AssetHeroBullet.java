package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetHeroBullet {
    private static final String TAG = AssetHeroBullet.class.getName();

    public final TextureRegion heroBulletStand;
    public final Animation heroBulletAnimation;

    public AssetHeroBullet(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        heroBulletStand = atlas.findRegion("heroBullet", 1);

        // Animation
        regions = atlas.findRegions("heroBullet");
        heroBulletAnimation = new Animation(0.5f / 9.0f, regions);
        regions.clear();
    }
}
