package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemySeven {
    private static final String TAG = AssetEnemySeven.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)|
    public static final float WIDTH_METERS = 120.0f * 0.7f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 115.0f * 0.7f / PlayScreen.PPM;

    private TextureRegion enemySevenStand;
    private Animation enemySevenAnimation;

    public AssetEnemySeven(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemySevenStand = atlas.findRegion("enemySeven", 1);

        // Animation
        regions = atlas.findRegions("enemySeven");
        enemySevenAnimation = new Animation(0.3f / 4.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemySevenStand() {
        return enemySevenStand;
    }

    public Animation getEnemySevenAnimation() {
        return enemySevenAnimation;
    }
}