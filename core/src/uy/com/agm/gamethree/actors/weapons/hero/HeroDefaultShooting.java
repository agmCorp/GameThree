package uy.com.agm.gamethree.actors.weapons.hero;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;

import uy.com.agm.gamethree.assets.Assets;
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

public class HeroDefaultShooting implements IShootStrategy {
    private static final String TAG = HeroDefaultShooting.class.getName();

    // HeroBullet (meters = pixels * resizeFactor / PPM)
    public static final float DEFAULT_BULLET_OFFSET_METERS = Hero.CIRCLE_SHAPE_RADIUS_METERS;
    private static final float DEFAULT_BULLET_VELOCITY_X = 0.0f;
    private static final float DEFAULT_BULLET_VELOCITY_Y = 6.0f;
    public static final float DEFAULT_BULLET_CIRCLE_SHAPE_RADIUS_METERS = 15.0f / PlayScreen.PPM;
    private static final float DEFAULT_BULLET_AUTOMATIC_FIRE_DELAY_SECONDS = 0.33f;
    private static final float DEFAULT_BULLET_MANUAL_FIRE_DELAY_SECONDS = 0.1f;
    private static final float RANDOM_OFFSET = 0.8f;

    // SilverBullet (meters = pixels * resizeFactor / PPM)
    public static final float SILVER_BULLET_VELOCITY_X = 0.0f;
    public static final float SILVER_BULLET_VELOCITY_Y = 6.0f;
    public static final float SILVER_BULLET_CIRCLE_SHAPE_RADIUS_METERS = 25.0f / PlayScreen.PPM;
    public static final float SILVER_BULLET_FIRE_DELAY_SECONDS = 1.0f;

    private PlayScreen screen;
    private float openFireTime;
    private float fireDelay;

    private Animation heroBulletAnimation;
    private Animation heroSilverBulletAnimation;

    public HeroDefaultShooting(PlayScreen screen) {
        this.screen = screen;
        this.openFireTime = 0;
        this.fireDelay = GameSettings.getInstance().isManualShooting() ? DEFAULT_BULLET_MANUAL_FIRE_DELAY_SECONDS : DEFAULT_BULLET_AUTOMATIC_FIRE_DELAY_SECONDS;

        // Animations
        heroBulletAnimation = Assets.getInstance().getHeroBullet().getHeroBulletAnimation();
        heroSilverBulletAnimation = Assets.getInstance().getSilverBullet().getSilverBulletAnimation();
    }

    @Override
    public void updateShoot(float dt) {
        openFireTime += dt;
        if (screen.getCreator().getHero().isSilverBulletEnabled()) {
            fireDelay = SILVER_BULLET_FIRE_DELAY_SECONDS;
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
        Hero hero = screen.getCreator().getHero();

        if (hero.isSilverBulletEnabled()) {
            if (hero.hasSilverBullets()) {
                hero.decreaseSilverBullets();
                screen.getCreator().createGameThreeActor(new ActorDef(new HeroBullet(screen, x,
                        y + DEFAULT_BULLET_OFFSET_METERS,
                        AssetSilverBullet.WIDTH_METERS,
                        AssetSilverBullet.HEIGHT_METERS,
                        SILVER_BULLET_CIRCLE_SHAPE_RADIUS_METERS,
                        0,
                        SILVER_BULLET_VELOCITY_X,
                        SILVER_BULLET_VELOCITY_Y,
                        heroSilverBulletAnimation)));
                // Sound FX
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHeroShootSwish());
            } else {
                // Sound FX
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHeroShootEmpty());
            }
        } else {
            screen.getCreator().createGameThreeActor(new ActorDef(new HeroBullet(screen, x,
                    y + DEFAULT_BULLET_OFFSET_METERS,
                    AssetHeroBullet.WIDTH_METERS,
                    AssetHeroBullet.HEIGHT_METERS,
                    DEFAULT_BULLET_CIRCLE_SHAPE_RADIUS_METERS,
                    0,
                    DEFAULT_BULLET_VELOCITY_X + (MathUtils.randomBoolean() ? MathUtils.randomSign() * RANDOM_OFFSET : 0), // Less accuracy is more fun
                    DEFAULT_BULLET_VELOCITY_Y + (MathUtils.randomBoolean() ? MathUtils.randomSign() * RANDOM_OFFSET : 0), // Less accuracy is more fun
                    heroBulletAnimation)));
            // Sound FX
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHeroShoot());
        }
    }
}
