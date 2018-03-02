package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyFour {
    private static final String TAG = AssetEnemyFour.class.getName();

    private TextureRegion enemyFourStand;
    private Animation enemyFourAnimation;
    private Animation enemyFourFrozenAnimation;

    public AssetEnemyFour(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyFourStand = atlas.findRegion("enemyFour", 1);

        // Animation
        regions = atlas.findRegions("enemyFour");
        enemyFourAnimation = new Animation(0.4f / 17.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("enemyFourFrozen");
        enemyFourFrozenAnimation = new Animation(0.4f / 17.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyFourStand() {
        return enemyFourStand;
    }

    public Animation getEnemyFourAnimation() {
        return enemyFourAnimation;
    }

    public Animation getEnemyFourFrozenAnimation() {
        return enemyFourFrozenAnimation;
    }
}