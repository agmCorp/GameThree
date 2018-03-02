package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyOne {
    private static final String TAG = AssetEnemyOne.class.getName();

    private TextureRegion enemyOneStand;
    private Animation enemyOneAnimation;

    public AssetEnemyOne(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyOneStand = atlas.findRegion("enemyOne", 1);

        // Animation
        regions = atlas.findRegions("enemyOne");
        enemyOneAnimation = new Animation(0.5f / 8.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyOneStand() {
        return enemyOneStand;
    }

    public Animation getEnemyOneAnimation() {
        return enemyOneAnimation;
    }
}