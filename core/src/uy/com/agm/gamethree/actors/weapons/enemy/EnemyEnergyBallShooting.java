package uy.com.agm.gamethree.actors.weapons.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetBulletF;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.ActorDef;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Vector2Util;

/**
 * Created by amorales on 5/3/2018.
 */

public class EnemyEnergyBallShooting implements IShootStrategy {
    private static final String TAG = EnemyEnergyBallShooting.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float CIRCLE_SHAPE_RADIUS_METERS = 10.0f / PlayScreen.PPM;
    private static final float ENERGY_BALL_LINEAR_VELOCITY = 2.0f;

    private PlayScreen screen;
    private float openFireTime;
    private float fireDelay;
    private Vector2 tmp; // Temporary GC friendly vector

    private Animation bulletAnimation;

    public EnemyEnergyBallShooting(PlayScreen screen, float initialOpenFireTime, float fireDelay) {
        this.screen = screen;
        this.openFireTime = initialOpenFireTime;
        this.fireDelay = fireDelay;

        // Temporary GC friendly vector
        tmp = new Vector2();

        // Animations
        bulletAnimation = Assets.getInstance().getBulletF().getBulletFAnimation();
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

    @Override
    public void shootWhenVisible() {
        this.openFireTime = fireDelay;
    }

    @Override
    public void setFireDelay(float fireDelaySeconds) {
        this.fireDelay = fireDelaySeconds;
    }

    private void shootImp(float x, float y) {
        // Move EnemyBullet from Enemy to Hero
        Vector2 heroPosition = screen.getCreator().getHero().getB2body().getPosition();
        tmp.set(x, y);
        Vector2Util.goToTarget(tmp, heroPosition.x, heroPosition.y, ENERGY_BALL_LINEAR_VELOCITY);

        float angle = tmp.angle();
        angle = (angle >= 90.0f) ? angle - 90.0f : 270.0f + angle;

        screen.getCreator().createGameThreeActor(new ActorDef(new EnemyBullet(screen, x, y,
                AssetBulletF.WIDTH_METERS,
                AssetBulletF.HEIGHT_METERS,
                CIRCLE_SHAPE_RADIUS_METERS,
                angle,
                tmp.x,
                tmp.y,
                bulletAnimation)));
        // Sound FX
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getEnergyBallShoot());
    }
}
