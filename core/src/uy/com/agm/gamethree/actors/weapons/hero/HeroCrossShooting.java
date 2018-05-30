package uy.com.agm.gamethree.actors.weapons.hero;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.actors.player.Hero;
import uy.com.agm.gamethree.actors.weapons.IShootStrategy;
import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.sprites.AssetBulletA;
import uy.com.agm.gamethree.assets.sprites.AssetHeroBullet;
import uy.com.agm.gamethree.assets.sprites.AssetSilverBullet;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.ActorDef;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by amorales on 5/3/2018.
 */

public class HeroCrossShooting implements IShootStrategy {
    private static final String TAG = HeroCrossShooting.class.getName();

    // Constants (meters = pixels * resizeFactor / PPM)
    private static final float AUTOMATIC_FIRE_DELAY_SECONDS = 0.4f;
    private static final float MANUAL_FIRE_DELAY_SECONDS = 0.3f;
    private static final float BULLET_CIRCLE_SHAPE_RADIUS_METERS = 20.0f / PlayScreen.PPM;
    private static final int NUMBER_BULLETS = 4;
    private static final float BULLET_VELOCITY_X = 0.0f;
    private static final float BULLET_VELOCITY_Y = 4.0f;
    protected static final float SHAKE_DURATION = 0.8f;

    private PlayScreen screen;
    private float openFireTime;
    private float fireDelay;
    private Vector2 tmp; // Temporary GC friendly vector

    private Animation heroBulletAnimation;
    private Animation heroSilverBulletAnimation;
    private Animation bulletAnimation;

    public HeroCrossShooting(PlayScreen screen) {
        this.screen = screen;
        this.openFireTime = 0;
        this.fireDelay = GameSettings.getInstance().isManualShooting() ? MANUAL_FIRE_DELAY_SECONDS : AUTOMATIC_FIRE_DELAY_SECONDS;

        // Temporary GC friendly vector
        tmp = new Vector2();

        // Animations
        heroBulletAnimation = Assets.getInstance().getHeroBullet().getHeroBulletAnimation();
        heroSilverBulletAnimation = Assets.getInstance().getSilverBullet().getSilverBulletAnimation();
        bulletAnimation = Assets.getInstance().getBulletB().getBulletBAnimation();
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

    private void shootImp(float x, float y) {
        Hero hero = screen.getCreator().getHero();
        float angle;
        for(int i = 0; i < NUMBER_BULLETS; i++) {
            angle = 90 * i;

            if (hero.isSilverBulletEnabled()) {
                if (hero.hasSilverBullets()) {
                    createBullet(x, y,
                            AssetSilverBullet.WIDTH_METERS,
                            AssetSilverBullet.HEIGHT_METERS,
                            HeroDefaultShooting.SILVER_BULLET_CIRCLE_SHAPE_RADIUS_METERS,
                            angle,
                            HeroDefaultShooting.SILVER_BULLET_VELOCITY_X,
                            HeroDefaultShooting.SILVER_BULLET_VELOCITY_Y,
                            heroSilverBulletAnimation);
                    // Sound FX
                    AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHeroShootSwish());
                    if (i == NUMBER_BULLETS - 1) {
                        hero.decreaseSilverBullets();
                    }
                } else {
                    // Sound FX
                    AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHeroShootEmpty());
                }
            } else {
                if (screen.getHud().isWeaponPowerRunningOut()) {
                    createBullet(x, y,
                            AssetHeroBullet.WIDTH_METERS,
                            AssetHeroBullet.HEIGHT_METERS,
                            HeroDefaultShooting.DEFAULT_BULLET_CIRCLE_SHAPE_RADIUS_METERS,
                            angle,
                            BULLET_VELOCITY_X,
                            BULLET_VELOCITY_Y,
                            heroBulletAnimation);
                } else {
                    createBullet(x, y,
                            AssetBulletA.BULLET_WIDTH_METERS,
                            AssetBulletA.BULLET_HEIGHT_METERS,
                            BULLET_CIRCLE_SHAPE_RADIUS_METERS,
                            angle,
                            BULLET_VELOCITY_X,
                            BULLET_VELOCITY_Y,
                            bulletAnimation);
                }
                // Sound FX
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBlade());
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
