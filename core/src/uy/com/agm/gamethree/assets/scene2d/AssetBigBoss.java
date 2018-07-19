package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetBigBoss {
    private static final String TAG = AssetBigBoss.class.getName();

    // Constants (pixels * resizeFactor)
    public static final float WIDTH_PIXELS = 406.0f * 1.0f;
    public static final float HEIGHT_PIXELS = 406.0f * 1.0f;

    private TextureRegion bigBossStand;
    private Animation bigBossHitOneAnimation;
    private Animation bigBossHitTwoAnimation;
    private Animation bigBossIdleAnimation;

    public AssetBigBoss(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        bigBossStand = atlas.findRegion("bigBossIdle", 1);

        // Animation
        regions = atlas.findRegions("bigBossHitOne");
        bigBossHitOneAnimation = new Animation(1.0f / 8.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bigBossHitTwo");
        bigBossHitTwoAnimation = new Animation(1.05f / 8.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("bigBossIdle");
        bigBossIdleAnimation = new Animation(1.0f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getBigBossStand() {
        return bigBossStand;
    }

    public Animation getBigBossHitOneAnimation() {
        return bigBossHitOneAnimation;
    }

    public Animation getBigBossHitTwoAnimation() {
        return bigBossHitTwoAnimation;
    }

    public Animation getBigBossIdleAnimation() {
        return bigBossIdleAnimation;
    }
}
