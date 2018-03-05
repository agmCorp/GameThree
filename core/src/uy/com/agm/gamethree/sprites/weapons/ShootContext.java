package uy.com.agm.gamethree.sprites.weapons;

import uy.com.agm.gamethree.sprites.weapons.hero.HeroHalfMoonShooting;

/**
 * Created by amorales on 5/3/2018.
 */

public class ShootContext {
    private static final String TAG = HeroHalfMoonShooting.class.getName();

    private IShootStrategy shootStrategy;

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