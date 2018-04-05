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

    // Collisions
    private static LinkedBlockingQueue<Rectangle> foundBodies;

    // Singleton: unique instance
    private static WorldQueryAABB instance;

    // Singleton: prevent instantiation from other classes
    private WorldQueryAABB() {
        foundBodies = new LinkedBlockingQueue<Rectangle>();
    }

    // Singleton: retrieve instance
    public static WorldQueryAABB getInstance() {
        if (instance == null) {
            instance = new WorldQueryAABB();
        }
        return instance;
    }

    public static LinkedBlockingQueue<Rectangle> getFoundBodies() {
        return foundBodies;
    }

    @Override
    public boolean reportFixture(Fixture fixture) {
        Object obj = fixture.getUserData();
        switch (fixture.getFilterData().categoryBits) {
            case WorldContactListener.POWER_BOX_BIT:
                Gdx.app.debug(TAG, "AABB******************** POWERBOX ");
                foundBodies.add(((PowerBox) obj).getBoundsMeters());
                break;
            case WorldContactListener.OBSTACLE_BIT:
                Gdx.app.debug(TAG, "AABB******************* OBSTACLE ");
                foundBodies.add(((Obstacle) obj).getBoundsMeters());
                break;
            case WorldContactListener.PATH_BIT:
                Gdx.app.debug(TAG, "AABB******************** PATH ");
                foundBodies.add(((Path) obj).getBoundsMeters());
                break;
        }
        return true;
    }
}
