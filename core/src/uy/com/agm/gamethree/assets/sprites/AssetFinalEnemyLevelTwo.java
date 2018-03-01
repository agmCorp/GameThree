package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/30/2017.
 */
// todo revisar todos los assets y sus tiempos
public class AssetFinalEnemyLevelTwo {
    private static final String TAG = AssetFinalEnemyLevelTwo.class.getName();

    private TextureRegion finalEnemyLevelTwoStand;
    private TextureRegion finalEnemyLevelTwoPowerStand;
    private Animation finalEnemyLevelTwoIdleAnimation;
    private Animation finalEnemyLevelTwoMovingUpAnimation;
    private Animation finalEnemyLevelTwoMovingDownAnimation;
    private Animation finalEnemyLevelTwoMovingLeftRightAnimation;
    private Animation finalEnemyLevelTwoShootingUpAnimation;
    private Animation finalEnemyLevelTwoShootingDownAnimation;
    private Animation finalEnemyLevelTwoShootingLeftRightAnimation;
    private Animation finalEnemyLevelTwoDeathAnimation;
    private Animation finalEnemyLevelTwoPowerAnimation;

    public AssetFinalEnemyLevelTwo(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        finalEnemyLevelTwoStand = atlas.findRegion("finalEnemyLevelOneIdle", 1);
        finalEnemyLevelTwoPowerStand = atlas.findRegion("finalEnemyLevelOnePower", 1);

        // Animation
        regions = atlas.findRegions("finalEnemyLevelOneIdle");
        finalEnemyLevelTwoIdleAnimation = new Animation(0.4f / 9.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroUp");
        finalEnemyLevelTwoMovingUpAnimation = new Animation(0.5f / 18.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroDown");
        finalEnemyLevelTwoMovingDownAnimation = new Animation(0.5f / 18.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroUp");
        finalEnemyLevelTwoMovingLeftRightAnimation = new Animation(0.5f / 18.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("SlashingUp");
        finalEnemyLevelTwoShootingUpAnimation = new Animation(0.5f / 12.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("SlashingDown");
        finalEnemyLevelTwoShootingDownAnimation = new Animation(0.5f / 12.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("SlashingDown");
        finalEnemyLevelTwoShootingLeftRightAnimation = new Animation(0.5f / 18.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroDead");
        finalEnemyLevelTwoDeathAnimation = new Animation(0.5f / 12.0f, regions);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelOnePower");
        finalEnemyLevelTwoPowerAnimation = new Animation(1.0f / 30.0f, regions);
        regions.clear();
    }

    public TextureRegion getFinalEnemyLevelTwoStand() {
        return finalEnemyLevelTwoStand;
    }

    public TextureRegion getFinalEnemyLevelTwoPowerStand() {
        return finalEnemyLevelTwoPowerStand;
    }

    public Animation getFinalEnemyLevelTwoIdleAnimation() {
        return finalEnemyLevelTwoIdleAnimation;
    }

    public Animation getFinalEnemyLevelTwoMovingUpAnimation() {
        return finalEnemyLevelTwoMovingUpAnimation;
    }

    public Animation getFinalEnemyLevelTwoMovingDownAnimation() {
        return finalEnemyLevelTwoMovingDownAnimation;
    }

    public Animation getFinalEnemyLevelTwoMovingLeftRightAnimation() {
        return finalEnemyLevelTwoMovingLeftRightAnimation;
    }

    public Animation getFinalEnemyLevelTwoShootingUpAnimation() {
        return finalEnemyLevelTwoShootingUpAnimation;
    }

    public Animation getFinalEnemyLevelTwoShootingDownAnimation() {
        return finalEnemyLevelTwoShootingDownAnimation;
    }

    public Animation getFinalEnemyLevelTwoShootingLeftRightAnimation() {
        return finalEnemyLevelTwoShootingLeftRightAnimation;
    }

    public Animation getFinalEnemyLevelTwoDeathAnimation() {
        return finalEnemyLevelTwoDeathAnimation;
    }

    public Animation getFinalEnemyLevelTwoPowerAnimation() {
        return finalEnemyLevelTwoPowerAnimation;
    }
}
