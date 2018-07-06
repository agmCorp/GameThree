package uy.com.agm.gamethree.actors.weapons.hero;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetBulletA;
import uy.com.agm.gamethree.assets.sprites.AssetHeroBullet;
import uy.com.agm.gamethree.assets.sprites.AssetSilverBullet;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.actors.player.Hero;
import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.tools.ActorDef;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by amorales on 5/3/2018.
 */

public class HeroHalfMoonShooting implements IShootStrategy {
    private static final String TAG = HeroHalfMoonShooting.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float AUTOMATIC_FIRE_DELAY_SECONDS = 0.2f;
    private static final float MANUAL_FIRE_DELAY_SECONDS = 0.1f;
    private static final float BULLET_CIRCLE_SHAPE_RADIUS_METERS = 20.0f / PlayScreen.PPM;
    private static final float BULLET_VELOCITY_X = 0.0f;
    private static final float BULLET_VELOCITY_Y = 7.0f;
    private static final float RANDOM_OFFSET = 0.8f;
    protected static final float SHAKE_DURATION = 0.8f;

    private PlayScreen screen;
    private int numberBullets;
    private float openFireTime;
    private float fireDelay;
    private Vector2 tmp; // Temporary GC friendly vector

    private Animation heroBulletAnimation;
    private Animation heroSilverBulletAnimation;
    private Animation bulletAnimation;

    public HeroHalfMoonShooting(PlayScreen screen, int numberBullets) {
        this.screen = screen;
        this.numberBullets = numberBullets;
        this.openFireTime = 0;
        this.fireDelay = GameSettings.getInstance().isManualShooting() ? MANUAL_FIRE_DELAY_SECONDS : AUTOMATIC_FIRE_DELAY_SECONDS;

        // Temporary GC friendly vector
        tmp = new Vector2();

        // Animations
        heroBulletAnimation = Assets.getInstance().getHeroBullet().getHeroBulletAnimation();
        heroSilverBulletAnimation = Assets.getInstance().getSilverBullet().getSilverBulletAnimation();
        bulletAnimation = Assets.getInstance().getBulletA().getBulletAAnimation();
    }

    @Override
    public void updateShoot(float dt) {
        openFireTime += dt;
        if (screen.getCreator().getHero().isSilverBulletEnabled()) {
            fireDelay = HeroDefaultShooting.SILVER_BULLET_FIRE_DELAY_SECONDS;
        }
    }

    @Override
    public void shoot(float x, float y) {
        if (openFireTime > fireDelay) {
            shootImp(x, y);
            openFireTime = 0;
        }
    }

    @Override
    public void setInitialOpenFireTime(float initialOpenFireTimeSeconds) {
        this.openFireTime = initialOpenFireTimeSeconds;
    }

    @Override
    public void setFireDelay(float fireDelaySeconds) {
        this.fireDelay = fireDelaySeconds;
    }

    private void shootImp(float x, float y) {
        float directionDegrees = 180.0f / (numberBullets + 1);
        float angle;
        Hero hero = screen.getCreator().getHero();

        for (int i = 1; i <= numberBullets; i++) {
            angle = directionDegrees * i;
            angle = (angle >= 90.0f) ? angle - 90.0f : 270.0f + angle;

            if (hero.isSilverBulletEnabled()) {
                if (hero.hasSilverBullets()) {
                    createBullet(x, y + HeroDefaultShooting.DEFAULT_BULLET_OFFSET_METERS,
                            AssetSilverBullet.WIDTH_METERS,
                            AssetSilverBullet.HEIGHT_METERS,
                            HeroDefaultShooting.SILVER_BULLET_CIRCLE_SHAPE_RADIUS_METERS,
                            angle,
                            HeroDefaultShooting.SILVER_BULLET_VELOCITY_X,
                            HeroDefaultShooting.SILVER_BULLET_VELOCITY_Y,
                            heroSilverBulletAnimation);
                    // Sound FX
                    AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHeroShootSwish());
                    if (i == numberBullets) {
                        hero.decreaseSilverBullets();
                    }
                } else {
                    // Sound FX
                    AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHeroShootEmpty());
                }
            } else {
                if (screen.getHud().isWeaponPowerRunningOut()) {
                    createBullet(x, y + HeroDefaultShooting.DEFAULT_BULLET_OFFSET_METERS,
                            AssetHeroBullet.WIDTH_METERS,
                            AssetHeroBullet.HEIGHT_METERS,
                            HeroDefaultShooting.DEFAULT_BULLET_CIRCLE_SHAPE_RADIUS_METERS,
                            angle,
                            BULLET_VELOCITY_X + (MathUtils.randomBoolean() ? MathUtils.randomSign() * RANDOM_OFFSET : 0), // Less accuracy is more fun
                            BULLET_VELOCITY_Y + (MathUtils.randomBoolean() ? MathUtils.randomSign() * RANDOM_OFFSET : 0), // Less accuracy is more fun
                            heroBulletAnimation);
                } else {
                    createBullet(x, y + HeroDefaultShooting.DEFAULT_BULLET_OFFSET_METERS,
                            AssetBulletA.BULLET_WIDTH_METERS,
                            AssetBulletA.BULLET_HEIGHT_METERS,
                            BULLET_CIRCLE_SHAPE_RADIUS_METERS,
                            angle,
                            BULLET_VELOCITY_X + (MathUtils.randomBoolean() ? MathUtils.randomSign() * RANDOM_OFFSET : 0), // Less accuracy is more fun
                            BULLET_VELOCITY_Y + (MathUtils.randomBoolean() ? MathUtils.randomSign() * RANDOM_OFFSET : 0), // Less accuracy is more fun
                            bulletAnimation);
                }
                // Sound FX
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHeroShoot());
            }
        }
    }

    private void createBullet(float x, float y, float width, float height, float circleShapeRadius, float angle, float velocityX, float velocityY, Animation animation) {
        tmp.set(velocityX, velocityY).rotate(angle); // I must rotate the velocity to move in that direction, otherwise I'll move in a straight line to the front.
        screen.getCreator().createGameThreeActor(new ActorDef(new HeroBullet(screen, x, y,
                width, height, circleShapeRadius, angle, tmp.x, tmp.y, animation)));
        screen.getShaker().shake(SHAKE_DURATION);
    }
}
