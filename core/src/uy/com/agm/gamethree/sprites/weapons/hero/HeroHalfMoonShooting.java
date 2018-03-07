package uy.com.agm.gamethree.sprites.weapons.hero;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.actordef.ActorDefBullet;

/**
 * Created by amorales on 5/3/2018.
 */

public class HeroHalfMoonShooting implements IShootStrategy {
    private static final String TAG = HeroHalfMoonShooting.class.getName();

    private PlayScreen screen;
    private int numberBullets;
    private float openFireTime;
    private float fireDelay;

    public HeroHalfMoonShooting(PlayScreen screen, int numberBullets) {
        this.screen = screen;
        this.numberBullets = numberBullets;
        this.openFireTime = 0;
        this.fireDelay = GameSettings.getInstance().isManualShooting() ? Constants.POWERTHREE_MANUAL_FIRE_DELAY_SECONDS : Constants.POWERTHREE_AUTOMATIC_FIRE_DELAY_SECONDS;
    }

    @Override
    public void updateShoot(float dt) {
        openFireTime += dt;
        if (screen.getPlayer().isSilverBulletEnabled()) {
            fireDelay = Constants.SILVERBULLET_FIRE_DELAY_SECONDS;
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
        float directionDegrees = 180.0f / (numberBullets + 1);
        float angle;
        Hero hero = screen.getPlayer();

        for (int i = 1; i <= numberBullets; i++) {
            angle = directionDegrees * i;
            angle = (angle >= 90.0f) ? angle - 90.0f : 270.0f + angle;

            if (hero.isSilverBulletEnabled()) {
                if (hero.hasSilverBullets()) {
                    screen.getCreator().createGameThreeActor(new ActorDefBullet(x,
                            y + Constants.HEROBULLET_OFFSET_METERS,
                            Constants.SILVERBULLET_WIDTH_METERS,
                            Constants.SILVERBULLET_HEIGHT_METERS,
                            Constants.SILVERBULLET_CIRCLESHAPE_RADIUS_METERS,
                            angle,
                            Constants.SILVERBULLET_VELOCITY_X,
                            Constants.SILVERBULLET_VELOCITY_Y,
                            Assets.getInstance().getSilverBullet().getSilverBulletAnimation(),
                            HeroBullet.class));
                    // Sound FX
                    AudioManager.getInstance().play(Assets.getInstance().getSounds().getHeroShootSwish());
                    if (i == numberBullets) {
                        hero.decreaseSilverBullets();
                    }
                } else {
                    // Sound FX
                    AudioManager.getInstance().play(Assets.getInstance().getSounds().getHeroShootEmpty());
                }
            } else {
                if (screen.getHud().isPowerRunningOut()) {
                    screen.getCreator().createGameThreeActor(new ActorDefBullet(x,
                            y + Constants.HEROBULLET_OFFSET_METERS,
                            Constants.HEROBULLET_WIDTH_METERS,
                            Constants.HEROBULLET_HEIGHT_METERS,
                            Constants.HEROBULLET_CIRCLESHAPE_RADIUS_METERS,
                            angle,
                            Constants.POWERTHREE_BULLET_VELOCITY_X,
                            Constants.POWERTHREE_BULLET_VELOCITY_Y,
                            Assets.getInstance().getHeroBullet().getHeroBulletAnimation(),
                            HeroBullet.class));
                } else {
                    screen.getCreator().createGameThreeActor(new ActorDefBullet(x,
                            y + Constants.HEROBULLET_OFFSET_METERS,
                            Constants.POWERTHREE_BULLET_WIDTH_METERS,
                            Constants.POWERTHREE_BULLET_HEIGHT_METERS,
                            Constants.POWERTHREE_BULLET_CIRCLESHAPE_RADIUS_METERS,
                            angle,
                            Constants.POWERTHREE_BULLET_VELOCITY_X,
                            Constants.POWERTHREE_BULLET_VELOCITY_Y,
                            Assets.getInstance().getBulletA().getBulletAAnimation(),
                            HeroBullet.class));
                }
                // Sound FX
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getHeroShoot());
            }
        }
    }
}
