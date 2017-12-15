package uy.com.agm.gamethree.sprites.tileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/4/2017.
 */

public class Borders extends InteractiveTileObject {
    private static final String TAG = Borders.class.getName();

    public Borders(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
    }

    @Override
    public void onHit() {
        Gdx.app.debug(TAG, "Border collision");
    }
}
