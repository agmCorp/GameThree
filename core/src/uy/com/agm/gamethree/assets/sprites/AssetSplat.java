package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

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

    private Array<TextureRegion> splats;

    public AssetSplat(TextureAtlas atlas) {
        splats = new Array<TextureRegion>();
        for(int i = 1; i <= MAX_TEXTURES; i++) {
            splats.add(atlas.findRegion("splat", i));
        }
    }

    public TextureRegion getRandomSplat() {
        return splats.get(MathUtils.random(0, MAX_TEXTURES - 1));
    }
}