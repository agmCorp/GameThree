package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyTwo {
    private static final String TAG = AssetEnemyTwo.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 100.0f * 0.8f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 100.0f * 0.8f / PlayScreen.PPM;

    private TextureRegion enemyTwoStand;
    private Animation enemyTwoAnimation;

    public AssetEnemyTwo(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyTwoStand = atlas.findRegion("enemyTwo", 1);

        // Animation
        regions = atlas.findRegions("enemyTwo");
        enemyTwoAnimation = new Animation(0.5f / 8.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyTwoStand() {
        return enemyTwoStand;
    }

    public Animation getEnemyTwoAnimation() {
        return enemyTwoAnimation;
    }
}