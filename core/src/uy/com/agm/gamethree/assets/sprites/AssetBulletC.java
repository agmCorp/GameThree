package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetBulletC {
    private static final String TAG = AssetBulletC.class.getName();

    private TextureRegion bulletCStand;
    private Animation bulletCAnimation;

    public AssetBulletC(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bulletCStand = atlas.findRegion("bulletC", 1);

        // Animation
        regions = atlas.findRegions("bulletC");
        bulletCAnimation = new Animation(0.5f / 2.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBulletCStand() {
        return bulletCStand;
    }

    public Animation getBulletCAnimation() {
        return bulletCAnimation;
    }
}