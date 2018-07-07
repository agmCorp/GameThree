package uy.com.agm.gamethree.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetEnemyBullet {
    private static final String TAG = AssetEnemyBullet.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 192.0f * 0.8f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 192.0f  * 0.8f / PlayScreen.PPM;
    public static final float MUZZLE_FLASH_WIDTH_METERS = 192.0f * 0.6f / PlayScreen.PPM;
    public static final float MUZZLE_FLASH_HEIGHT_METERS = 192.0f * 0.6f / PlayScreen.PPM;

    private TextureRegion enemyBulletStand;
    private TextureRegion enemyBulletMuzzleFlashShoot;
    private TextureRegion enemyBulletMuzzleFlashImpact;
    private Animation enemyBulletAnimation;

    public AssetEnemyBullet(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyBulletStand = atlas.findRegion("enemyBullet", 1);
        enemyBulletMuzzleFlashShoot = atlas.findRegion("enemyBulletMuzzleFlashShoot");
        enemyBulletMuzzleFlashImpact = atlas.findRegion("enemyBulletMuzzleFlashImpact");

        // Animation
        regions = atlas.findRegions("enemyBullet");
        enemyBulletAnimation = new Animation(0.5f / 10.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyBulletStand() {
        return enemyBulletStand;
    }

    public TextureRegion getEnemyBulletMuzzleFlashShoot() {
        return enemyBulletMuzzleFlashShoot;
    }

    public TextureRegion getEnemyBulletMuzzleFlashImpact() {
        return enemyBulletMuzzleFlashImpact;
    }

    public Animation getEnemyBulletAnimation() {
        return enemyBulletAnimation;
    }
}