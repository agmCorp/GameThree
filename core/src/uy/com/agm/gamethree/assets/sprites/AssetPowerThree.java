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
    public final TextureRegion powerThreeFireStand;
    public final Animation powerThreeFireAnimation;

    public AssetPowerThree(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        powerThreeStand = atlas.findRegion("powerThree", 1);

        // Animation
        regions = atlas.findRegions("powerThree");
        powerThreeAnimation = new Animation(1.0f / 16.0f, regions); // todo
        regions.clear();

        powerThreeFireStand = atlas.findRegion("powerThreeFire", 1);

        // Animation
        regions = atlas.findRegions("powerThreeFire");
        powerThreeFireAnimation = new Animation(1.0f / 16.0f, regions); // todo
        regions.clear();
    }
}