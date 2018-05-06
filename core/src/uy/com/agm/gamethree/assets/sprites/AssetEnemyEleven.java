package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyEleven {
    private static final String TAG = AssetEnemyEleven.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)|
    public static final float WIDTH_METERS = 137.0f * 0.6f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 127.0f * 0.6f / PlayScreen.PPM;

    private TextureRegion enemyElevenStand;
    private Animation enemyElevenAnimation;
    private Animation enemyElevenShootAnimation;

    public AssetEnemyEleven(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyElevenStand = atlas.findRegion("enemyEleven", 1);

        // Animation
        regions = atlas.findRegions("enemyEleven");
        enemyElevenAnimation = new Animation(0.3f / 8.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("enemyElevenShoot");
        enemyElevenShootAnimation = new Animation(1.0f / 16.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyElevenStand() {
        return enemyElevenStand;
    }

    public Animation getEnemyElevenAnimation() {
        return enemyElevenAnimation;
    }

    public Animation getEnemyElevenShootAnimation() {
        return enemyElevenShootAnimation;
    }
}