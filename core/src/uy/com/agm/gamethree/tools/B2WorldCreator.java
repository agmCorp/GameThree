package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
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
import uy.com.agm.gamethree.sprites.tileobjects.Path;
import uy.com.agm.gamethree.sprites.weapons.EnemyBullet;
import uy.com.agm.gamethree.sprites.weapons.HeroBullet;
import uy.com.agm.gamethree.sprites.weapons.Weapon;
import uy.com.agm.gamethree.tools.actordef.ActorDef;
import uy.com.agm.gamethree.tools.actordef.ActorDefBullet;
import uy.com.agm.gamethree.tools.actordef.ActorDefPower;

/**
 * Created by AGM on 12/4/2017.
 */

public class B2WorldCreator {
    private static final String TAG = B2WorldCreator.class.getName();

    // Constants
    private static final String LAYER_BORDER = "border";
    private static final String LAYER_OBSTACLE = "obstacle";
    private static final String LAYER_PATH = "path";
    private static final String LAYER_ENEMYONE = "enemyOne";
    private static final String LAYER_ENEMYTWO = "enemyTwo";
    private static final String LAYER_ENEMYTHREE = "enemyThree";
    private static final String LAYER_ENEMYFOUR = "enemyFour";
    private static final String LAYER_POWERBOX = "powerBox";
    private static final String KEY_POWERONE = "powerOne";
    private static final String KEY_POWERTWO = "powerTwo";
    private static final String KEY_POWERTHREE = "powerThree";
    private static final String KEY_POWERFOUR = "powerFour";
    private static final String KEY_TIMER = "timer";
    private static final String KEY_COLONE = "colOne";
    private static final String KEY_COLSILVERBULLET = "colSilverBullet";

    private PlayScreen screen;
    private Array<Enemy> enemies;
    private Array<PowerBox> powerBoxes;
    private Array<Item> items;
    private Array<Weapon> weapons;
    private LinkedBlockingQueue<ActorDef> actorsToCreate;

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
        actorsToCreate = new LinkedBlockingQueue<ActorDef>();

        TiledMap map = screen.getMap();

        // Layer: border
        layer = map.getLayers().get(LAYER_BORDER);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                new Borders(screen, object);
            }
        }

        // Layer: obstacle
        layer = map.getLayers().get(LAYER_OBSTACLE);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                new Obstacle(screen, object);
            }
        }

        // Layer: path
        layer = map.getLayers().get(LAYER_PATH);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                new Path(screen, object);
            }
        }

        // Layer: enemyOne
        layer = map.getLayers().get(LAYER_ENEMYONE);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyOne(screen, object));
            }
        }

        // Layer: enemyTwo
        layer = map.getLayers().get(LAYER_ENEMYTWO);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyTwo(screen, object));
            }
        }

        // Layer: enemyThree
        layer = map.getLayers().get(LAYER_ENEMYTHREE);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyThree(screen, object));
            }
        }

        // Layer: enemyFour
        layer = map.getLayers().get(LAYER_ENEMYFOUR);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyFour(screen, object));
            }
        }

        // Layer: powerBoxes
        layer = map.getLayers().get(LAYER_POWERBOX);
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

    public void createGameThreeActor(ActorDef actorDef) {
        actorsToCreate.add(actorDef);
    }

    public void handleCreatingActors() {
        if (!actorsToCreate.isEmpty()) {
            ActorDef actorDef = actorsToCreate.poll(); // Similar to pop but for a queue, removes the element

            if (actorDef.getType() == ColOne.class) {
                items.add(new ColOne(screen, actorDef.getX(), actorDef.getY()));
            }
            if (actorDef.getType() == ColSilverBullet.class) {
                items.add(new ColSilverBullet(screen, actorDef.getX(), actorDef.getY()));
            }
            if (actorDef.getType() == PowerOne.class) {
                items.add(new PowerOne(screen, actorDef.getX(),
                        actorDef.getY(), ((ActorDefPower) actorDef).getTimer()));
            }
            if (actorDef.getType() == PowerTwo.class) {
                items.add(new PowerTwo(screen, actorDef.getX(),
                        actorDef.getY(), ((ActorDefPower) actorDef).getTimer()));
            }
            if (actorDef.getType() == PowerThree.class) {
                items.add(new PowerThree(screen, actorDef.getX(),
                        actorDef.getY(), ((ActorDefPower) actorDef).getTimer()));
            }
            if (actorDef.getType() == PowerFour.class) {
                items.add(new PowerFour(screen, actorDef.getX(),
                        actorDef.getY(), ((ActorDefPower) actorDef).getTimer()));
            }
            if (actorDef.getType() == HeroBullet.class) {
                weapons.add(new HeroBullet(screen, actorDef.getX(), actorDef.getY(),
                        ((ActorDefBullet) actorDef).getWidth(),
                        ((ActorDefBullet) actorDef).getHeight(),
                        ((ActorDefBullet) actorDef).getCircleShapeRadius(),
                        ((ActorDefBullet) actorDef).getAngle(),
                        ((ActorDefBullet) actorDef).getAnimation()));
            }
            if (actorDef.getType() == EnemyBullet.class) {
                weapons.add(new EnemyBullet(screen, actorDef.getX(), actorDef.getY()));
            }
        }
    }

    public void getItemOnHit(MapObject object, float x, float y) {
        MapProperties mp = object.getProperties();

        int timer = object.getProperties().get(KEY_TIMER, 0, Integer.class);
        if (mp.containsKey(KEY_COLONE)) {
            createGameThreeActor(new ActorDef(x, y, ColOne.class));
        }
        if (mp.containsKey(KEY_COLSILVERBULLET)) {
            createGameThreeActor(new ActorDef(x, y, ColSilverBullet.class));
        }
        if (mp.containsKey(KEY_POWERONE)) {
            createGameThreeActor(new ActorDefPower(x, y, timer, PowerOne.class));
        }
        if (mp.containsKey(KEY_POWERTWO)) {
            createGameThreeActor(new ActorDefPower(x, y, timer, PowerTwo.class));
        }
        if (mp.containsKey(KEY_POWERTHREE)) {
            createGameThreeActor(new ActorDefPower(x, y, timer, PowerThree.class));
        }
        if (mp.containsKey(KEY_POWERFOUR)) {
            createGameThreeActor(new ActorDefPower(x, y, timer, PowerFour.class));
        }
    }
}
