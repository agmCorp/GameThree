package uy.com.agm.gamethree.actors.backgroundObjects.staticObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

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

        // The default value is 0xFFFF for maskBits, or in other words this fixture will collide
        // with every other fixture as long as the other fixture has this categoryBit in its maskBits list.
        Filter filter = new Filter();
        filter.categoryBits = WorldContactListener.BORDER_BIT; // Depicts what this fixture is
        fixture.setFilterData(filter);
        fixture.setUserData(this);
    }

    @Override
    public void onBump() {
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBump());
    }

    @Override
    protected Shape getShape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boundsMeters.getWidth() / 2, boundsMeters.getHeight() / 2);
        return shape;
    }
}
