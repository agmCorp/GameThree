package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by amorales on 4/4/2018.
 */

public class WorldQueryCallback implements QueryCallback {
    private LinkedBlockingQueue<Fixture> foundBodies;

    public WorldQueryCallback() {
        foundBodies = new LinkedBlockingQueue<Fixture>();
    }
    
    @Override
    public boolean reportFixture(Fixture fixture) {
        foundBodies.add(fixture);
        return true;
    }
}
