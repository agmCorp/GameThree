package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetSkullHead {
    private static final String TAG = AssetSkullHead.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 90.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 73.0f * 1.0f;

    private TextureRegion skullHeadStand;
    private Animation skullHeadAnimation;

    public AssetSkullHead(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        skullHeadStand = atlas.findRegion("skullHeadBlink", 1);

        // Animation
        regions = atlas.findRegions("skullHeadBlink");
        skullHeadAnimation = new Animation(1.0f / 2.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getSkullHeadStand() {
        return skullHeadStand;
    }

    public Animation getSkullHeadAnimation() {
        return skullHeadAnimation;
    }
}
