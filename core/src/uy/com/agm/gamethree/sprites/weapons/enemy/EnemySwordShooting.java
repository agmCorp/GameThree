package uy.com.agm.gamethree.sprites.weapons.enemy;

import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetBulletC;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;
import uy.com.agm.gamethree.tools.actordef.ActorDefBullet;

/**
 * Created by amorales on 5/3/2018.
 */

public class EnemySwordShooting implements IShootStrategy {
    private static final String TAG = EnemySwordShooting.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 30.0f / PlayScreen.PPM;
    private static final float SWORD_LINEAR_VELOCITY = 2.0f;

    private PlayScreen screen;
    private float openFireTime;
    private float fireDelay;
    private Vector2 tmp; // Temp GC friendly vector

    public EnemySwordShooting(PlayScreen screen, float initialOpenFireTime, float fireDelay) {
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
        Vector2Util.goToTarget(tmp, screen.getPlayer().getB2body().getPosition().x, screen.getPlayer().getB2body().getPosition().y, SWORD_LINEAR_VELOCITY);

        float angle = tmp.angle();
        angle = (angle >= 90.0f) ? angle - 90.0f : 270.0f + angle;

        screen.getCreator().createGameThreeActor(new ActorDefBullet(x, y,
                AssetBulletC.WIDTH_METERS,
                AssetBulletC.HEIGHT_METERS,
                CIRCLE_SHAPE_RADIUS_METERS,
                angle,
                tmp.x,
                tmp.y,
                Assets.getInstance().getBulletC().getBulletCAnimation(),
                EnemyBullet.class));
        // Sound FX
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getEnemyShoot());
    }
}
