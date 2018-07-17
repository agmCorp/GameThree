package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetHeroHead {
    private static final String TAG = AssetHeroHead.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 90.0f * 0.55f;
    public static final float HEIGHT_PIXELS = 88.0f * 0.55f;

    private TextureRegion heroHeadStand;
    private Animation heroHeadAnimation;

    public AssetHeroHead(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        heroHeadStand = atlas.findRegion("heroHeadBlink", 1);

        // Animation
        regions = atlas.findRegions("heroHeadBlink");
        heroHeadAnimation = new Animation(1.0f / 2.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getHeroHeadStand() {
        return heroHeadStand;
    }

    public Animation getHeroHeadAnimation() {
        return heroHeadAnimation;
    }
}
