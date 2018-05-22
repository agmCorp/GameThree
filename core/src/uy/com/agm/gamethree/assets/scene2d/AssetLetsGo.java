package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetLetsGo {
    private static final String TAG = AssetLetsGo.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 310.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 198.0f * 1.0f;

    private TextureRegion stageLetsGoStand;
    private Animation stageLetsGoAnimation;

    public AssetLetsGo(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        stageLetsGoStand = atlas.findRegion("letsGo", 1);

        // Animation
        regions = atlas.findRegions("letsGo");
        stageLetsGoAnimation = new Animation(0.5f / 50.0f, regions);
        regions.clear();
    }

    public TextureRegion getStageLetsGoStand() {
        return stageLetsGoStand;
    }

    public Animation getStageLetsGoAnimation() {
        return stageLetsGoAnimation;
    }
}
