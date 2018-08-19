package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyTwelve {
    private static final String TAG = AssetEnemyTwelve.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 120.0f * 0.7f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 115.0f * 0.7f / PlayScreen.PPM;

    private TextureRegion enemyTwelveStand;
    private Animation enemyTwelveAnimation;
    private Animation enemyTwelveWeakAnimation;

    public AssetEnemyTwelve(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyTwelveStand = atlas.findRegion("enemyTwelve", 1);

        // Animation
        regions = atlas.findRegions("enemyTwelve");
        enemyTwelveAnimation = new Animation(0.3f / 3.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("enemyTwelveWeak");
        enemyTwelveWeakAnimation = new Animation(0.3f / 3.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyTwelveStand() {
        return enemyTwelveStand;
    }

    public Animation getEnemyTwelveAnimation() {
        return enemyTwelveAnimation;
    }

    public Animation getEnemyTwelveWeakAnimation() {
        return enemyTwelveWeakAnimation;
    }
}