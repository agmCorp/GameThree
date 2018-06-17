package uy.com.agm.gamethree.game;

import com.admob.IAdsController;
import com.admob.DummyAdsController;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;

public class GameThree extends Game {
    private static final String TAG = GameThree.class.getName();

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private IAdsController adsController;

    public GameThree(IAdsController adsController){
        if (adsController != null) {
            this.adsController = adsController;
        } else {
            this.adsController = new DummyAdsController();
        }
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

        // Constructs a new SpriteBatch
        batch = new SpriteBatch();

        // Constructs a new ShapeRenderer for debugging
        shapeRenderer = new ShapeRenderer();

        // Set a splash screen
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen(ScreenEnum.SPLASH);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.getInstance().dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public IAdsController getAdsController() {
        return adsController;
    }
}
