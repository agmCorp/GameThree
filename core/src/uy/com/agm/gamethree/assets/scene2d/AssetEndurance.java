package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEndurance {
    private static final String TAG = AssetEndurance.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 100.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 100.0f * 1.0f;

    private TextureRegion enduranceStand;
    private TextureRegion endurance100;
    private TextureRegion endurance75;
    private TextureRegion endurance50;
    private TextureRegion endurance25;
    private TextureRegion endurance0;

    public AssetEndurance(TextureAtlas atlas) {
        enduranceStand = atlas.findRegion("endurance100");
        endurance100 = atlas.findRegion("endurance100");
        endurance75 = atlas.findRegion("endurance75");
        endurance50 = atlas.findRegion("endurance50");
        endurance25 = atlas.findRegion("endurance25");
        endurance0 = atlas.findRegion("endurance0");
    }

    public TextureRegion getEnduranceStand() {
        return enduranceStand;
    }

    public TextureRegion getEndurance100() {
        return endurance100;
    }

    public TextureRegion getEndurance75() {
        return endurance75;
    }

    public TextureRegion getEndurance50() {
        return endurance50;
    }

    public TextureRegion getEndurance25() {
        return endurance25;
    }

    public TextureRegion getEndurance0() {
        return endurance0;
    }
}
