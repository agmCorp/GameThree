package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetPowerThree {
    private static final String TAG = AssetPowerThree.class.getName();

    public final TextureRegion powerThreeStand;
    public final Animation powerThreeAnimation;

    public AssetPowerThree(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        powerThreeStand = atlas.findRegion("bulletA", 1);

        // Animation
        regions = atlas.findRegions("bulletA");
        powerThreeAnimation = new Animation(0.5f / 4.0f, regions);
        regions.clear();
    }
}