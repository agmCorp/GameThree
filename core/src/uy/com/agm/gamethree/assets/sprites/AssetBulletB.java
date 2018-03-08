package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetBulletB {
    private static final String TAG = AssetBulletB.class.getName();

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