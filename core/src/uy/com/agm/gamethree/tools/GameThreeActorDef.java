package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by AGM on 12/14/2017.
 */

public class GameThreeActorDef {
    private static final String TAG = GameThreeActorDef.class.getName();

    private Vector2 position;
    private Class<?> type;

    public GameThreeActorDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Class<?> getType() {
        return type;
    }
}
