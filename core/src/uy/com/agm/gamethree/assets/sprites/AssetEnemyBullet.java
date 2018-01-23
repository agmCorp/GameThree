package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyBullet {
    private static final String TAG = AssetEnemyBullet.class.getName();

    private TextureRegion enemyBulletStand;
    private Animation enemyBulletAnimation;

    public AssetEnemyBullet(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyBulletStand = atlas.findRegion("enemyBullet", 1);

        // Animation
        regions = atlas.findRegions("enemyBullet");
        enemyBulletAnimation = new Animation(0.5f / 10.0f, regions);
        regions.clear();
    }

    public TextureRegion getEnemyBulletStand() {
        return enemyBulletStand;
    }

    public Animation getEnemyBulletAnimation() {
        return enemyBulletAnimation;
    }
}