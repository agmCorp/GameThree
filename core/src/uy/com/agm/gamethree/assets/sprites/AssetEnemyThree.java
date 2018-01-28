package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyThree {
    private static final String TAG = AssetEnemyThree.class.getName();

    private TextureRegion enemyThreeStand;
    private Animation enemyThreeAnimation;

    public AssetEnemyThree(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyThreeStand = atlas.findRegion("enemyThree", 1);

        // Animation
        regions = atlas.findRegions("enemyThree");
        enemyThreeAnimation = new Animation(0.5f / 8.0f, regions);
        regions.clear();
    }

    public TextureRegion getEnemyThreeStand() {
        return enemyThreeStand;
    }

    public Animation getEnemyThreeAnimation() {
        return enemyThreeAnimation;
    }
}