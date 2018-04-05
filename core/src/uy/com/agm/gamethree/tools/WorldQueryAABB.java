package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

import java.util.concurrent.LinkedBlockingQueue;

import uy.com.agm.gamethree.sprites.boxes.PowerBox;
import uy.com.agm.gamethree.sprites.tileobjects.Obstacle;
import uy.com.agm.gamethree.sprites.tileobjects.Path;

/**
 * Created by AGM on 4/4/2018.
 */

public class WorldQueryAABB implements QueryCallback {
    private static final String TAG = WorldQueryAABB.class.getName();

    private LinkedBlockingQueue<Rectangle> foundBodies;

    // Singleton: unique instance
    private static WorldQueryAABB instance;

    // Singleton: prevent instantiation from other classes
    private WorldQueryAABB() {
        // Near collisions
        foundBodies = new LinkedBlockingQueue<Rectangle>();
    }

    // Singleton: retrieve instance
    public static WorldQueryAABB getInstance() {
        if (instance == null) {
            instance = new WorldQueryAABB();
        }
        return instance;
    }

    public LinkedBlockingQueue<Rectangle> getFoundBodies() {
        return foundBodies;
    }

    @Override
    public boolean reportFixture(Fixture fixture) {
        Object obj = fixture.getUserData();
        Gdx.app.debug(TAG, "******************** soy el reportFixture " + fixture.getFilterData().categoryBits);

        if (obj instanceof Obstacle) {
            Gdx.app.debug(TAG, "******************** OBSTACLE ");
            foundBodies.add(((Obstacle)obj).getBoundsMeters());
        } else {
            if (obj instanceof Path) {
                Gdx.app.debug(TAG, "******************** PATH ");
                foundBodies.add(((Path)obj).getBoundsMeters());
            } else {
                if (obj instanceof PowerBox) {
                    Gdx.app.debug(TAG, "******************** POWER ");
                    foundBodies.add(((PowerBox)obj).getBoundsMeters());
                }
            }
        }
        return true;
    }
}
