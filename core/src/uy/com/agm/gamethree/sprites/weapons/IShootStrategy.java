package uy.com.agm.gamethree.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by amorales on 5/3/2018.
 */

public interface IShootStrategy {
    public void shootEnhanced(float x, float y, float bulletWidth, float bulletHeight, float bulletCircleShapeRadius, Animation bulletAnimation, int numberBullets);
}
