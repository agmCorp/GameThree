package uy.com.agm.gamethree.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;

public class GameThree extends Game {
    private static final String TAG = GameThree.class.getName();

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        // Debug
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Load assets
        Assets.instance.init(new AssetManager());

        // Load preferences and settings
        GameSettings.getInstance().load();

        // Constructs a new SpriteBatch
        batch = new SpriteBatch();

        // Constructs a new ShapeRenderer for debugging
        shapeRenderer = new ShapeRenderer();

        // Set the new Screen
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.instance.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }
}
