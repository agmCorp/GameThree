package uy.com.agm.gamethree.sprites.weapons.enemy;

import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetEnemyBullet;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.actordef.ActorDefBullet;

/**
 * Created by amorales on 5/3/2018.
 */

public class EnemyDefaultShooting implements IShootStrategy {
    private static final String TAG = EnemyDefaultShooting.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float DEFAULT_BULLET_OFFSET_METERS = 40.0f / PlayScreen.PPM;
    public static final float DEFAULT_BULLET_LINEAR_VELOCITY = 2.0f;
    public static final float DEFAULT_BULLET_CIRCLE_SHAPE_RADIUS_METERS = 10.0f / PlayScreen.PPM;

    private PlayScreen screen;
    private float openFireTime;
    private float fireDelay;
    private Vector2 tmp; // Temp GC friendly vector

    public EnemyDefaultShooting(PlayScreen screen, float initialOpenFireTime, float fireDelay) {
        this.screen = screen;
        this.openFireTime = initialOpenFireTime;
        this.fireDelay = fireDelay;

        // Temp GC friendly vector
        tmp = new Vector2();
    }

    @Override
    public void updateShoot(float dt) {
        openFireTime += dt;
    }

    @Override
    public void shoot(float x, float y) {
        if (openFireTime > fireDelay) {
            shootImp(x, y);
            openFireTime = 0;
        }
    }

    private void shootImp(float x, float y) {
        // Move EnemyBullet from Enemy to Hero
        tmp.set(x, y);
        Vector2Util.goToTarget(tmp, screen.getPlayer().getB2body().getPosition().x, screen.getPlayer().getB2body().getPosition().y, DEFAULT_BULLET_LINEAR_VELOCITY);

        screen.getCreator().createGameThreeActor(new ActorDefBullet(x, y,
                AssetEnemyBullet.WIDTH_METERS,
                AssetEnemyBullet.HEIGHT_METERS,
                DEFAULT_BULLET_CIRCLE_SHAPE_RADIUS_METERS,
                0,
                tmp.x,
                tmp.y,
                Assets.getInstance().getEnemyBullet().getEnemyBulletAnimation(),
                EnemyBullet.class));
        // Sound FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getEnemyShoot());
    }
}
