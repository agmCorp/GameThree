package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.GameController;
import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.scenes.Hud;
import uy.com.agm.gamethree.sprites.boundary.Edge;
import uy.com.agm.gamethree.sprites.enemies.Enemy;
import uy.com.agm.gamethree.sprites.finals.FinalEnemyLevelOne;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.sprites.powerup.Items.Item;
import uy.com.agm.gamethree.sprites.powerup.boxes.PowerBox;
import uy.com.agm.gamethree.sprites.weapons.Weapon;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.Constants;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/2/2017.
 */

public class PlayScreen implements Screen {
    private static final String TAG = PlayScreen.class.getName();

    // Reference to our Game, used to set Screens
    private GameThree game;

    // Basic playscreen variables
    public OrthographicCamera gameCam;
    public Viewport gameViewPort;
    private Hud hud;

    // TiledEditor map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    // Main character
    private Hero player;

    // Boundaries
    private Edge upperEdge;
    private Edge bottomEdge;

    // Final Enemy (level one)
    private FinalEnemyLevelOne finalEnemyLevelOne;

    public PlayScreen(GameThree game) {
        this.game = game;

        // Create a cam used to move up through our world
        gameCam = new OrthographicCamera();

        // Create a FitViewport to maintain virtual aspect ratio despite screen size
        gameViewPort = new FitViewport(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM, gameCam);

        // Create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        // Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("levelOne/levelOne.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);

        // Initially set our gamcam to be centered correctly at the start (bottom) of the map
        gameCam.position.set(gameViewPort.getWorldWidth() / 2, gameViewPort.getWorldHeight() / 2, 0);

        // Create our Box2D world, setting no gravity in x and no gravity in y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), true);

        // Allows for debug lines of our box2d world.
        if (Constants.DEBUG_BOUNDARIES) {
            b2dr = new Box2DDebugRenderer();
        }

        creator = new B2WorldCreator(this);

        // Create the hero in our game world
        player = new Hero(this, gameCam.position.x, gameCam.position.y / 2);

        // Create boundaries
        upperEdge = new Edge(this, true);
        bottomEdge = new Edge(this, false);

        // Create the final enemy in our game world
        finalEnemyLevelOne = new FinalEnemyLevelOne(this, gameCam.position.x, gameViewPort.getWorldHeight() * Constants.WORLD_SCREENS - Constants.FINALLEVELONE_HEIGHT_METERS + 0.2f); // TODO CONSTANTE DE AJUSTE PORQUE

        // Create our collision listener
        world.setContactListener(new WorldContactListener());

        // todo Load preferences for audio settings and start playing music
        // GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.songLevelOne);

        // User input handler
        Gdx.input.setInputProcessor(getInputProcessor(new GameController(player)));
    }

    private InputProcessor getInputProcessor(GameController gc) {
        /* GameController is an InputAdapter because it extends that class and
         * It's also a GestureListener because it implements that interface.
         * In GameController then I can recognize gestures (like fling) and I can
         * recognize events such as touchUp that doesn't exist within the interface
         * GestureListener but exists within an InputAdapter.
         * As the InputAdapter methods are too many, I decided to extend that
         * class (to implement within GameController only the method that I'm interested in) and
         * implemented the GestureListener interface because, after all, there are only few extra methods that I must declare.
         * To work with both InputProcessors at the same time, I must use a InputMultiplexer.
         * The fling and touchUp events, for example, always run at the same time.
         * First I registered GestureDetector so that fling is executed before touchUp and as they are related,
         * when I return true in the fling event the touchUp is canceled. If I return false both are executed.
         * */

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new GestureDetector(gc));
        multiplexer.addProcessor(gc);
        return multiplexer;
    }

    @Override
    public void show() {
    }

    // Key control
    private void handleInput(float dt) {
        // We use GameController instead of input.isKeyPressed.
    }

    private void update(float dt) {
        // Handle user input first
        handleInput(dt);

        // Handle creation of game actors first
        creator.handleCreatingGameThreeActors();

        // Step in the physics simulation
        world.step(Constants.WORLD_TIME_STEP, Constants.WORLD_VELOCITY_ITERATIONS, Constants.WORLD_POSITION_ITERATIONS);

        updateHero(dt);
        updateEnemies(dt);
        updatePowerBoxes(dt);
        updateItems(dt);
        updateWeapons(dt);
        updateFinalEnemyLevelOne(dt);
        updateHud(dt);
        updateCamera(dt);
    }

    private void updateHero(float dt) {
        player.update(dt);
    }

    private void updateEnemies(float dt) {
        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
        }
        // Clean up collection
        for (Enemy enemy : creator.getEnemies()) {
            if (enemy.isDisposable()) {
                creator.removeEnemy(enemy);
            }
        }
    }

    private void updatePowerBoxes(float dt) {
        for (PowerBox powerBox : creator.getPowerBoxes()) {
            powerBox.update(dt);
        }
        // Clean up collection
        for (PowerBox powerBox : creator.getPowerBoxes()) {
            if (powerBox.isDisposable()) {
                creator.removePowerBox(powerBox);
            }
        }
    }

    private void updateItems(float dt) {
        for (Item item : creator.getItems()) {
            item.update(dt);
        }
        // Clean up collection
        for (Item item : creator.getItems()) {
            if (item.isDisposable()) {
                creator.removeItem(item);
            }
        }
    }

    private void updateWeapons(float dt) {
        for (Weapon weapon : creator.getWeapons()) {
            weapon.update(dt);
        }
        // Clean up collection
        for (Weapon weapon : creator.getWeapons()) {
            if (weapon.isDisposable()) {
                creator.removeWeapon(weapon);
            }
        }
    }

    private void updateFinalEnemyLevelOne(float dt) {
        finalEnemyLevelOne.update(dt);
    }

    private void updateHud(float dt) {
        hud.update(dt);
    }

    private void updateCamera(float dt) {
        // If Hero is dead, we freeze the camera
        if(!player.isHeroDead()) {
            if (upperEdge.getB2body().getPosition().y + Constants.EDGE_HEIGHT_METERS / 2 >= gameViewPort.getWorldHeight() * Constants.WORLD_SCREENS) {
                stopEdges();
            }
        } else {
            stopEdges();
        }

        // Our camera is relative to the edges
        gameCam.position.y = upperEdge.getB2body().getPosition().y + Constants.EDGE_HEIGHT_METERS / 2 - gameViewPort.getWorldHeight() / 2;

        // Update our gamecam with correct coordinates after changes
        gameCam.update();

        // Tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gameCam);
    }

    public B2WorldCreator getCreator() {
        return creator;
    }

    public Hero getPlayer() {
        return player;
    }

    public Edge getBottomEdge() {
        return bottomEdge;
    }

    public Edge getUpperEdge() {
        return upperEdge;
    }

    @Override
    public void render(float delta) {
        // Separate our update logic from render
        update(delta);

        // Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render our game map
        renderer.render();

        // Renderer our Box2DDebugLines
        if (Constants.DEBUG_BOUNDARIES) {
            b2dr.render(world, gameCam.combined);
        }

        // Set our batch to now draw what the gameCam camera sees.
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        drawHero();
        drawEnemies();
        drawPowerBoxes();
        drawItems();
        drawWeapons();
        drawFinalEnemyLevelOne();

        game.batch.end();

        // Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        // Debug
        if (Constants.DEBUG_BOUNDARIES) {
            // Set our batch to now draw what the gameCam camera sees.
            game.shapeRenderer.setProjectionMatrix(gameCam.combined);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            game.shapeRenderer.setColor(1, 1, 0, 1);

            renderDebugHero();
            renderDebugEnemies();
            renderDebugPowerBoxes();
            renderDebugItems();
            renderDebugWeapons();
            renderDebugFinalEnemyLevelOne();
            Gdx.app.debug(TAG, "FPS: " + Gdx.graphics.getFramesPerSecond());

            game.shapeRenderer.end();
        }

        if (player.isGameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    private void drawHero() {
        player.draw(game.batch);
    }

    private void drawEnemies() {
        for (Enemy enemy : creator.getEnemies()) {
            enemy.draw(game.batch);
        }
    }

    private void drawPowerBoxes() {
        for (PowerBox powerBox : creator.getPowerBoxes()) {
            powerBox.draw(game.batch);
        }
    }

    private void drawItems() {
        for (Item item : creator.getItems())  {
            item.draw(game.batch);
        }
    }

    private void drawWeapons() {
        for (Weapon weapon : creator.getWeapons()) {
            weapon.draw(game.batch);
        }
    }

    private void drawFinalEnemyLevelOne() {
        finalEnemyLevelOne.draw(game.batch);
    }

    private void renderDebugHero() {
        player.renderDebug(game.shapeRenderer);
    }

    private void renderDebugEnemies() {
        for (Enemy enemy : creator.getEnemies()) {
            enemy.renderDebug(game.shapeRenderer);
        }
    }

    private void renderDebugPowerBoxes() {
        for (PowerBox powerBox : creator.getPowerBoxes()) {
            powerBox.renderDebug(game.shapeRenderer);
        }
    }

    private void renderDebugItems() {
        for (Item item : creator.getItems()) {
            item.renderDebug(game.shapeRenderer);
        }
    }

    private void renderDebugWeapons() {
        for (Weapon weapon : creator.getWeapons()) {
            weapon.renderDebug(game.shapeRenderer);
        }
    }

    private void renderDebugFinalEnemyLevelOne() {
        finalEnemyLevelOne.renderDebug(game.shapeRenderer);
    }

    private void stopEdges() {
        upperEdge.stop();
        bottomEdge.stop();
    }

    @Override
    public void resize(int width, int height) {
        // Updated our game viewport
        gameViewPort.update(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public Hud getHud() {
        return hud;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Dispose of all our opened resources
        map.dispose();
        renderer.dispose();
        world.dispose();
        if (Constants.DEBUG_BOUNDARIES) {
            b2dr.dispose();
        }
        hud.dispose();
    }
}
