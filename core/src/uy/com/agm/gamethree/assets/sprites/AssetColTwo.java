package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by amorales on 23/1/2018.
 */

public class AssetColTwo {
    private static final String TAG = AssetColTwo.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 150.0f * 0.8f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 275.0f * 0.8f / PlayScreen.PPM;

    private TextureRegion colTwoStand;
    private Animation colTwoAnimation;

    public AssetColTwo(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        colTwoStand = atlas.findRegion("colTwo", 1);

        // Animation
        regions = atlas.findRegions("colTwo");
        colTwoAnimation = new Animation(0.5f / 30.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getColTwoStand() {
        return colTwoStand;
    }

    public Animation getColTwoAnimation() {
        return colTwoAnimation;
    }
}
