package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by amorales on 28/12/2017.
 */

public class Vector2Util {
    private static final String TAG = Vector2Util.class.getName();

    /*
     * To go from origin to target at constant speed, we must:
     * - subtract their position vectors: target - origin.
     * - get the direction of the previous vector (normalization)
     * - apply constant velocity on that direction
     * This method affects origin to avoid gc
     */
    public static Vector2 goToTarget(Vector2 origin, float targetX, float targetY, float constantSpeed) {
        return origin.set(targetX - origin.x, targetY - origin.y).nor().scl(constantSpeed);
    }
}
