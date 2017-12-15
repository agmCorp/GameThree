package uy.com.agm.gamethree.sprites.tileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/4/2017.
 */

public class Obstacle extends InteractiveTileObject {
    private static final String TAG = Obstacle.class.getName();

    public Obstacle(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(GameThree.OBSTACLE_BIT);
    }

    @Override
    public void onHit() {
        Gdx.app.debug(TAG, "Obstacle collision");
    }
}
