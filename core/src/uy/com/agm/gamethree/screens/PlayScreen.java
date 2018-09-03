package uy.com.agm.gamethree.screens;

import com.admob.IAdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

import uy.com.agm.gamethree.actors.backgroundObjects.kinematicObjects.Bridge;
import uy.com.agm.gamethree.actors.backgroundObjects.kinematicObjects.Edge;
import uy.com.agm.gamethree.actors.backgroundObjects.staticObjects.PowerBox;
import uy.com.agm.gamethree.actors.bosses.Boss;
import uy.com.agm.gamethree.actors.enemies.Enemy;
import uy.com.agm.gamethree.actors.items.Item;
import uy.com.agm.gamethree.actors.player.Hero;
import uy.com.agm.gamethree.actors.weapons.Weapon;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameController;
import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.screens.util.DimScreen;
import uy.com.agm.gamethree.screens.util.InfoScreen;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;
import uy.com.agm.gamethree.screens.util.ScreenTransitionEnum;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.LevelFactory;
import uy.com.agm.gamethree.tools.Shaker;
import uy.com.agm.gamethree.tools.WorldContactListener;

import static com.badlogic.gdx.Gdx.input;


/**
 * Created by AGM on 12/2/2017.
 */

public class PlayScreen extends AbstractScreen {
    private static final String TAG = PlayScreen.class.getName();

    // Constants

    // Time to wait till level completed screen turns up
    private static final float LEVEL_COMPLETED_DELAY_SECONDS = 6.0f;

    // World physics simulation parameters
    private static final float MAX_FRAME_TIME = 0.25f;
    private static final float WORLD_TIME_STEP = 1/300.0f;
    private static final int WORLD_VELOCITY_ITERATIONS = 6;
    private static final int WORLD_POSITION_ITERATIONS = 2;

    // Each screen is 800px height, so the whole world (see TiledEditor) is 8000px.
    public static final int WORLD_SCREENS = 10;

    // Box2D Scale (Pixels Per Meter)
    public static final float PPM = 100;

    // Position (y-axis) where the epic fight against the boss begins
    private static final float LEVEL_CHALLENGE_BEGIN = V_HEIGHT * (WORLD_SCREENS - 1) / PPM;

    // Game cam velocity (m/s)
    public static final float GAME_CAMERA_VELOCITY = DebugConstants.STATIC_GAME_CAM ? 0 : 0.304f;

    // Break duration
    public static final float BREAK_SECONDS = 1.6f;

    // Game state
    private enum PlayScreenState {
        PAUSED, RUNNING, BEAK
    }
    private PlayScreenState playScreenState;
    private float breakTime;

    // Reference to our Game, used to set Screens
    private GameThree game;

    // Current level
    private int level;

    // Time to wait after the level is completed
    private float levelCompletedTime;

    // Basic PlayScreen variables
    private OrthographicCamera gameCam;
    private Viewport gameViewPort;
    private Hud hud;
    private InfoScreen infoScreen;
    private DimScreen dimScreen;
    private boolean levelStarts;
    private boolean showGameControllersHelp;
    private boolean showRedFlashHelp;
    private boolean allowAds;
    private boolean newScreen;

    // TiledEditor map variable
    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

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

    // The boss
    private Boss boss;

    // Screen shaker
    private Shaker shaker;

    public PlayScreen(Integer level) {
        this.level = level;
        levelCompletedTime = 0;

        this.game = ScreenManager.getInstance().getGame();

        // Create a cam used to move up through our world
        gameCam = new OrthographicCamera();

        // Create a FitViewport to maintain virtual aspect ratio despite screen size
        // We use the convention 100 pixels = 1 meter to work with meters and therefore meters per seconds in velocity and so on.
        gameViewPort = new FitViewport(AbstractScreen.V_WIDTH / PPM, AbstractScreen.V_HEIGHT / PPM, gameCam);

        // Get the map and setup our map renderer
        map = LevelFactory.getLevelMap(level);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        // Initially set the gameCam to be centered correctly at the start (bottom) of the map
        gameCam.position.set(gameViewPort.getWorldWidth() / 2, DebugConstants.GAME_CAM_Y_METERS > 0 ? DebugConstants.GAME_CAM_Y_METERS : gameViewPort.getWorldHeight() / 2, 0);

        // Create the Box2D world, setting no gravity in x and no gravity in y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), true);

        // Avoid sticking to walls when velocity is less than 1
        world.setVelocityThreshold(0.0f);

        // Set accumulator for world.step
        accumulator = 0;

        // Allows for debug lines of the box2d world.
        if (DebugConstants.DEBUG_LINES) {
            b2dr = new Box2DDebugRenderer();
        }

        creator = new B2WorldCreator(this);

        // Get our hero
        player = creator.getHero();

        if (DebugConstants.GAME_CAM_Y_METERS > 0) {
            player.getB2body().setTransform(this.getGameCam().position.x,
                    this.getGameCam().position.y - this.getGameViewPort().getWorldHeight() / 4,
                    player.getB2body().getAngle());
        }

        // Boundaries
        upperEdge = creator.getUpperEdge();
        bottomEdge = creator.getBottomEdge();

        // Get the boss
        boss = creator.getBoss();

        // Create the collision listener
        world.setContactListener(new WorldContactListener());

        // Create the game HUD for score, time, etc.
        hud = new Hud(this, level, 0, LevelFactory.getLevelTimer(level), Hero.LIVES_START, Hero.ENDURANCE_START);
        hud.buildStage();

        // Create the InfoScreen for help messages, images, animations, etc.
        infoScreen = new InfoScreen(this, level);
        infoScreen.buildStage();

        // Create the DimScreen for pause/resume
        dimScreen = new DimScreen(this);
        dimScreen.buildStage();

        // Indicates that the level is just beginning
        levelStarts = true;

        // Used to display a help about controllers
        showGameControllersHelp = true;

        // Used to display a help about slippery enemies
        showRedFlashHelp = true;

        // Used to display ads
        allowAds = true;

        // True if we change the screen (see ScreenManager.showScreen(...))
        newScreen = false;

        // Start playing level music from the beginning (if we arrive from DimScreen/reload)
        AudioManager.getInstance().stopMusic();

        // Play level music
        AudioManager.getInstance().playMusic(LevelFactory.getLevelMusic(level), true);

        // User input handler
        input.setInputProcessor(getInputProcessor(new GameController(this)));

        // Screen shaker
        shaker = new Shaker();

        // PlayScreen running
        playScreenState = PlayScreenState.RUNNING;
        breakTime = 0;
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
        multiplexer.addProcessor(dimScreen);  // DimScreen also implements InputProcessor and receives events
        multiplexer.addProcessor(infoScreen); // InfoScreen also implements InputProcessor and receives events
        multiplexer.addProcessor(new GestureDetector(gc)); // Detects gestures (tap, long press, fling, pan, zoom, pinch)
        multiplexer.addProcessor(gc); // User input handler on PlayScreen
        return multiplexer;
    }

    // Key control
    private void handleInput(float dt) {
        // We use GameController instead of input.isKeyPressed.
    }

    private void gameLogic(float delta) {
        // Update logic (take input, creation of new game actors, do physics)
        if (playScreenState == PlayScreenState.RUNNING) {
            updateGameLogic(delta);
        } else {
            if (playScreenState == PlayScreenState.BEAK) {
                updateGameLogicBreak(delta);
            }
        }
    }

    private void updateGameLogic(float dt) {
        // Handle user input first
        handleInput(dt);

        // Handle creation of game actors first
        creator.handleCreatingActors();

        // Step in the physics simulation
        // Anything that involves adding fixtures, removing fixtures from a body or creating a body during actual simulation
        // requires you to do it OUTSIDE the physics step.
        // The 'physics step' involves WorldContactListener and all the methods it calls on bodies (like onHit, onTake, etc).
        // So, for instance, in Enemy.onHit() I must never touch the enemy's body (I should just mark it to
        // modify it inside the Enemy.update() method)
        doPhysicsStep(dt);

        // The order is not important
        updateKinematicBridges(dt);
        updatePowerBoxes(dt);
        updateItems(dt);
        updateWeapons(dt);
        updateEnemies(dt);
        updateBoss(dt);
        updateHero(dt);

        // Always at the end
        updateHud(dt);
        updateInfoScreen(dt);
        updateCamera(dt);

        creator.printDebugStatus();
    }

    private void updateGameLogicBreak(float dt) {
        // Update Hero logic to show a blink effect
        updateHero(dt);

        // Break time count
        breakTime += dt;
        if (breakTime > BREAK_SECONDS) {
            playScreenState = PlayScreenState.RUNNING;
            breakTime = 0;
        }
    }

    private void doPhysicsStep(float dt) {
        // Fixed time step
        // Max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(dt, MAX_FRAME_TIME);
        accumulator += frameTime;
        while (accumulator >= WORLD_TIME_STEP) {
            world.step(WORLD_TIME_STEP, WORLD_VELOCITY_ITERATIONS, WORLD_POSITION_ITERATIONS);
            accumulator -= WORLD_TIME_STEP;
        }
    }

    private void updateHero(float dt) {
        player.update(dt);
    }

    private void updateEnemies(float dt) {
        Enemy enemy;
        Iterator<Enemy> iterator = creator.getEnemies().iterator();
        while(iterator.hasNext()) {
            enemy = iterator.next();
            enemy.update(dt);
            if(enemy.isDisposable()){
                iterator.remove();
            }
        }
    }

    private void updateKinematicBridges(float dt) {
        Bridge bridge;
        Iterator<Bridge> iterator = creator.getBridges().iterator();
        while(iterator.hasNext()) {
            bridge = iterator.next();
            bridge.update(dt);
            if(bridge.isDisposable()){
                iterator.remove();
            }
        }
    }

    private void updatePowerBoxes(float dt) {
        PowerBox powerBox;
        Iterator<PowerBox> iterator = creator.getPowerBoxes().iterator();
        while(iterator.hasNext()) {
            powerBox = iterator.next();
            powerBox.update(dt);
            if(powerBox.isDisposable()){
                iterator.remove();
            }
        }
    }

    private void updateItems(float dt) {
        Item item;
        Iterator<Item> iterator = creator.getItems().iterator();
        while(iterator.hasNext()) {
            item = iterator.next();
            item.update(dt);
            if(item.isDisposable()){
                iterator.remove();
            }
        }
    }

    private void updateWeapons(float dt) {
        Weapon weapon;
        Iterator<Weapon> iterator = creator.getWeapons().iterator();
        while(iterator.hasNext()) {
            weapon = iterator.next();
            weapon.update(dt);
            if(weapon.isDisposable()){
                iterator.remove();
            }
        }
    }

    private void updateBoss(float dt) {
        boss.update(dt);
    }

    private void updateHud(float dt) {
        if(!player.isDead() && !boss.isDestroyed()) {
            hud.update(dt);
        }
    }

    private void updateInfoScreen(float dt) {
        infoScreen.update(dt);
    }

    public boolean isTheEndOfTheWorld() {
        return upperEdge.getB2body().getPosition().y + Edge.HEIGHT_METERS / 2 >= gameViewPort.getWorldHeight() * WORLD_SCREENS;
    }

    private void updateCamera(float dt) {
        // If Hero is dead, we freeze the camera
        if(!player.isDead()) {
            if (isTheEndOfTheWorld()) {
                stopEdges();
            }
        } else {
            stopEdges();
        }

        // Update camera
        shaker.update(dt, gameCam, new Vector2(gameViewPort.getWorldWidth() / 2, upperEdge.getB2body().getPosition().y + Edge.HEIGHT_METERS / 2 - gameViewPort.getWorldHeight() / 2));

        // Tell our renderer to draw only what our camera can see in our game world.
        tiledMapRenderer.setView(gameCam);
    }

    public B2WorldCreator getCreator() {
        return creator;
    }

    public Boss getBoss() {
        return boss;
    }

    public Edge getBottomEdge() {
        return bottomEdge;
    }

    public Edge getUpperEdge() {
        return upperEdge;
    }

    public int getLevel() {
        return level;
    }

    public Shaker getShaker() {
        return shaker;
    }

    private void renderGame(float delta) {
        // Clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the game map
        tiledMapRenderer.render();

        // Render Box2DDebugLines
        if (DebugConstants.DEBUG_LINES) {
            b2dr.render(world, gameCam.combined);
        }

        // Set our batch to now draw what the gameCam camera sees.
        SpriteBatch batch = game.getGameBatch();
        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();

        // This order is important
        // This determine if a sprite has to be drawn in front or behind another sprite
        renderSplats(batch);
        renderKinematicBridges(batch);
        renderPowerBoxes(batch);
        renderItems(batch);
        renderWeapons(batch);
        renderEnemies(batch);
        renderBoss(batch);
        renderHero(batch);

        batch.end();

        // Render the Hud (bottom layer)
        hud.render(delta);

        // Render the InfoScreen (middle layer)
        infoScreen.render(delta);

        // Render the DimScreen (top layer)
        dimScreen.render(delta);

        // Debug
        if (DebugConstants.DEBUG_LINES) {
            // Set our batch to now draw what the gameCam camera sees.
            ShapeRenderer shapeRenderer = game.getGameShapeRenderer();
            shapeRenderer.setProjectionMatrix(gameCam.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);

            renderDebugPowerBoxes(shapeRenderer);
            renderDebugItems(shapeRenderer);
            renderDebugWeapons(shapeRenderer);
            renderDebugBoss(shapeRenderer);
            renderDebugEnemies(shapeRenderer);
            renderDebugHero(shapeRenderer);

            shapeRenderer.end();
        }
    }

    private boolean isLevelCompleted(float delta) {
        boolean isLevelCompleted = false;

        if (boss.isDisposable()) {
            levelCompletedTime += delta;
            isLevelCompleted = levelCompletedTime > LEVEL_COMPLETED_DELAY_SECONDS;
        }
        return isLevelCompleted;
    }

    private boolean isTimeToShowAds() {
        return levelCompletedTime > LEVEL_COMPLETED_DELAY_SECONDS - 1;
    }

    private boolean isChallengeBegin() {
        return player.getB2body().getPosition().y >= LEVEL_CHALLENGE_BEGIN;
    }

    private void renderHero(SpriteBatch batch) {
       player.draw(batch);
    }

    private void renderKinematicBridges(SpriteBatch batch) {
        for (Bridge bridge : creator.getBridges()) {
            bridge.draw(batch);
        }
    }

    private void renderSplats(SpriteBatch batch) {
        for (Enemy enemy : creator.getEnemies()) {
            if (enemy.isSplat()) {
                enemy.draw(batch);
            }
        }
    }

    private void renderEnemies(SpriteBatch batch) {
        for (Enemy enemy : creator.getEnemies()) {
            if (!enemy.isSplat()) {
                enemy.draw(batch);
            }
        }
    }

    private void renderPowerBoxes(SpriteBatch batch) {
        for (PowerBox powerBox : creator.getPowerBoxes()) {
            powerBox.draw(batch);
        }
    }

    private void renderItems(SpriteBatch batch) {
        for (Item item : creator.getItems())  {
            item.draw(batch);
        }
    }

    private void renderWeapons(SpriteBatch batch) {
        for (Weapon weapon : creator.getWeapons()) {
            weapon.draw(batch);
        }
    }

    private void renderBoss(SpriteBatch batch) {
        boss.draw(batch);
    }

    private void renderDebugHero(ShapeRenderer shapeRenderer) {
        player.renderDebug(shapeRenderer);
    }

    private void renderDebugEnemies(ShapeRenderer shapeRenderer) {
        for (Enemy enemy : creator.getEnemies()) {
            enemy.renderDebug(shapeRenderer);
        }
    }

    private void renderDebugPowerBoxes(ShapeRenderer shapeRenderer) {
        for (PowerBox powerBox : creator.getPowerBoxes()) {
            powerBox.renderDebug(shapeRenderer);
        }
    }

    private void renderDebugItems(ShapeRenderer shapeRenderer) {
        for (Item item : creator.getItems()) {
            item.renderDebug(shapeRenderer);
        }
    }

    private void renderDebugWeapons(ShapeRenderer shapeRenderer) {
        for (Weapon weapon : creator.getWeapons()) {
            weapon.renderDebug(shapeRenderer);
        }
    }

    private void renderDebugBoss(ShapeRenderer shapeRenderer) {
        boss.renderDebug(shapeRenderer);
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

    private void speedUpEdges() {
        upperEdge.speedUp();
        bottomEdge.speedUp();
    }

    private void resumeHero() {
        player.resume();
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

    public InfoScreen getInfoScreen() {
        return infoScreen;
    }

    public DimScreen getDimScreen() {
        return dimScreen;
    }

    private void gameResults(float delta) {
        boolean finish = false;

        /* We evaluate mutual exclusion conditions.
         * A boolean value is used to avoid nested if/else sentences.
         */

        finish = !finish && levelStarts;
        if (finish) {
            if (level == 1 && showGameControllersHelp) {
                infoScreen.showGameControllersHelp();
                showGameControllersHelp = false;
            } else {
                infoScreen.showLetsGo();
                levelStarts = false;
            }
        }

        finish = !finish && player.isTimeToPlayAgain();
        if (finish) {
            allowAds = true;
            player.playAgain();
            startEdges();
        }

        finish = !finish && (player.isTimeToShowAds() || isTimeToShowAds()) && allowAds;
        if (finish) {
            // Advertisement
            showAd();
            allowAds = false;
        }

        finish = !finish && !player.isSilverBulletEnabled() && !player.isDead() && isChallengeBegin();
        if (finish) {
            // Show help
            boss.showChallengeBeginHelp();

            // Disable shooting
            player.disableShooting();

            // Change Hero's weapon
            player.applySilverBullet();

            // Speed up the gamecam
            speedUpEdges();
        }

        // New Screens are evaluated at the end, because they call playScreen.dispose.
        // Dispose method destroys the world among other objects, so b2bodies (like player.getB2body()) are no longer available
        finish = !finish && player.isGameOver() && !newScreen;
        if (finish) {
            newScreen = true;
            ScreenManager.getInstance().showScreen(ScreenEnum.GAME_OVER, ScreenTransitionEnum.COLOR_FADE_BLACK);
        }

        finish = !finish && isLevelCompleted(delta) && !newScreen;
        if (finish) {
            newScreen = true;
            ScreenManager.getInstance().showScreen(ScreenEnum.LEVEL_COMPLETED, ScreenTransitionEnum.SLICE_UP_DOWN_10, level, hud.getScore(), player.getPenalties());
        }
    }

    public void showAd() {
        IAdsController adsController = game.getAdsController();
        if (adsController.isWifiConnected()) {
            adsController.showInterstitialAd(new Runnable() {
                @Override
                public void run() {
                    if (isPlayScreenStatePaused()) {
                        dimScreen.setGameStateRunning();
                    }
                }
            });
        } else {
            Gdx.app.debug(TAG, "**** Not connected to the internet");
        }
    }

    public boolean isPlayScreenStatePaused() {
        return playScreenState == PlayScreenState.PAUSED;
    }

    public void setPlayScreenStatePaused(boolean pauseAudio) {
        this.playScreenState = PlayScreenState.PAUSED;
        if (pauseAudio) {
            pauseAudio();
        }
    }

    public void pauseAudio() {
        AudioManager.getInstance().pauseMusic();
        AudioManager.getInstance().pauseSound();
    }

    public void resumeAudio() {
        if (!player.isDead() && !boss.isDestroyed()) {
            AudioManager.getInstance().resumeMusic();
        }
        AudioManager.getInstance().resumeSound();
    }

    public boolean isPlayScreenStateRunning() {
        return playScreenState == PlayScreenState.RUNNING;
    }

    public void setPlayScreenStateRunning(){
        this.playScreenState = PlayScreenState.RUNNING;
        resumeAudio();
        resumeHero();
    }

    public boolean isPlayScreenStateBreak() {
        return playScreenState == PlayScreenState.BEAK;
    }

    public void setPlayScreenStateBreak(){
        this.playScreenState = PlayScreenState.BEAK;
    }

    public void enemyGetsAway() {
        if (!player.isDead() && !player.isWarmingUp()) {
            if (player.getEndurance() > 1) {
                if (showRedFlashHelp) {
                    showRedFlashHelp = false;
                    infoScreen.showModalRedFlashHelp();
                } else {
                    infoScreen.showRedFlash();
                }
            } else {
                infoScreen.showRedFlash();
            }
            player.decreaseEndurance();
            player.addPenalty();
        }
    }

    @Override
    public InputProcessor getInputProcessor() {
        return getInputProcessor(new GameController(this));
    }

    @Override
    public void render(float delta) {
        // Separate our update logic from render: game logic -> render -> game results

        // Update logic
        gameLogic(delta);

        // Render logic
        renderGame(delta);

        // Analyze game results
        if (playScreenState == PlayScreenState.RUNNING) {
            gameResults(delta);
        }
    }

    @Override
    public void buildStage() {
        // Nothing to do here
    }

    @Override
    public void show() {
        // Nothing to do here
    }

    @Override
    public void resize(int width, int height) {
        // Updated the game viewports
        gameViewPort.update(width, height);
        hud.getViewport().update(width, height);
        dimScreen.getViewport().update(width, height);
        infoScreen.getViewport().update(width, height);
    }

    @Override
    public void pause() {
        dimScreen.setGameStatePaused();
    }

    @Override
    public void dispose() {
        // Dispose of all our opened resources
        super.dispose();
        map.dispose();
        tiledMapRenderer.dispose();
        world.dispose();
        if (DebugConstants.DEBUG_LINES) {
            b2dr.dispose();
        }
        hud.dispose();
        dimScreen.dispose();
        infoScreen.dispose();
    }
}
