package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/30/2017.
 */

public class AssetFinalEnemyLevelOne {
    private static final String TAG = AssetFinalEnemyLevelOne.class.getName();

    private TextureRegion finalEnemyLevelOneStand;
    private TextureRegion finalEnemyLevelOnePowerStand;
    private TextureRegion finalEnemyLevelOneBackground;
    private Animation finalEnemyLevelOneIdleAnimation;
    private Animation finalEnemyLevelOneWalkAnimation;
    private Animation finalEnemyLevelOneShootAnimation;
    private Animation finalEnemyLevelOneDeathAnimation;
    private Animation finalEnemyLevelOnePowerAnimation;

    public AssetFinalEnemyLevelOne(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        finalEnemyLevelOneStand = atlas.findRegion("finalEnemyLevelOneIdle", 1);
        finalEnemyLevelOnePowerStand = atlas.findRegion("finalEnemyLevelOnePower", 1);
        finalEnemyLevelOneBackground = atlas.findRegion("spiderWeb");

        // Animation
        regions = atlas.findRegions("finalEnemyLevelOneIdle");
        finalEnemyLevelOneIdleAnimation = new Animation(0.4f / 9.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelOneWalk");
        finalEnemyLevelOneWalkAnimation = new Animation(0.7f / 17.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelOneShoot");
        finalEnemyLevelOneShootAnimation = new Animation(0.5f / 13.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelOneDeath");
        finalEnemyLevelOneDeathAnimation = new Animation(1.4f / 34.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("finalEnemyLevelOnePower");
        finalEnemyLevelOnePowerAnimation = new Animation(1.0f / 30.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getFinalEnemyLevelOneStand() {
        return finalEnemyLevelOneStand;
    }

    public TextureRegion getFinalEnemyLevelOnePowerStand() {
        return finalEnemyLevelOnePowerStand;
    }

    public TextureRegion getFinalEnemyLevelOneBackground() {
        return finalEnemyLevelOneBackground;
    }

    public Animation getFinalEnemyLevelOneIdleAnimation() {
        return finalEnemyLevelOneIdleAnimation;
    }

    public Animation getFinalEnemyLevelOneWalkAnimation() {
        return finalEnemyLevelOneWalkAnimation;
    }

    public Animation getFinalEnemyLevelOneShootAnimation() {
        return finalEnemyLevelOneShootAnimation;
    }

    public Animation getFinalEnemyLevelOneDeathAnimation() {
        return finalEnemyLevelOneDeathAnimation;
    }

    public Animation getFinalEnemyLevelOnePowerAnimation() {
        return finalEnemyLevelOnePowerAnimation;
    }
}
