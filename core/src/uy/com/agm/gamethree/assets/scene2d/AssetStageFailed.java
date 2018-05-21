package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetStageFailed {
    private static final String TAG = AssetStageFailed.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 324.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 177.0f * 1.0f;

    private TextureRegion stageFailedStand;
    private Animation stageFailedAnimation;

    public AssetStageFailed(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        stageFailedStand = atlas.findRegion("stageFailed", 1);

        // Animation
        regions = atlas.findRegions("stageFailed");
        stageFailedAnimation = new Animation(2.0f / 50.0f, regions);
        regions.clear();
    }

    public TextureRegion getStageFailedStand() {
        return stageFailedStand;
    }

    public Animation getStageFailedAnimation() {
        return stageFailedAnimation;
    }
}
