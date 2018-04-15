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

public class Path extends StaticBackgroundObject {
    private static final String TAG = Path.class.getName();

    public Path(PlayScreen screen, MapObject object) {
        super(screen, object);
        // The default value is 0xFFFF for maskBits, or in other words this fixture will collide
        // with every other fixture as long as the other fixture has this categoryBit in its maskBits list.
        Filter filter = new Filter();
        filter.categoryBits = WorldContactListener.PATH_BIT; // Depicts what this fixture is
        fixture.setFilterData(filter);
        fixture.setUserData(this);
    }

    @Override
    public void onBump() {
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getBump());
    }

    @Override
    protected Shape getShape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boundsMeters.getWidth() / 2, boundsMeters.getHeight() / 2);
        return shape;
    }
}
