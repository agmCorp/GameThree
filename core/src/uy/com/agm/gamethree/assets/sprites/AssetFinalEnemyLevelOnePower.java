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

    public final TextureRegion AssetFinalEnemyLevelOnePowerStand;
    public final Animation AssetFinalEnemyLevelOnePowerAnimation;

    public AssetFinalEnemyLevelOnePower(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        AssetFinalEnemyLevelOnePowerStand = atlas.findRegion("finalEnemyLevelOnePower", 1);

        // Animation
        regions = atlas.findRegions("finalEnemyLevelOnePower");
        AssetFinalEnemyLevelOnePowerAnimation = new Animation(1.0f / 20.0f, regions);
        regions.clear();
    }
}