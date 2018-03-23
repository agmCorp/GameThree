package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyEight {
    private static final String TAG = AssetEnemyEight.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)|
    public static final float WIDTH_METERS = 122.0f * 0.8f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 81.0f * 0.8f / PlayScreen.PPM;

    private TextureRegion enemyEightStand;
    private Animation enemyEightAnimation;

    public AssetEnemyEight(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyEightStand = atlas.findRegion("enemyEight", 1);

        // Animation
        regions = atlas.findRegions("enemyEight");
        enemyEightAnimation = new Animation(0.7f / 8.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyEightStand() {
        return enemyEightStand;
    }

    public Animation getEnemyEightAnimation() {
        return enemyEightAnimation;
    }
}