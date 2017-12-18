package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by AGM on 12/14/2017.
 */

public class GameThreeActorDef {
    public Vector2 position;
    public Class<?> type;

    public GameThreeActorDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;

    }
}
