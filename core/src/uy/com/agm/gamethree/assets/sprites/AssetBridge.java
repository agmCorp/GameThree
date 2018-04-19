package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetBridge {
    private static final String TAG = AssetBridge.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 300.0f * 0.64f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 300.0f * 0.64f / PlayScreen.PPM;

    private TextureRegion bridgeA;
    private TextureRegion bridgeB;

    public AssetBridge(TextureAtlas atlas) {
        bridgeA = atlas.findRegion("bridgeA");
        bridgeB = atlas.findRegion("bridgeB");
    }

    public TextureRegion getBridgeA() {
        return bridgeA;
    }

    public TextureRegion getBridgeB() {
        return bridgeB;
    }
}