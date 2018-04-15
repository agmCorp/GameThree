package uy.com.agm.gamethree.actors.backgroundObjects.staticObjects;

import com.badlogic.gdx.maps.MapObject;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/4/2017.
 */

public class Border extends StaticBackgroundObject {
    private static final String TAG = Border.class.getName();

    /* We have eight borders (left1, left2, left3, left4, right1, right2, right3, right4). These borders
     * are near 2000 / PPM meters long (20 meters).
     * Box2D works with floating point numbers and tolerances have to be used to make Box2D perform well.
     * These tolerances have been tuned to work well with meters-kilogram-second (MKS) units. In particular,
     * Box2D has been tuned to work well with moving shapes between 0.1 and 10 meters.
     * Static shapes may be up to 50 meters long without trouble.
    */
    public Border(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(WorldContactListener.BORDER_BIT);
    }

    @Override
    public void onBump() {
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getBump());
    }
}
