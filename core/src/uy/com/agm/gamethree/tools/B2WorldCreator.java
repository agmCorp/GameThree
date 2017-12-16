package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.scenes.Hud;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.enemies.Enemy;
import uy.com.agm.gamethree.sprites.tileObjects.Borders;
import uy.com.agm.gamethree.sprites.enemies.EnemyOne;
import uy.com.agm.gamethree.sprites.tileObjects.CoinBox;
import uy.com.agm.gamethree.sprites.tileObjects.Obstacle;

/**
 * Created by AGM on 12/4/2017.
 */

public class B2WorldCreator {
    //modificado
    private static final String TAG = B2WorldCreator.class.getName();

    private Array<Enemy> enemies;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        Hud hud = screen.getHud();

        // Layer: borders
        for(MapObject object : map.getLayers().get("borders").getObjects().getByType(RectangleMapObject.class)) {
            new Borders(screen, object);
        }

        // Layer: obstacles
        for(MapObject object : map.getLayers().get("obstacles").getObjects().getByType(RectangleMapObject.class)) {
            new Obstacle(screen, object);
        }

        // Layer: coinboxes
        for(MapObject object : map.getLayers().get("coinbox").getObjects().getByType(RectangleMapObject.class)) {
            new CoinBox(screen, object);
        }

        // Layer: enemyOne
        enemies = new Array<Enemy>();
        for(MapObject object : map.getLayers().get("enemyOne").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            Gdx.app.debug(TAG, "** TAMANO B2WORLDCREATOR X, Y " + rect.getX() / GameThree.PPM + " " + rect.getY() / GameThree.PPM );
            enemies.add(new EnemyOne(screen, rect.getX() / GameThree.PPM, rect.getY() / GameThree.PPM));
        }
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }
}
