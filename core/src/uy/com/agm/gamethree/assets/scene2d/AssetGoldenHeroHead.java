package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetGoldenHeroHead {
    private static final String TAG = AssetGoldenHeroHead.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 60.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 60.0f * 1.0f;

    private TextureRegion goldenHeroHeadStand;
    private Animation goldenHeroHeadAnimation;

    public AssetGoldenHeroHead(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        goldenHeroHeadStand = atlas.findRegion("goldenHeroHeadBlink", 1);

        // Animation
        regions = atlas.findRegions("goldenHeroHeadBlink");
        goldenHeroHeadAnimation = new Animation(1.0f / 2.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getGoldenHeroHeadStand() {
        return goldenHeroHeadStand;
    }

    public Animation getGoldenHeroHeadAnimation() {
        return goldenHeroHeadAnimation;
    }
}
