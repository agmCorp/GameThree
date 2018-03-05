package uy.com.agm.gamethree.sprites.weapons.hero;

import com.badlogic.gdx.graphics.g2d.Animation;

import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.IShootStrategy;
import uy.com.agm.gamethree.tools.actordef.ActorDefBullet;

/**
 * Created by amorales on 5/3/2018.
 */

public class HeroHalfMoonShooting implements IShootStrategy {
    private PlayScreen screen;

    public HeroHalfMoonShooting(PlayScreen screen) {
        this.screen = screen;
    }

    @Override
    public void shootEnhanced(float x, float y, float bulletWidth, float bulletHeight, float bulletCircleShapeRadius, Animation bulletAnimation, int numberBullets) {
        float directionDegrees = 180.0f / (numberBullets + 1);
        float angle;
        for (int i = 1; i <= numberBullets; i++) {
            angle = directionDegrees * i;
            angle = (angle >= 90.0f) ? angle - 90.0f : 270.0f + angle;

            if (screen.getHud().isPowerRunningOut() && !screen.getPlayer().isSilverBulletEnabled()) {
                screen.getCreator().createGameThreeActor(new ActorDefBullet(x,
                        y + Constants.HEROBULLET_OFFSET_METERS, angle, HeroBullet.class));
            } else {
                screen.getCreator().createGameThreeActor(new ActorDefBullet(x,
                        y + Constants.HEROBULLET_OFFSET_METERS,
                        bulletWidth,
                        bulletHeight,
                        bulletCircleShapeRadius,
                        angle,
                        bulletAnimation,
                        HeroBullet.class));
            }
        }
    }
}
