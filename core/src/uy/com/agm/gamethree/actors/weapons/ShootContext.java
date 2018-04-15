package uy.com.agm.gamethree.actors.weapons;

/**
 * Created by amorales on 5/3/2018.
 */

public class ShootContext {
    private static final String TAG = ShootContext.class.getName();

    private IShootStrategy shootStrategy;

    public ShootContext(IShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public void setStrategy(IShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public void update(float dt) {
        shootStrategy.updateShoot(dt);
    }

    public void shoot(float x, float y) {
        shootStrategy.shoot(x, y);
    }
}