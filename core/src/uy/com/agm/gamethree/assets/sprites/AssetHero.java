package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetHero {
    private static final String TAG = AssetHero.class.getName();

    public final TextureRegion heroStand;
    public final Animation heroDeadAnimation;
    public final Animation heroMovingUpAnimation;
    public final Animation heroMovingDownAnimation;
    public final Animation heroMovingLeftRightAnimation;

    public AssetHero(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        heroStand = atlas.findRegion("heroUp", 1);

        // Animation
        regions = atlas.findRegions("heroUp");
        heroMovingUpAnimation = new Animation(0.5f / 6.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroDown");
        heroMovingDownAnimation = new Animation(0.5f / 6.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroUp");
        heroMovingLeftRightAnimation = new Animation(0.5f / 6.0f, regions); // We use the same animation as heroMovingUpAnimation
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroDead");
        heroDeadAnimation = new Animation(0.5f / 10.0f, regions);
        regions.clear();
    }
}
