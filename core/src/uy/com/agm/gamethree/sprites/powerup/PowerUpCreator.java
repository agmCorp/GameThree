package uy.com.agm.gamethree.sprites.powerup;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

import uy.com.agm.gamethree.sprites.powerup.Items.PowerOne;
import uy.com.agm.gamethree.sprites.powerup.Items.PowerThree;
import uy.com.agm.gamethree.sprites.powerup.Items.PowerTwo;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.GameThreeActorDef;

/**
 * Created by AGM on 1/7/2018.
 */

public class PowerUpCreator {
    public static void getItemOnHit(MapObject object, B2WorldCreator creator, float x, float y) {
        MapProperties mp = object.getProperties();
        if (mp.containsKey("powerOne")) {
            creator.createGameThreeActor(new GameThreeActorDef(x, y, PowerOne.class));
        }
        if (mp.containsKey("powerTwo")) {
            creator.createGameThreeActor(new GameThreeActorDef(x, y, PowerTwo.class));
        }
        if (mp.containsKey("powerThree")) {
            creator.createGameThreeActor(new GameThreeActorDef(x, y, PowerThree.class));
        }
    }
}
