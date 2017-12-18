package uy.com.agm.gamethree.sprites.powerup.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by AGM on 12/14/2017.
 */

public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;

    }
}
