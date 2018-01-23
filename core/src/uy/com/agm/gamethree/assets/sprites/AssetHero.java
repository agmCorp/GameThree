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

    private TextureRegion heroStand;
    private Animation heroDeadAnimation;
    private Animation heroMovingUpAnimation;
    private Animation heroMovingDownAnimation;
    private Animation heroMovingLeftRightAnimation;

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

    public TextureRegion getHeroStand() {
        return heroStand;
    }

    public Animation getHeroDeadAnimation() {
        return heroDeadAnimation;
    }

    public Animation getHeroMovingUpAnimation() {
        return heroMovingUpAnimation;
    }

    public Animation getHeroMovingDownAnimation() {
        return heroMovingDownAnimation;
    }

    public Animation getHeroMovingLeftRightAnimation() {
        return heroMovingLeftRightAnimation;
    }
}
