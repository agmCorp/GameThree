package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyNine {
    private static final String TAG = AssetEnemyNine.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)|
    public static final float WIDTH_METERS = 100.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 92.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion enemyNineStand;
    private Animation enemyNineAnimation;
    private Animation enemyNineRaidAnimation;

    public AssetEnemyNine(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyNineStand = atlas.findRegion("enemyNine", 1);

        // Animation
        regions = atlas.findRegions("enemyNine");
        enemyNineAnimation = new Animation(0.3f / 3.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("enemyNineRaid");
        enemyNineRaidAnimation = new Animation(0.3f / 3.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyNineStand() {
        return enemyNineStand;
    }

    public Animation getEnemyNineAnimation() {
        return enemyNineAnimation;
    }

    public Animation getEnemyNineRaidAnimation() {
        return enemyNineRaidAnimation;
    }
}