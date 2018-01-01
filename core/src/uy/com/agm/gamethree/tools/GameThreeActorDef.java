package uy.com.agm.gamethree.tools;

/**
 * Created by AGM on 12/14/2017.
 */

public class GameThreeActorDef {
    private static final String TAG = GameThreeActorDef.class.getName();

    private float x;
    private float y;
    private Class<?> type;

    public GameThreeActorDef(float x, float y, Class<?> type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Class<?> getType() {
        return type;
    }
}
