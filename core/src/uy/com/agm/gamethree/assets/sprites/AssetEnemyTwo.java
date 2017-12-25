package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyTwo {
    private static final String TAG = AssetEnemyTwo.class.getName();

    public final TextureRegion enemyTwoStand;
    public final Animation enemyTwoAnimation;

    public AssetEnemyTwo(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyTwoStand = atlas.findRegion("enemyTwo", 1);

        // Animation
        regions = atlas.findRegions("enemyTwo");
        enemyTwoAnimation = new Animation(0.5f / 8.0f, regions);
        regions.clear();
    }
}