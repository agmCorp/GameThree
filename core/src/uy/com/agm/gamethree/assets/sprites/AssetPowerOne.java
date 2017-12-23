package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetPowerOne {
    private static final String TAG = AssetPowerOne.class.getName();

    public final TextureRegion powerOneStand;
    public final Animation powerOneAnimation;

    public AssetPowerOne(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        powerOneStand = atlas.findRegion("powerOne", 1);

        // Animation
        regions = atlas.findRegions("powerOne");
        powerOneAnimation = new Animation(1.0f / 16.0f, regions);
        regions.clear();
    }
}