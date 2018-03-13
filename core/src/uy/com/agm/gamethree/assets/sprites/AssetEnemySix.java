package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemySix {
    private static final String TAG = AssetEnemySix.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)|
    public static final float WIDTH_METERS = 118.0f * 0.6f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 182.0f * 0.6f / PlayScreen.PPM;
    public static final float BEAM_WIDTH_METERS = 50.0f * 0.6f / PlayScreen.PPM;
    public static final float BEAM_HEIGHT_METERS = 182.0f * 0.6f / PlayScreen.PPM;

    private TextureRegion enemySixIdleStand;
    private TextureRegion enemySixBeamStand;
    private TextureRegion powerBeamStand;
    private Animation enemySixIdleAnimation;
    private Animation enemySixBeamAnimation;
    private Animation powerBeamAnimation;

    public AssetEnemySix(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemySixIdleStand = atlas.findRegion("enemySixIdle", 1);
        enemySixBeamStand = atlas.findRegion("enemySixBeam", 1);
        powerBeamStand = atlas.findRegion("beam", 1);

        // Animation
        regions = atlas.findRegions("enemySixIdle");
        enemySixIdleAnimation = new Animation(0.5f / 7.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("enemySixBeam");
        enemySixBeamAnimation = new Animation(0.5f / 5.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("beam");
        powerBeamAnimation = new Animation(0.5f / 5.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemySixIdleStand() {
        return enemySixIdleStand;
    }

    public TextureRegion getEnemySixBeamStand() {
        return enemySixBeamStand;
    }

    public TextureRegion getPowerBeamStand() {
        return powerBeamStand;
    }

    public Animation getEnemySixIdleAnimation() {
        return enemySixIdleAnimation;
    }

    public Animation getEnemySixBeamAnimation() {
        return enemySixBeamAnimation;
    }

    public Animation getPowerBeamAnimation() {
        return powerBeamAnimation;
    }
}