package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetSplat {
    private static final String TAG = AssetSplat.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)|
    public static final float WIDTH_METERS = 100.0f * 0.6f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 100.0f * 0.6f / PlayScreen.PPM;
    public static final int MAX_TEXTURES = 16;

    private TextureAtlas atlas;

    public AssetSplat(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    public TextureRegion getRandomSplat() {
        return atlas.findRegion("splat", MathUtils.random(1, MAX_TEXTURES));
    }
}