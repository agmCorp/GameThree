package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetShield {
    private static final String TAG = AssetShield.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 181.0f * 1.3f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 181.0f * 1.3f / PlayScreen.PPM;

    private TextureRegion shieldStand;
    private Animation shieldAnimation;

    public AssetShield(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        shieldStand = atlas.findRegion("shield", 1);

        // Animation
        regions = atlas.findRegions("shield");
        shieldAnimation = new Animation(0.3f / 21.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getShieldStand() {
        return shieldStand;
    }

    public Animation getShieldAnimation() {
        return shieldAnimation;
    }
}