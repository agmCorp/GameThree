package uy.com.agm.gamethree.game;

import uy.com.agm.gamethree.admob.DummyAdsController;
import uy.com.agm.gamethree.admob.IAdsController;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rafaskoberg.gdx.typinglabel.TypingConfig;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.playservices.IPlayServices;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;
import uy.com.agm.gamethree.screens.util.ScreenTransitionEnum;

public class GameThree extends DirectedGame {
    private static final String TAG = GameThree.class.getName();

    private SpriteBatch gameBatch;
    private ShapeRenderer gameShapeRenderer;
    private IAdsController adsController;
    private IPlayServices playServices;

    public GameThree(IAdsController adsController, IPlayServices playServices){
        this.adsController = adsController;
        this.playServices = playServices;
    }

    @Override
    public void create() {
        // Debug
        if (DebugConstants.DEBUG_MODE) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_INFO);
            Gdx.app.log(TAG, "**** Debug messages not enabled (set DEBUG_MODE = true to enable them) ****");
        }

        // Load preferences and settings
        GameSettings.getInstance().load();

        // Set TypingConfig new line character interval multiplier
        TypingConfig.INTERVAL_MULTIPLIERS_BY_CHAR.put('\n', 0);

        // Constructs a new SpriteBatch
        gameBatch = new SpriteBatch();

        // Constructs a new ShapeRenderer for debugging
        gameShapeRenderer = new ShapeRenderer();

        // Set a splash screen
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen(ScreenEnum.SPLASH, ScreenTransitionEnum.ROTATING);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.getInstance().dispose();
        gameBatch.dispose();
        gameShapeRenderer.dispose();
    }

    public SpriteBatch getGameBatch() {
        return gameBatch;
    }

    public ShapeRenderer getGameShapeRenderer() {
        return gameShapeRenderer;
    }

    public IAdsController getAdsController() {
        return adsController;
    }

    public IPlayServices getPlayServices() {
        return playServices;
    }
}
