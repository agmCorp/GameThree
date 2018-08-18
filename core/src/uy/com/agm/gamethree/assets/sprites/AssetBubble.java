package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/23/2017.
 */

public class AssetBubble {
    private static final String TAG = AssetBubble.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 108.0f * 1.0f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 108.0f * 1.0f / PlayScreen.PPM;

    private TextureRegion bubbleStand;
    private TextureRegion bubblePopStand;
    private Animation bubbleAnimation;
    private Animation bubblePopAnimation;

    public AssetBubble(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bubbleStand = atlas.findRegion("bubble", 1);
        bubblePopStand = atlas.findRegion("bubblePop", 1);

        // Animation
        regions = atlas.findRegions("bubble");
        bubbleAnimation = new Animation(0.8f / 14.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bubblePop");
        bubblePopAnimation = new Animation(0.7f / 11.0f, regions);
        regions.clear();
    }

    public TextureRegion getBubbleStand() {
        return bubbleStand;
    }

    public TextureRegion getBubblePopStand() {
        return bubblePopStand;
    }

    public Animation getBubbleAnimation() {
        return bubbleAnimation;
    }

    public Animation getBubblePopAnimation() {
        return bubblePopAnimation;
    }
}