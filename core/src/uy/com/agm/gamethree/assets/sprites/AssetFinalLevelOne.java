package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/30/2017.
 */

public class AssetFinalLevelOne {
    private static final String TAG = AssetFinalLevelOne.class.getName();

    public final TextureRegion finalLevelOneStand;
    public final Animation finalLevelOneIdleAnimation;
    public final Animation finalLevelOneWalkAnimation;
    public final Animation finalLevelOneShootAnimation;
    public final Animation finalLevelOneDeathAnimation;

    public AssetFinalLevelOne(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        finalLevelOneStand = atlas.findRegion("finalLevelOneIdle", 1);

        // Animation
        regions = atlas.findRegions("finalLevelOneIdle");
        finalLevelOneIdleAnimation = new Animation(0.4f / 9.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalLevelOneWalk");
        finalLevelOneWalkAnimation = new Animation(0.7f / 17.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalLevelOneShoot");
        finalLevelOneShootAnimation = new Animation(0.5f / 13.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalLevelOneDeath");
        finalLevelOneDeathAnimation = new Animation(1.4f / 34.0f, regions);
        regions.clear();
    }
}
