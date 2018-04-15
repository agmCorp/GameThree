package uy.com.agm.gamethree.actors.backgroundObjects.staticObjects;

import com.badlogic.gdx.maps.MapObject;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/4/2017.
 */

public class Obstacle extends StaticBackgroundObject {
    private static final String TAG = Obstacle.class.getName();

    public Obstacle(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(WorldContactListener.OBSTACLE_BIT);
    }

    @Override
    public void onBump() {
        AudioManager.getInstance().play(Assets.getInstance().getSounds().getBump());
    }
}
