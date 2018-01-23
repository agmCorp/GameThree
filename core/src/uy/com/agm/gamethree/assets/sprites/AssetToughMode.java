package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetToughMode {
    private static final String TAG = AssetToughMode.class.getName();

    private TextureRegion toughModeStand;
    private Animation toughModeAnimation;

    public AssetToughMode(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        toughModeStand = atlas.findRegion("toughMode", 1);

        // Animation
        regions = atlas.findRegions("toughMode");
        toughModeAnimation = new Animation(1.0f / 20.0f, regions);
        regions.clear();
    }

    public TextureRegion getToughModeStand() {
        return toughModeStand;
    }

    public Animation getToughModeAnimation() {
        return toughModeAnimation;
    }
}