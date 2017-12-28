package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by amorales on 28/12/2017.
 */

public class Vector2Util {
    /*
     * To go from origin to target at constant speed, we must:
      * - subtract their position vectors: target - origin.
      * - get the direction of the previous vector (normalization)
      * - apply constant velocity on that direction
     */
    public static Vector2 goToTarget(Vector2 origin, Vector2 target, float constantSpeed) {
        return target.cpy().sub(origin).nor().scl(constantSpeed);
    }
}
