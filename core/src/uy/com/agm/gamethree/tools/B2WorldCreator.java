package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;

import java.util.concurrent.LinkedBlockingQueue;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.boxes.PowerBox;
import uy.com.agm.gamethree.sprites.enemies.Enemy;
import uy.com.agm.gamethree.sprites.enemies.EnemyFour;
import uy.com.agm.gamethree.sprites.enemies.EnemyOne;
import uy.com.agm.gamethree.sprites.enemies.EnemyThree;
import uy.com.agm.gamethree.sprites.enemies.EnemyTwo;
import uy.com.agm.gamethree.sprites.items.Item;
import uy.com.agm.gamethree.sprites.items.collectibles.ColOne;
import uy.com.agm.gamethree.sprites.items.collectibles.ColSilverBullet;
import uy.com.agm.gamethree.sprites.items.powerups.PowerFour;
import uy.com.agm.gamethree.sprites.items.powerups.PowerOne;
import uy.com.agm.gamethree.sprites.items.powerups.PowerThree;
import uy.com.agm.gamethree.sprites.items.powerups.PowerTwo;
import uy.com.agm.gamethree.sprites.tileobjects.Borders;
import uy.com.agm.gamethree.sprites.tileobjects.Obstacle;
import uy.com.agm.gamethree.sprites.weapons.EnemyBullet;
import uy.com.agm.gamethree.sprites.weapons.HeroBullet;
import uy.com.agm.gamethree.sprites.weapons.Weapon;

/**
 * Created by AGM on 12/4/2017.
 */

public class B2WorldCreator {
    private static final String TAG = B2WorldCreator.class.getName();

    private PlayScreen screen;
    private Array<Enemy> enemies;
    private Array<PowerBox> powerBoxes;
    private Array<Item> items;
    private Array<Weapon> weapons;
    private LinkedBlockingQueue<GameThreeActorDef> gameThreeActorsToCreate;

    public B2WorldCreator(PlayScreen screen) {
        MapLayer layer;
        this.screen = screen;

        // Enemies
        enemies = new Array<Enemy>();

        // PowerBoxes
        powerBoxes = new Array<PowerBox>();

        // Items
        items = new Array<Item>();

        // Weapons
        weapons = new Array<Weapon>();

        // Queue
        gameThreeActorsToCreate = new LinkedBlockingQueue<GameThreeActorDef>();

        TiledMap map = screen.getMap();

        // Layer: borders
        layer = map.getLayers().get("borders");
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                new Borders(screen, object);
            }
        }

        // Layer: obstacles
        layer = map.getLayers().get("obstacles");
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                new Obstacle(screen, object);
            }
        }

        // Layer: enemyOne
        layer = map.getLayers().get("enemyOne");
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyOne(screen, object));
            }
        }

        // Layer: enemyTwo
        layer = map.getLayers().get("enemyTwo");
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyTwo(screen, object));
            }
        }

        // Layer: enemyThree
        layer = map.getLayers().get("enemyThree");
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyThree(screen, object));
            }
        }

        // Layer: enemyFour
        layer = map.getLayers().get("enemyFour");
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyFour(screen, object));
            }
        }

        // Layer: powerBoxes
        layer = map.getLayers().get("powerBox");
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                powerBoxes.add(new PowerBox(screen, object));
            }
        }
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public void removeEnemy(Enemy enemy) {
        enemies.removeValue(enemy, true);
    }

    public Array<PowerBox> getPowerBoxes() {
        return powerBoxes;
    }

    public void removePowerBox(PowerBox powerBox) {
        powerBoxes.removeValue(powerBox, true);
    }

    public Array<Item> getItems() {
        return items;
    }

    public void removeItem(Item item) {
        items.removeValue(item, true);
    }

    public Array<Weapon> getWeapons() {
        return weapons;
    }

    public void removeWeapon(Weapon weapon) {
        weapons.removeValue(weapon, true);
    }

    public void createGameThreeActor(GameThreeActorDef gameThreeActorDef) {
        gameThreeActorsToCreate.add(gameThreeActorDef);
    }

    public void handleCreatingGameThreeActors() {
        if (!gameThreeActorsToCreate.isEmpty()) {
            GameThreeActorDef gameThreeActorDef = gameThreeActorsToCreate.poll(); // Similar to pop but for a queue, removes the element

            if (gameThreeActorDef.getType() == ColOne.class) {
                items.add(new ColOne(screen, gameThreeActorDef.getX(), gameThreeActorDef.getY()));
            }
            if (gameThreeActorDef.getType() == ColSilverBullet.class) {
                items.add(new ColSilverBullet(screen, gameThreeActorDef.getX(), gameThreeActorDef.getY()));
            }
            if (gameThreeActorDef.getType() == PowerOne.class) {
                items.add(new PowerOne(screen, gameThreeActorDef.getX(), gameThreeActorDef.getY()));
            }
            if (gameThreeActorDef.getType() == PowerTwo.class) {
                items.add(new PowerTwo(screen, gameThreeActorDef.getX(), gameThreeActorDef.getY()));
            }
            if (gameThreeActorDef.getType() == PowerThree.class) {
                items.add(new PowerThree(screen, gameThreeActorDef.getX(), gameThreeActorDef.getY()));
            }
            if (gameThreeActorDef.getType() == PowerFour.class) {
                items.add(new PowerFour(screen, gameThreeActorDef.getX(), gameThreeActorDef.getY()));
            }
            if (gameThreeActorDef.getType() == HeroBullet.class) {
                weapons.add(new HeroBullet(screen, gameThreeActorDef.getX(), gameThreeActorDef.getY(),
                                gameThreeActorDef.getWidth(), gameThreeActorDef.getHeight(),
                                gameThreeActorDef.getCircleShapeRadius(),
                                gameThreeActorDef.getAngle(), gameThreeActorDef.getAnimation()));
            }
            if (gameThreeActorDef.getType() == EnemyBullet.class) {
                weapons.add(new EnemyBullet(screen, gameThreeActorDef.getX(), gameThreeActorDef.getY()));
            }
        }
    }
}
