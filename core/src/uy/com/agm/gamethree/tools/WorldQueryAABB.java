package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 4/4/2018.
 */

public class WorldQueryAABB implements QueryCallback {
    private final String TAG = WorldQueryAABB.class.getName();

    private PlayScreen screen;
    private Array<Fixture> foundBodies;
    private Array<Short> categoryBits;

    public WorldQueryAABB(PlayScreen screen) {
        this.screen = screen;
        foundBodies = new Array<Fixture>();
    }

    public Array<Fixture> findBodies(Array<Short> categoryBits, float lowerX, float lowerY, float upperX, float upperY) {
        this.categoryBits = categoryBits;
        foundBodies.clear();
        screen.getWorld().QueryAABB(this, lowerX, lowerY, upperX, upperY);
        return foundBodies;
    }

    @Override
    public boolean reportFixture(Fixture fixture) {
        boolean add = categoryBits != null ? categoryBits.contains(fixture.getFilterData().categoryBits, false) : true;
        if (add) {
            foundBodies.add(fixture);
        }
        return true;
    }
}
