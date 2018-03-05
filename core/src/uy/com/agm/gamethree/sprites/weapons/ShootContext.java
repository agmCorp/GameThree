package uy.com.agm.gamethree.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by amorales on 5/3/2018.
 */

public class ShootContext {
    IShootStrategy shootStrategy;

    public ShootContext(IShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public void setStrategy(IShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public void shoot(float dt) {
        shootStrategy.shoot(dt);
    }
}