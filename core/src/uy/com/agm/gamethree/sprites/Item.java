package uy.com.agm.gamethree.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.scenes.Hud;
import uy.com.agm.gamethree.screens.PlayScreen;

/**
 * Created by AGM on 12/4/2017.
 */

public class Item extends InteractiveTileObject {
    private static final String TAG = Item.class.getName();
    private Hud hud;

    public Item(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        this.hud = screen.getHud();
        fixture.setUserData(this);
        setCategoryFilter(GameThree.ITEM_BIT);
    }

    @Override
    public void onHit() {
        Gdx.app.debug(TAG, "Item collision");
        setCategoryFilter(GameThree.TAKEN_BIT);
        getCell().setTile(null);
        hud.addScore(200);
    }
}
