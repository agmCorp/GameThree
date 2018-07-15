package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetGrace {
    private static final String TAG = AssetGrace.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 100.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 100.0f * 1.0f;

    private TextureRegion graceStand;
    private TextureRegion grace100;
    private TextureRegion grace75;
    private TextureRegion grace50;
    private TextureRegion grace25;
    private TextureRegion grace0;

    public AssetGrace(TextureAtlas atlas) {
        graceStand = atlas.findRegion("grace100");
        grace100 = atlas.findRegion("grace100");
        grace75 = atlas.findRegion("grace75");
        grace50 = atlas.findRegion("grace50");
        grace25 = atlas.findRegion("grace25");
        grace0 = atlas.findRegion("grace0");
    }

    public TextureRegion getGraceStand() {
        return graceStand;
    }

    public TextureRegion getGrace100() {
        return grace100;
    }

    public TextureRegion getGrace75() {
        return grace75;
    }

    public TextureRegion getGrace50() {
        return grace50;
    }

    public TextureRegion getGrace25() {
        return grace25;
    }

    public TextureRegion getGrace0() {
        return grace0;
    }
}
