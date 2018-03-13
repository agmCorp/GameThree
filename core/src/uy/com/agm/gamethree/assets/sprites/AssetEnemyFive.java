package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyFive {
    private static final String TAG = AssetEnemyFive.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)|
    public static final float WIDTH_METERS = 122.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 81.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion enemyFiveStand;
    private Animation enemyFiveAnimation;

    public AssetEnemyFive(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyFiveStand = atlas.findRegion("enemyFive", 1);

        // Animation
        regions = atlas.findRegions("enemyFive");
        enemyFiveAnimation = new Animation(0.5f / 6.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyFiveStand() {
        return enemyFiveStand;
    }

    public Animation getEnemyFiveAnimation() {
        return enemyFiveAnimation;
    }
}