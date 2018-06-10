package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetHourglass {
    private static final String TAG = AssetHourglass.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 70.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 70.0f * 1.0f;

    private TextureRegion hourglassStand;
    private Animation hourglassAnimation;

    public AssetHourglass(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        hourglassStand = atlas.findRegion("hourglass", 1);

        // Animation
        regions = atlas.findRegions("hourglass");
        hourglassAnimation = new Animation(2.0f / 50.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getHourglassStand() {
        return hourglassStand;
    }

    public Animation getHourglassAnimation() {
        return hourglassAnimation;
    }
}
