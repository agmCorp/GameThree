package uy.com.agm.gamethree.sprites.tileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.scenes.Hud;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.Items.ItemDef;
import uy.com.agm.gamethree.sprites.Items.PowerOne;
import uy.com.agm.gamethree.tools.Assets;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 12/4/2017.
 */

public class CoinBox extends InteractiveTileObject {
    private static final String TAG = CoinBox.class.getName();
    private Hud hud;

    public CoinBox(PlayScreen screen, MapObject object) {
        super(screen, object);
        this.hud = screen.getHud();
        fixture.setUserData(this);
        setCategoryFilter(GameThree.COINBOX_BIT);
    }

    @Override
    public void onHit() {
        Gdx.app.debug(TAG, "CoinBox collision");
        setCategoryFilter(GameThree.COINBOXOPENED_BIT);

        float MARGEN = 32; // TODO ARREGLAR ESTO

        if(object.getProperties().containsKey("powerOne")) {
            screen.createItem(new ItemDef(new Vector2(b2body.getPosition().x, b2body.getPosition().y + MARGEN / GameThree.PPM), PowerOne.class));
        }
        AudioManager.instance.play(Assets.instance.sounds.openCoinBox, 1, MathUtils.random(1.0f, 1.1f));
        getCell().setTile(null);
        hud.addScore(200);
    }
}
