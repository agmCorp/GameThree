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

    private TextureRegion powerThreeStand;
    private Animation powerThreeAnimation;

    public AssetPowerThree(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        powerThreeStand = atlas.findRegion("powerThree", 1);

        // Animation
        regions = atlas.findRegions("powerThree");
        powerThreeAnimation = new Animation(0.5f / 17.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getPowerThreeStand() {
        return powerThreeStand;
    }

    public Animation getPowerThreeAnimation() {
        return powerThreeAnimation;
    }
}