package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.game.GameController;
import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;
import uy.com.agm.gamethree.sprites.boundary.Edge;
import uy.com.agm.gamethree.sprites.boxes.PowerBox;
import uy.com.agm.gamethree.sprites.enemies.Enemy;
import uy.com.agm.gamethree.sprites.finals.FinalEnemy;
import uy.com.agm.gamethree.sprites.items.Item;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.sprites.weapons.Weapon;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.LevelFactory;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/2/2017.
 */

public class PlayScreen extends AbstractScreen {
    private static final String TAG = PlayScreen.class.getName();

    public enum PlayScreenState
    {
        PAUSED, RUNNING
    }
    private PlayScreenState playScreenState;

    // Reference to our Game, used to set Screens
    private GameThree game;

    // Current level
    private int level;

    // Time to wait after the level is completed
    private float levelCompletedTimer;

    // Basic playscreen variables
    private OrthographicCamera gameCam;
    private Viewport gameViewPort;
    private Hud hud;

    // TiledEditor map variable
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private float accumulator;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    // Main character
    private Hero player;

    // Boundaries
    private Edge upperEdge;
    private Edge bottomEdge;

    // Final Enemy
    private FinalEnemy finalEnemy;

    public PlayScreen(Integer level) {
        this.level = level;
        levelCompletedTimer = 0;

        this.game = (GameThree) ScreenManager.getInstance().getGame();

        // Create a cam used to move up through our world
        gameCam = new OrthographicCamera();

        // Create a FitViewport to maintain virtual aspect ratio despite screen size
        gameViewPort = new FitViewport(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM, gameCam);

        // Get our map and setup our map renderer
        map = LevelFactory.getLevelMap(this.level);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);

        // Initially set our gamcam to be centered correctly at the start (bottom) of the map
        gameCam.position.set(gameViewPort.getWorldWidth() / 2, gameViewPort.getWorldHeight() / 2, 0);
        // todo gameCam.position.set(gameViewPort.getWorldWidth() / 2, 69, 0);

        // Create our Box2D world, setting no gravity in x and no gravity in y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), true);

        // Set accumulator for world.step
        accumulator = 0;

        // Allows for debug lines of our box2d world.
        if (Constants.DEBUG_MODE) {
            b2dr = new Box2DDebugRenderer();
        }

        creator = new B2WorldCreator(this);

        // Create the hero in our game world
        player = new Hero(this, gameCam.position.x, gameCam.position.y / 2);
        //player.getB2body().setTransform(this.getGameCam().position.x, this.getGameCam().position.y - this.getGameViewPort().getWorldHeight() / 4, player.getB2body().getAngle()); // todo

        // Create boundaries
        upperEdge = new Edge(this, true);
        bottomEdge = new Edge(this, false);

        // Create the final enemy in our game world
        finalEnemy = LevelFactory.getFinalEnemy(this, this.level);

        // Create our collision listener
        world.setContactListener(new WorldContactListener());

        // Create our game HUD for scores/timers/level info
        hud = new Hud(this, LevelFactory.getLevelTimer(this.level), player.getLives());
        hud.buildStage();

        // Stop menu music and start playing level music
        AudioManager.getInstance().stopMusic();
        AudioManager.getInstance().play(LevelFactory.getLevelMusic(this.level));

        // User input handler
        Gdx.input.setInputProcessor(getInputProcessor(new GameController(this)));

        // PlayScreen running
        playScreenState = PlayScreenState.RUNNING;
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
        multiplexer.addProcessor(hud); // Hud also implements InputProcessor and receives events
        multiplexer.addProcessor(new GestureDetector(gc));
        multiplexer.addProcessor(gc);
        return multiplexer;
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
        doPhysicsStep(dt);

        updatePowerBoxes(dt);
        updateItems(dt);
        updateWeapons(dt);
        updateFinalEnemy(dt);
        updateEnemies(dt);
        updateHero(dt);
        updateHud(dt);
        updateCamera(dt);
    }

    private void doPhysicsStep(float dt) {
        // Fixed time step
        // Max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(dt, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.WORLD_TIME_STEP) {
            world.step(Constants.WORLD_TIME_STEP, Constants.WORLD_VELOCITY_ITERATIONS, Constants.WORLD_POSITION_ITERATIONS);
            accumulator -= Constants.WORLD_TIME_STEP;
        }
    }

    private void updateHero(float dt) {
        player.update(dt);
    }

    private void updateEnemies(float dt) {
        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
        }
        // Clean up collection
        for(Enemy enemy : creator.getEnemies()) {
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
        for(PowerBox powerBox : creator.getPowerBoxes()) {
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
        for(Item item : creator.getItems()) {
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
        for(Weapon weapon : creator.getWeapons()) {
            if (weapon.isDisposable()) {
                creator.removeWeapon(weapon);
            }
        }
    }

    private void updateFinalEnemy(float dt) {
        finalEnemy.update(dt);
    }

    private void updateHud(float dt) {
        if(!player.isHeroDead() && !finalEnemy.isDestroyed()) {
            hud.update(dt);
        }
    }

    private boolean isTheEndOfTheWorld() {
        return upperEdge.getB2body().getPosition().y + Constants.EDGE_HEIGHT_METERS / 2 >= gameViewPort.getWorldHeight() * Constants.WORLD_SCREENS;
    }

    private void updateCamera(float dt) {
        // If Hero is dead, we freeze the camera
        if(!player.isHeroDead()) {
            if (isTheEndOfTheWorld()) {
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

    public FinalEnemy getFinalEnemy() {
        return finalEnemy;
    }

    public Edge getBottomEdge() {
        return bottomEdge;
    }

    public Edge getUpperEdge() {
        return upperEdge;
    }

    private void render() {
        // Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render our game map
        renderer.render();

        // Renderer our Box2DDebugLines
        if (Constants.DEBUG_MODE) {
            b2dr.render(world, gameCam.combined);
        }

        // Set our batch to now draw what the gameCam camera sees.
        game.getBatch().setProjectionMatrix(gameCam.combined);
        game.getBatch().begin();

        renderPowerBoxes();
        renderItems();
        renderWeapons();
        renderFinalEnemy();
        renderEnemies();
        renderHero();

        game.getBatch().end();

        if (playScreenState == PlayScreenState.PAUSED) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.getShapeRenderer().setProjectionMatrix(hud.getCamera().combined);
            game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
            game.getShapeRenderer().setColor(0, 0, 0, 0.5f);
            game.getShapeRenderer().rect(0, 0, hud.getCamera().viewportWidth, hud.getCamera().viewportHeight);
            game.getShapeRenderer().end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        // Draw hud
        hud.draw();

        // Debug
        if (Constants.DEBUG_MODE) {
            // Set our batch to now draw what the gameCam camera sees.
            game.getShapeRenderer().setProjectionMatrix(gameCam.combined);
            game.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
            game.getShapeRenderer().setColor(1, 1, 0, 1);

            renderDebugPowerBoxes();
            renderDebugItems();
            renderDebugWeapons();
            renderDebugFinalEnemy();
            renderDebugEnemies();
            renderDebugHero();

            game.getShapeRenderer().end();
        }
    }

    private boolean isLevelCompleted(float delta) {
        boolean isLevelCompleted = false;

        if (finalEnemy.isDisposable()) {
            levelCompletedTimer += delta;
            isLevelCompleted = levelCompletedTimer > Constants.LEVEL_COMPLETED_DELAY_SECONDS;
        }
        return isLevelCompleted;
    }

    private void renderHero() {
        player.draw(game.getBatch());
    }

    private void renderEnemies() {
        for (Enemy enemy : creator.getEnemies()) {
            enemy.draw(game.getBatch());
        }
    }

    private void renderPowerBoxes() {
        for (PowerBox powerBox : creator.getPowerBoxes()) {
            powerBox.draw(game.getBatch());
        }
    }

    private void renderItems() {
        for (Item item : creator.getItems())  {
            item.draw(game.getBatch());
        }
    }

    private void renderWeapons() {
        for (Weapon weapon : creator.getWeapons()) {
            weapon.draw(game.getBatch());
        }
    }

    private void renderFinalEnemy() {
        finalEnemy.draw(game.getBatch());
    }

    private void renderDebugHero() {
        player.renderDebug(game.getShapeRenderer());
    }

    private void renderDebugEnemies() {
        for (Enemy enemy : creator.getEnemies()) {
            enemy.renderDebug(game.getShapeRenderer());
        }
    }

    private void renderDebugPowerBoxes() {
        for (PowerBox powerBox : creator.getPowerBoxes()) {
            powerBox.renderDebug(game.getShapeRenderer());
        }
    }

    private void renderDebugItems() {
        for (Item item : creator.getItems()) {
            item.renderDebug(game.getShapeRenderer());
        }
    }

    private void renderDebugWeapons() {
        for (Weapon weapon : creator.getWeapons()) {
            weapon.renderDebug(game.getShapeRenderer());
        }
    }

    private void renderDebugFinalEnemy() {
        finalEnemy.renderDebug(game.getShapeRenderer());
    }

    private void stopEdges() {
        upperEdge.stop();
        bottomEdge.stop();
    }

    private void startEdges() {
        if (!isTheEndOfTheWorld()) {
            upperEdge.start();
            bottomEdge.start();
        }
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public OrthographicCamera getGameCam() {
        return gameCam;
    }

    public Viewport getGameViewPort() {
        return gameViewPort;
    }

    public Hud getHud() {
        return hud;
    }

    public void playLevelMusic() {
        AudioManager.getInstance().play(LevelFactory.getLevelMusic(this.level));
    }

    private void gameResults(float delta) {
        if (player.isTimeToPlayAgain()) {
            player.playAgain();
            startEdges();
        }

        if (player.isGameOver()) {
            ScreenManager.getInstance().showScreen(ScreenEnum.GAME_OVER);
        }

        if (isLevelCompleted(delta)) {
            ScreenManager.getInstance().showScreen(ScreenEnum.LEVEL_COMPLETED, this.level, hud.getScore());
        }
    }

    public PlayScreenState getPlayScreenState(){
        return playScreenState;
    }

    public void setPlayScreenStatePaused(){
        this.playScreenState = PlayScreenState.PAUSED;
        AudioManager.getInstance().pauseMusic();
        AudioManager.getInstance().pauseSound();
    }

    public void setPlayScreenStateRunning(){
        this.playScreenState = PlayScreenState.RUNNING;
        if (!player.isHeroDead() && !finalEnemy.isDestroyed()) {
            AudioManager.getInstance().resumeMusic();
        }
        AudioManager.getInstance().resumeSound();
    }

    @Override
    public void render(float delta) {
        // Separate our update logic from render
        if (playScreenState == PlayScreenState.RUNNING) {
            update(delta);
        }

        // Render logic
        render();

        // Analyze game results
        if (playScreenState == PlayScreenState.RUNNING) {
            gameResults(delta);
        }
    }

    @Override
    public void buildStage() {
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        // Updated our game viewport
        gameViewPort.update(width, height);
    }

    @Override
    public void pause() {
        hud.setGameStatePaused();
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
        super.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        if (Constants.DEBUG_MODE) {
            b2dr.dispose();
        }
        hud.dispose();
    }
}
