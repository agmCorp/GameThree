package uy.com.agm.gamethree.tools.actordef;

/**
 * Created by AGM on 12/14/2017.
 */

public class ActorDef {
    private static final String TAG = ActorDef.class.getName();

    private float x;
    private float y;
    private Class<?> type;

    public ActorDef(float x, float y, Class<?> type) {
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
