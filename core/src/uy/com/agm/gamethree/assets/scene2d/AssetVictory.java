package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetVictory {
    private static final String TAG = AssetVictory.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 464.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 227.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion victoryStand;
    private Animation victoryAnimation;

    public AssetVictory(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        victoryStand = atlas.findRegion("victory", 1);

        // Animation
        regions = atlas.findRegions("victory");
        victoryAnimation = new Animation(2.0f / 35.0f, regions);
        regions.clear();
    }

    public TextureRegion getVictoryStand() {
        return victoryStand;
    }

    public Animation getVictoryAnimation() {
        return victoryAnimation;
    }
}
