package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.scenes.Hud;
import uy.com.agm.gamethree.sprites.enemies.Enemy;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.sprites.powerup.Items.Item;
import uy.com.agm.gamethree.sprites.powerup.boxes.PowerBox;
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

    // Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    public B2WorldCreator creator;

    // Main character
    public Hero player;

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

        // Initially set our gamcam to be centered correctly at the start (bottom) of of map
        gameCam.position.set(gameViewPort.getWorldWidth() / 2, gameViewPort.getWorldHeight() / 2, 0);

        // Create our Box2D world, setting no gravity in X and no gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), true);

        // Allows for debug lines of our box2d world.
        if (Constants.DEBUG_BOUNDARIES) {
            b2dr = new Box2DDebugRenderer();
        }

        creator = new B2WorldCreator(this);

        // Create the hero in our game world
        player = new Hero(this, 1.0f, 1.0f);

        world.setContactListener(new WorldContactListener());
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        // Control our player using linear velocity
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y <= Constants.MAX_LINEAR_VELOCITY) {
            player.b2body.setLinearVelocity(new Vector2(player.b2body.getLinearVelocity().x, Constants.LINEAR_VELOCITY));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().y >= -Constants.MAX_LINEAR_VELOCITY) {
            player.b2body.setLinearVelocity(new Vector2(player.b2body.getLinearVelocity().x, -Constants.LINEAR_VELOCITY));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= Constants.MAX_LINEAR_VELOCITY) {
            player.b2body.setLinearVelocity(new Vector2(Constants.LINEAR_VELOCITY, player.b2body.getLinearVelocity().y));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -Constants.MAX_LINEAR_VELOCITY) {
            player.b2body.setLinearVelocity(new Vector2(-Constants.LINEAR_VELOCITY, player.b2body.getLinearVelocity().y));
        }
    }

    public void update(float dt) {
        // Handle user input first
        handleInput(dt);
        creator.handleCreatingItems();

        // Takes 1 step in the physics simulation (60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(dt);

        // Enemies
        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
        }

        // PowerBoxes
        for (PowerBox powerBox : creator.getPowerBoxes()) {
            powerBox.update(dt);
        }

        // Items
        for (Item item : creator.getItems()) {
            item.update(dt);
        }

        hud.update(dt);

        // Cam is moving up
        gameCam.position.y += Constants.GAMECAM_VELOCITY * dt;

        // Update our gamecam with correct coordinates after changes
        gameCam.update();

        // Tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gameCam);
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

        // Hero
        player.draw(game.batch);

        // Enemies
        for (Enemy enemy : creator.getEnemies()) {
            enemy.draw(game.batch);
        }

        // PowerBoxes
        for (PowerBox powerBox : creator.getPowerBoxes()) {
            powerBox.draw(game.batch);
        }

        // Items
        for (Item item : creator.getItems()) {
            item.draw(game.batch);
        }
        game.batch.end();

        // Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        // Debug
        if (Constants.DEBUG_BOUNDARIES) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            // Set our batch to now draw what the gameCam camera sees.
            shapeRenderer.setProjectionMatrix(gameCam.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);

            // Hero
            player.renderDebug(shapeRenderer);

            // Enemies
            for (Enemy enemy : creator.getEnemies()) {
                enemy.renderDebug(shapeRenderer);
            }

            // Power boxes
            for (PowerBox powerBox : creator.getPowerBoxes()) {
                powerBox.renderDebug(shapeRenderer);
            }

            // Items
            for (Item item : creator.getItems()) {
                item.renderDebug(shapeRenderer);
            }
            shapeRenderer.end();
        }
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
        b2dr.dispose();
        hud.dispose();
    }
}
