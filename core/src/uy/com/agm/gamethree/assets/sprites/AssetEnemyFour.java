package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyFour {
    private static final String TAG = AssetEnemyFour.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 125.0f * 0.8f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 106.0f * 0.8f / PlayScreen.PPM;
    public static final float FROZEN_WIDTH_METERS = 123.0f * 0.8f / PlayScreen.PPM;
    public static final float FROZEN_HEIGHT_METERS = 99.0f * 0.8f / PlayScreen.PPM;

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