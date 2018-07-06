package uy.com.agm.gamethree.actors.weapons;

/**
 * Created by amorales on 5/3/2018.
 */

public interface IShootStrategy {
    public void updateShoot(float dt);
    public void shoot(float x, float y);
    public void setFireDelaySeconds(float initialOpenFireTime, float fireDelay);
}
