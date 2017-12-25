package uy.com.agm.gamethree.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 12/4/2017.
 */

public class Borders extends InteractiveTileObject {
    private static final String TAG = Borders.class.getName();

    public Borders(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
    }

    @Override
    public void onHit() {
        AudioManager.instance.play(Assets.instance.sounds.bump, 1, MathUtils.random(1.0f, 1.1f));
    }
}
