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

public class HeroDefaultShooting implements IShootStrategy {
    private static final String TAG = HeroDefaultShooting.class.getName();

    private PlayScreen screen;
    private float openFireTime;
    private float fireDelay;

    public HeroDefaultShooting(PlayScreen screen) {
        this.screen = screen;
        this.openFireTime = 0;
        this.fireDelay = GameSettings.getInstance().isManualShooting() ? Constants.HEROBULLET_MANUAL_FIRE_DELAY_SECONDS : Constants.HEROBULLET_AUTOMATIC_FIRE_DELAY_SECONDS;
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
        Hero hero = screen.getPlayer();

        if (hero.isSilverBulletEnabled()) {
            if (hero.hasSilverBullets()) {
                hero.decreaseSilverBullets();
                screen.getCreator().createGameThreeActor(new ActorDefBullet(x,
                        y + Constants.HEROBULLET_OFFSET_METERS,
                        Constants.SILVERBULLET_WIDTH_METERS,
                        Constants.SILVERBULLET_HEIGHT_METERS,
                        Constants.SILVERBULLET_CIRCLESHAPE_RADIUS_METERS,
                        0,
                        Constants.SILVERBULLET_VELOCITY_X,
                        Constants.SILVERBULLET_VELOCITY_Y,
                        Assets.getInstance().getSilverBullet().getSilverBulletAnimation(),
                        HeroBullet.class));
                // Sound FX
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getHeroShootSwish());
            } else {
                // Sound FX
                AudioManager.getInstance().play(Assets.getInstance().getSounds().getHeroShootEmpty());
            }
        } else {
            screen.getCreator().createGameThreeActor(new ActorDefBullet(x,
                    y + Constants.HEROBULLET_OFFSET_METERS,
                    Constants.HEROBULLET_WIDTH_METERS,
                    Constants.HEROBULLET_HEIGHT_METERS,
                    Constants.HEROBULLET_CIRCLESHAPE_RADIUS_METERS,
                    0,
                    Constants.HEROBULLET_VELOCITY_X,
                    Constants.HEROBULLET_VELOCITY_Y,
                    Assets.getInstance().getHeroBullet().getHeroBulletAnimation(),
                    HeroBullet.class));
            // Sound FX
            AudioManager.getInstance().play(Assets.getInstance().getSounds().getHeroShoot());
        }
    }
}
