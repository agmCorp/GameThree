package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetDizzy {
    private static final String TAG = AssetDizzy.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 201.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 136.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion dizzyStand;
    private Animation dizzyAnimation;

    public AssetDizzy(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        dizzyStand = atlas.findRegion("dizzy", 1);

        // Animation
        regions = atlas.findRegions("dizzy");
        dizzyAnimation = new Animation(1.6f / 20.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getDizzyStand() {
        return dizzyStand;
    }

    public Animation getDizzyAnimation() {
        return dizzyAnimation;
    }
}