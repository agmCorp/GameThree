package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetKinematicBridge {
    private static final String TAG = AssetKinematicBridge.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 300.0f * 0.5f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 300.0f * 0.5f / PlayScreen.PPM;

    private TextureRegion kinematicBridgeA;

    public AssetKinematicBridge(TextureAtlas atlas) {
        kinematicBridgeA = atlas.findRegion("kinematicBridgeA");
    }

    public TextureRegion getKinematicBridgeA() {
        return kinematicBridgeA;
    }
}