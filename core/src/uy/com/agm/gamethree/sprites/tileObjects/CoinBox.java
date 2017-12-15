package uy.com.agm.gamethree.sprites.tileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.scenes.Hud;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.Items.ItemDef;
import uy.com.agm.gamethree.sprites.Items.PowerOne;

/**
 * Created by AGM on 12/4/2017.
 */

public class CoinBox extends InteractiveTileObject {
    private static final String TAG = CoinBox.class.getName();
    private Hud hud;

    public CoinBox(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        this.hud = screen.getHud();
        fixture.setUserData(this);
        setCategoryFilter(GameThree.COINBOX_BIT);
    }

    @Override
    public void onHit() {
        Gdx.app.debug(TAG, "CoinBox collision");
        setCategoryFilter(GameThree.COINBOXOPENED_BIT);

        float MARGEN = 32; // TODO ARREGLAR ESTO
        screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + MARGEN / GameThree.PPM), PowerOne.class));
        getCell().setTile(null);
        hud.addScore(200);
    }
}
