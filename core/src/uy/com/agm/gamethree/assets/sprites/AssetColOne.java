package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by amorales on 23/1/2018.
 */

public class AssetColOne {
    private static final String TAG = AssetColOne.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 84.0f * 0.5f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 85.0f * 0.5f / PlayScreen.PPM;

    private TextureRegion goldStand;
    private TextureRegion silverStand;
    private TextureRegion bronzeStand;
    private Animation goldAnimation;
    private Animation silverAnimation;
    private Animation bronzeAnimation;

    public AssetColOne(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        goldStand = atlas.findRegion("gold", 1);
        silverStand  = atlas.findRegion("silver", 1);
        bronzeStand = atlas.findRegion("bronze", 1);

        // Animation
        regions = atlas.findRegions("gold");
        goldAnimation = new Animation(0.5f / 10.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("silver");
        silverAnimation = new Animation(0.5f / 10.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bronze");
        bronzeAnimation = new Animation(0.5f / 10.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getGoldStand() {
        return goldStand;
    }

    public TextureRegion getSilverStand() {
        return silverStand;
    }

    public TextureRegion getBronzeStand() {
        return bronzeStand;
    }

    public Animation getGoldAnimation() {
        return goldAnimation;
    }

    public Animation getSilverAnimation() {
        return silverAnimation;
    }

    public Animation getBronzeAnimation() {
        return bronzeAnimation;
    }
}
