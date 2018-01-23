package uy.com.agm.gamethree.sprites.items;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

import uy.com.agm.gamethree.sprites.items.collectibles.ColOne;
import uy.com.agm.gamethree.sprites.items.powerups.PowerFour;
import uy.com.agm.gamethree.sprites.items.powerups.PowerOne;
import uy.com.agm.gamethree.sprites.items.powerups.PowerThree;
import uy.com.agm.gamethree.sprites.items.powerups.PowerTwo;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.GameThreeActorDef;

/**
 * Created by AGM on 1/7/2018.
 */

public class ItemCreator {
    public static void getItemOnHit(MapObject object, B2WorldCreator creator, float x, float y) {
        MapProperties mp = object.getProperties();
        if (mp.containsKey("colOne")) {
            creator.createGameThreeActor(new GameThreeActorDef(x, y, ColOne.class));
        }
        if (mp.containsKey("powerOne")) {
            creator.createGameThreeActor(new GameThreeActorDef(x, y, PowerOne.class));
        }
        if (mp.containsKey("powerTwo")) {
            creator.createGameThreeActor(new GameThreeActorDef(x, y, PowerTwo.class));
        }
        if (mp.containsKey("powerThree")) {
            creator.createGameThreeActor(new GameThreeActorDef(x, y, PowerThree.class));
        }
        if (mp.containsKey("powerFour")) {
            creator.createGameThreeActor(new GameThreeActorDef(x, y, PowerFour.class));
        }
    }
}
