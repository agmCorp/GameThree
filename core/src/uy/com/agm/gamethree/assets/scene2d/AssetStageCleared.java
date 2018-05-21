package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetStageCleared {
    private static final String TAG = AssetStageCleared.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 325.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 140.0f * 1.0f;

    private TextureRegion stageClearedStand;
    private Animation stageClearedAnimation;

    public AssetStageCleared(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        stageClearedStand = atlas.findRegion("stageCleared", 1);

        // Animation
        regions = atlas.findRegions("stageCleared");
        stageClearedAnimation = new Animation(2.0f / 38.0f, regions);
        regions.clear();
    }

    public TextureRegion getStageClearedStand() {
        return stageClearedStand;
    }

    public Animation getStageClearedAnimation() {
        return stageClearedAnimation;
    }
}
