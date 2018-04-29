package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyTen {
    private static final String TAG = AssetEnemyTen.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 130.0f * 0.6f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 130.0f * 0.6f / PlayScreen.PPM;

    private TextureRegion enemyTenStand;
    private Animation enemyTenAnimation;

    public AssetEnemyTen(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyTenStand = atlas.findRegion("enemyTen", 1);

        // Animation
        regions = atlas.findRegions("enemyTen");
        enemyTenAnimation = new Animation(0.5f / 16.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyTenStand() {
        return enemyTenStand;
    }

    public Animation getEnemyTenAnimation() {
        return enemyTenAnimation;
    }
}