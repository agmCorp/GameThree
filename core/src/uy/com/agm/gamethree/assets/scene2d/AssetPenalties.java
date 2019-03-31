package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetPenalties {
    private static final String TAG = AssetPenalties.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 100.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 91.0f * 1.0f;

    private TextureRegion penaltiesStand;
    private Animation penaltiesAnimation;

    public AssetPenalties(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        penaltiesStand = atlas.findRegion("skull", 1);

        // Animation
        regions = atlas.findRegions("skull");
        penaltiesAnimation = new Animation(1.0f / 11.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getPenaltiesStand() {
        return penaltiesStand;
    }

    public Animation getPenaltiesAnimation() {
        return penaltiesAnimation;
    }
}
