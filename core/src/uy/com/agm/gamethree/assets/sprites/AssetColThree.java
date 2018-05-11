package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by amorales on 23/1/2018.
 */

public class AssetColThree {
    private static final String TAG = AssetColThree.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 130.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 80.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion colThreeStand;
    private Animation colThreeAnimation;

    public AssetColThree(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        colThreeStand = atlas.findRegion("colThree", 1);

        // Animation
        regions = atlas.findRegions("colThree");
        colThreeAnimation = new Animation(0.3f / 6.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getColThreeStand() {
        return colThreeStand;
    }

    public Animation getColThreeAnimation() {
        return colThreeAnimation;
    }
}
