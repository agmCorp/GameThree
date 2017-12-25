package uy.com.agm.gamethree.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
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
        AudioManager.instance.play(Assets.instance.sounds.bump, 1, MathUtils.random(1.0f, 1.1f));
    }
}
