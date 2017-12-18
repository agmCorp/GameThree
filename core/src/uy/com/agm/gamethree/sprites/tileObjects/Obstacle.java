package uy.com.agm.gamethree.sprites.tileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by AGM on 12/4/2017.
 */

public class Obstacle extends InteractiveTileObject {
    private static final String TAG = Obstacle.class.getName();

    public Obstacle(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Constants.OBSTACLE_BIT);
    }

    @Override
    public void onHit() {
        Gdx.app.debug(TAG, "Obstacle collision");
    }
}
