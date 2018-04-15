package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.concurrent.LinkedBlockingQueue;

import uy.com.agm.gamethree.assets.sprites.AssetEnemySeven;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.actors.backgroundObjects.kinematicObjects.Edge;
import uy.com.agm.gamethree.actors.backgroundObjects.kinematicObjects.Bridge;
import uy.com.agm.gamethree.actors.backgroundObjects.staticObjects.PowerBox;
import uy.com.agm.gamethree.actors.enemies.Enemy;
import uy.com.agm.gamethree.actors.enemies.EnemyEight;
import uy.com.agm.gamethree.actors.enemies.EnemyFive;
import uy.com.agm.gamethree.actors.enemies.EnemyFour;
import uy.com.agm.gamethree.actors.enemies.EnemyOne;
import uy.com.agm.gamethree.actors.enemies.EnemySeven;
import uy.com.agm.gamethree.actors.enemies.EnemySix;
import uy.com.agm.gamethree.actors.enemies.EnemyThree;
import uy.com.agm.gamethree.actors.enemies.EnemyTwo;
import uy.com.agm.gamethree.actors.items.Item;
import uy.com.agm.gamethree.actors.items.collectibles.ColOne;
import uy.com.agm.gamethree.actors.items.collectibles.ColSilverBullet;
import uy.com.agm.gamethree.actors.items.powerups.PowerFour;
import uy.com.agm.gamethree.actors.items.powerups.PowerOne;
import uy.com.agm.gamethree.actors.items.powerups.PowerThree;
import uy.com.agm.gamethree.actors.items.powerups.PowerTwo;
import uy.com.agm.gamethree.actors.backgroundObjects.staticObjects.Border;
import uy.com.agm.gamethree.actors.backgroundObjects.staticObjects.Obstacle;
import uy.com.agm.gamethree.actors.backgroundObjects.staticObjects.Path;
import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.actors.weapons.enemy.EnemyBullet;
import uy.com.agm.gamethree.actors.weapons.hero.HeroBullet;

/**
 * Created by AGM on 12/4/2017.
 */

public class B2WorldCreator {
    private static final String TAG = B2WorldCreator.class.getName();

    // Constants
    private static final String LAYER_BORDER = "border";
    private static final String LAYER_OBSTACLE = "obstacle";
    private static final String LAYER_PATH = "path";
    private static final String LAYER_BRIDGE = "bridge";
    private static final String LAYER_ENEMY_ONE = "enemyOne";
    private static final String LAYER_ENEMY_TWO = "enemyTwo";
    private static final String LAYER_ENEMY_THREE = "enemyThree";
    private static final String LAYER_ENEMY_FOUR = "enemyFour";
    private static final String LAYER_ENEMY_FIVE = "enemyFive";
    private static final String LAYER_ENEMY_SIX = "enemySix";
    private static final String LAYER_ENEMY_SEVEN = "enemySeven";
    private static final String LAYER_ENEMY_EIGHT = "enemyEight";
    private static final String LAYER_POWER_BOX = "powerBox";

    public static final String KEY_ID = "id";
    public static final String KEY_POWER_ONE = "powerOne";
    public static final String KEY_POWER_TWO = "powerTwo";
    public static final String KEY_POWER_THREE = "powerThree";
    public static final String KEY_POWER_FOUR = "powerFour";
    public static final String KEY_COL_ONE = "colOne";
    public static final String KEY_COL_SILVER_BULLET = "colSilverBullet";
    public static final String KEY_STRENGTH = "strength";
    public static final String KEY_TIMES_IT_FREEZE = "timesItFreeze";
    public static final String KEY_ENEMY_BULLET = "enemyBullet";
    public static final String KEY_ENEMY_SEVEN = "enemySeven";

    private PlayScreen screen;
    private Array<Border> borders;
    private Array<Edge> edges;
    private Edge upperEdge;
    private Edge bottomEdge;
    private Array<Obstacle> obstacles;
    private Array<Path> paths;
    private Array<Bridge> bridges;
    private Array<Enemy> enemies;
    private Array<PowerBox> powerBoxes;
    private Array<Item> items;
    private Array<Weapon> weapons;

    private LinkedBlockingQueue<ActorDef> actorsToCreate;
    private ArrayMap<String, String> arrayMapDebug;

    public B2WorldCreator(PlayScreen screen) {
        MapLayer layer;
        this.screen = screen;

        borders = new Array<Border>();
        edges = new Array<Edge>();
        obstacles = new Array<Obstacle>();
        paths = new Array<Path>();
        bridges = new Array<Bridge>();
        enemies = new Array<Enemy>();
        powerBoxes = new Array<PowerBox>();
        items = new Array<Item>();
        weapons = new Array<Weapon>();

        // Queue
        actorsToCreate = new LinkedBlockingQueue<ActorDef>();

        // Debug
        arrayMapDebug = new ArrayMap<String, String>();

        TiledMap map = screen.getMap();

        // Layer: border
        layer = map.getLayers().get(LAYER_BORDER);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                borders.add(new Border(screen, object));
            }
        }

        // Edges
        upperEdge = new Edge(screen, true);
        bottomEdge = new Edge(screen, false);
        edges.add(upperEdge);
        edges.add(bottomEdge);

        // Layer: obstacle
        layer = map.getLayers().get(LAYER_OBSTACLE);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                obstacles.add(new Obstacle(screen, object));
            }
        }

        // Layer: path
        layer = map.getLayers().get(LAYER_PATH);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                paths.add(new Path(screen, object));
            }
        }

        // Layer: bridge
        layer = map.getLayers().get(LAYER_BRIDGE);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                bridges.add(new Bridge(screen, object));
            }
        }

        // Layer: enemyOne
        layer = map.getLayers().get(LAYER_ENEMY_ONE);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyOne(screen, object));
            }
        }

        // Layer: enemyTwo
        layer = map.getLayers().get(LAYER_ENEMY_TWO);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyTwo(screen, object));
            }
        }

        // Layer: enemyThree
        layer = map.getLayers().get(LAYER_ENEMY_THREE);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyThree(screen, object));
            }
        }

        // Layer: enemyFour
        layer = map.getLayers().get(LAYER_ENEMY_FOUR);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyFour(screen, object));
            }
        }

        // Layer: enemyFive
        layer = map.getLayers().get(LAYER_ENEMY_FIVE);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyFive(screen, object));
            }
        }

        // Layer: enemySix
        layer = map.getLayers().get(LAYER_ENEMY_SIX);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemySix(screen, object));
            }
        }

        // Layer: enemySeven
        layer = map.getLayers().get(LAYER_ENEMY_SEVEN);
        if (layer != null) {
            MapProperties mp;
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                mp = object.getProperties();
                if (!mp.containsKey(B2WorldCreator.KEY_ENEMY_SEVEN)) {
                    mp.put(B2WorldCreator.KEY_ENEMY_SEVEN, "");
                }
                enemies.add(new EnemySeven(screen, object));
            }
        }

        // Layer: enemyEight
        layer = map.getLayers().get(LAYER_ENEMY_EIGHT);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new EnemyEight(screen, object));
            }
        }

        // Layer: powerBoxes
        layer = map.getLayers().get(LAYER_POWER_BOX);
        if (layer != null) {
            for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                powerBoxes.add(new PowerBox(screen, object));
            }
        }
    }

    public Array<Border> getBorders() {
        return borders;
    }

    public Array<Edge> getEdges() {
        return edges;
    }

    public Edge getUpperEdge() {
        return upperEdge;
    }

    public Edge getBottomEdge() {
        return bottomEdge;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public Array<Path> getPaths() {
        return paths;
    }

    public Array<Bridge> getBridges() {
        return bridges;
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public Array<PowerBox> getPowerBoxes() {
        return powerBoxes;
    }

    public Array<Item> getItems() {
        return items;
    }

    public Array<Weapon> getWeapons() {
        return weapons;
    }

    public void createGameThreeActor(ActorDef actorDef) {
        actorsToCreate.add(actorDef);
    }

    public void handleCreatingActors() {
        if (!actorsToCreate.isEmpty()) {
            ActorDef actorDef = actorsToCreate.poll(); // Similar to pop but for a queue, removes the element
            Object actor = actorDef.getUserData();

            if (actor instanceof EnemySeven) {
                enemies.add((EnemySeven) actor);
            }

            if (actor instanceof ColOne) {
                items.add((ColOne) actor);
            }
            if (actor instanceof ColSilverBullet) {
                items.add((ColSilverBullet) actor);
            }
            if (actor instanceof PowerOne) {
                items.add((PowerOne) actor);
            }
            if (actor instanceof PowerTwo) {
                items.add((PowerTwo) actor);
            }
            if (actor instanceof PowerThree) {
                items.add((PowerThree) actor);
            }
            if (actor instanceof PowerFour) {
                items.add((PowerFour) actor);
            }
            if (actor instanceof HeroBullet) {
                weapons.add((HeroBullet) actor);
            }
            if (actor instanceof EnemyBullet) {
                weapons.add((EnemyBullet) actor);
            }
        }
    }

    public void getItemOnHit(MapObject object, float x, float y) {
        MapProperties mp = object.getProperties();
        int timer;

        if (mp.containsKey(KEY_ENEMY_SEVEN)) {
            RectangleMapObject newObject;
            int rnd = MathUtils.random(EnemySeven.MIN_CLONE, EnemySeven.MAX_CLONE);
            for (int i = 0; i < rnd; i++) {
                newObject = new RectangleMapObject(x * PlayScreen.PPM,
                        y * PlayScreen.PPM,
                        AssetEnemySeven.WIDTH_METERS * PlayScreen.PPM,
                        AssetEnemySeven.HEIGHT_METERS * PlayScreen.PPM); // Width and height are irrelevant
                createGameThreeActor(new ActorDef(new EnemySeven(screen, newObject)));
            }
        }

        if (mp.containsKey(KEY_COL_ONE)) {
            createGameThreeActor(new ActorDef(new ColOne(screen, x, y)));
        }
        if (mp.containsKey(KEY_COL_SILVER_BULLET)) {
            createGameThreeActor(new ActorDef(new ColSilverBullet(screen, x, y)));
        }
        if (mp.containsKey(KEY_POWER_ONE)) {
            timer = object.getProperties().get(KEY_POWER_ONE, 0, Integer.class);
            createGameThreeActor(new ActorDef(new PowerOne(screen, x, y, timer)));
        }
        if (mp.containsKey(KEY_POWER_TWO)) {
            timer = object.getProperties().get(KEY_POWER_TWO, 0, Integer.class);
            createGameThreeActor(new ActorDef(new PowerTwo(screen, x, y, timer)));
        }
        if (mp.containsKey(KEY_POWER_THREE)) {
            timer = object.getProperties().get(KEY_POWER_THREE, 0, Integer.class);
            createGameThreeActor(new ActorDef(new PowerThree(screen, x, y, timer)));
        }
        if (mp.containsKey(KEY_POWER_FOUR)) {
            timer = object.getProperties().get(KEY_POWER_FOUR, 0, Integer.class);
            createGameThreeActor(new ActorDef(new PowerFour(screen, x, y, timer)));
        }
    }

    public void printDebugStatus() {
        if (PlayScreen.DEBUG_BODY_COUNT) {
            arrayMapDebug.clear();
            String key;
            String value;
            String data;

            for (Bridge bridge : getBridges()) {
                key = bridge.whoAmI();
                value = arrayMapDebug.get(key);
                data = bridge.getTiledMapId() + " (" + bridge.getCurrentState() + ")";
                value = value == null ? data : value + ", " + data;
                arrayMapDebug.put(key, value);
            }

            for (Enemy enemy : getEnemies()) {
                key = enemy.whoAmI();
                value = arrayMapDebug.get(key);
                data = enemy.getTiledMapId() + " (" + enemy.getCurrentState() + ")";
                value = value == null ? data : value + ", " + data;
                arrayMapDebug.put(key, value);
            }

            for (PowerBox powerBox : getPowerBoxes()) {
                key = powerBox.whoAmI();
                value = arrayMapDebug.get(key);
                data = powerBox.getTiledMapId() + " (" + powerBox.getCurrentState() + ")";
                value = value == null ? data : value + ", " + data;
                arrayMapDebug.put(key, value);
            }

            for (Item item : getItems()) {
                key = item.whoAmI();
                value = arrayMapDebug.get(key);
                data = item.getCurrentState();
                value = value == null ? data : value + ", " + data;
                arrayMapDebug.put(key, value);
            }

            for (Weapon weapon : getWeapons()) {
                key = weapon.whoAmI();
                value = arrayMapDebug.get(key);
                data = weapon.getCurrentState();
                value = value == null ? data : value + ", " + data;
                arrayMapDebug.put(key, value);
            }

            Gdx.app.debug(TAG, "***** World.bodyCount: " + screen.getWorld().getBodyCount());
            Gdx.app.debug(TAG, "***** Borders: " + borders.size);
            Gdx.app.debug(TAG, "***** Edges: " + edges.size);
            Gdx.app.debug(TAG, "***** Obstacles: " + obstacles.size);
            Gdx.app.debug(TAG, "***** Path: " + paths.size);
            Gdx.app.debug(TAG, "***** Bridges: " + bridges.size);
            Gdx.app.debug(TAG, "***** Enemies: " + enemies.size);
            Gdx.app.debug(TAG, "***** Power boxes: " + powerBoxes.size);
            Gdx.app.debug(TAG, "***** Items: " + items.size);
            Gdx.app.debug(TAG, "***** Weapons: " + weapons.size);
            Gdx.app.debug(TAG, "***** FinalEnemy: " + (screen.getFinalEnemy().isDisposable() ? 0 : 1));
            Gdx.app.debug(TAG, "***** Hero always 1");
            Gdx.app.debug(TAG, "***** Objects not disposables detail (Bridge, Enemy, PowerBox, Item, Weapon) ****");
            for (ObjectMap.Entry<String, String> entry : arrayMapDebug.entries()) {
                Gdx.app.debug(TAG, "***** " + entry.key + ": " + entry.value);
            }
            Gdx.app.debug(TAG, "*********************************");
        }
    }
}
