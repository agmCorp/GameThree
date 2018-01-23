package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetPowerFour {
    private static final String TAG = AssetPowerFour.class.getName();

    private TextureRegion powerFourStand;
    private Animation powerFourAnimation;

    public AssetPowerFour(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        powerFourStand = atlas.findRegion("powerFour", 1);

        // Animation
        regions = atlas.findRegions("powerFour");
        powerFourAnimation = new Animation(0.5f / 20.0f, regions);
        regions.clear();
    }

    public TextureRegion getPowerFourStand() {
        return powerFourStand;
    }

    public Animation getPowerFourAnimation() {
        return powerFourAnimation;
    }
}