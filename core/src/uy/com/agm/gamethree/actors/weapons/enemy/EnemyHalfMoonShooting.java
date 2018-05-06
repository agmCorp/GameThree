package uy.com.agm.gamethree.actors.weapons.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.actors.weapons.hero.HeroDefaultShooting;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetBulletA;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.ActorDef;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by amorales on 5/3/2018.
 */

public class EnemyHalfMoonShooting implements IShootStrategy {
    private static final String TAG = EnemyHalfMoonShooting.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float BULLET_CIRCLE_SHAPE_RADIUS_METERS = 10.0f / PlayScreen.PPM;
    private static final float BULLET_VELOCITY_X = 0.0f;
    private static final float BULLET_VELOCITY_Y = -2.0f;

    private PlayScreen screen;
    private float openFireTime;
    private float fireDelay;
    private int numberBullets;
    private Vector2 tmp; // Temporary GC friendly vector

    private Animation bulletAnimation;

    public EnemyHalfMoonShooting(PlayScreen screen, float initialOpenFireTime, float fireDelay, int numberBullets) {
        this.screen = screen;
        this.openFireTime = initialOpenFireTime;
        this.fireDelay = fireDelay;
        this.numberBullets = numberBullets;

        // Temporary GC friendly vector
        tmp = new Vector2();

        // Animations
        bulletAnimation = Assets.getInstance().getBulletE().getBulletEAnimation();
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
        float directionDegrees = 180.0f / (numberBullets + 1);
        float angle;

        for (int i = 1; i <= numberBullets; i++) {
            angle = directionDegrees * i;
            angle = (angle >= 90.0f) ? angle - 90.0f : 270.0f + angle;

            createBullet(x, y + HeroDefaultShooting.DEFAULT_BULLET_OFFSET_METERS,
                    AssetBulletA.BULLET_WIDTH_METERS,
                    AssetBulletA.BULLET_HEIGHT_METERS,
                    BULLET_CIRCLE_SHAPE_RADIUS_METERS,
                    angle,
                    BULLET_VELOCITY_X,
                    BULLET_VELOCITY_Y,
                    bulletAnimation);

            // Sound FX
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getShoot());
        }
    }

    private void createBullet(float x, float y, float width, float height, float circleShapeRadius, float angle, float velocityX, float velocityY, Animation animation) {
        tmp.set(velocityX, velocityY).rotate(angle); // I must rotate the velocity to move in that direction, otherwise I'll move in a straight line to the front.
        screen.getCreator().createGameThreeActor(new ActorDef(new EnemyBullet(screen, x, y,
                width, height, circleShapeRadius, angle, tmp.x, tmp.y, animation)));
    }
}
