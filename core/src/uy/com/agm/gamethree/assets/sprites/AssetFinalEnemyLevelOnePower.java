package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetFinalEnemyLevelOnePower {
    private static final String TAG = AssetFinalEnemyLevelOnePower.class.getName();

    private TextureRegion finalEnemyLevelOnePowerStand;
    private Animation finalEnemyLevelOnePowerAnimation;

    public AssetFinalEnemyLevelOnePower(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        finalEnemyLevelOnePowerStand = atlas.findRegion("finalEnemyLevelOnePower", 1);

        // Animation
        regions = atlas.findRegions("finalEnemyLevelOnePower");
        finalEnemyLevelOnePowerAnimation = new Animation(1.0f / 30.0f, regions);
        regions.clear();
    }

    public TextureRegion getFinalEnemyLevelOnePowerStand() {
        return finalEnemyLevelOnePowerStand;
    }

    public Animation getFinalEnemyLevelOnePowerAnimation() {
        return finalEnemyLevelOnePowerAnimation;
    }
}