package uy.com.agm.gamethree.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 12/4/2017.
 */

public class Borders extends InteractiveTileObject {
    private static final String TAG = Borders.class.getName();

    public Borders(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Constants.BORDERS_BIT);
    }

    @Override
    public void onBump() {
        AudioManager.instance.play(Assets.getInstance().getSounds().getBump());
    }
}
