package uy.com.agm.gamethree.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by amorales on 5/3/2018.
 */

public class ShootContext {
    IShootStrategy shootStrategy;

    public void setStrategy(IShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public void shootEnhanced(float x, float y, float bulletWidth, float bulletHeight, float bulletCircleShapeRadius, Animation bulletAnimation, int numberBullets) {
        shootStrategy.shootEnhanced(x, y, bulletWidth, bulletHeight, bulletCircleShapeRadius, bulletAnimation, numberBullets);
    }
}