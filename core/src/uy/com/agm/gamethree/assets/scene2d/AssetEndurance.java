package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEndurance {
    private static final String TAG = AssetEndurance.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 100.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 100.0f * 1.0f;
    public static final int MAX_TEXTURES = 16;

    Array<TextureAtlas.AtlasRegion> regions;

    public AssetEndurance(TextureAtlas atlas) {
        regions = atlas.findRegions("endurance");
    }

    public TextureRegion getEnduranceStand(int index) {
        return index < MAX_TEXTURES ? regions.get(index) : regions.get(MAX_TEXTURES - 1);
    }
}
