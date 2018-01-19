package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;

/**
 * Created by AGM on 1/18/2018.
 */

public abstract class AbstractScreen extends Stage implements Screen {
    private static final String TAG = AbstractScreen.class.getName();

    protected AbstractScreen() {
        super(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, new OrthographicCamera()));
    }

    // Subclasses must load actors in this method
    public abstract void buildStage();

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Calling to Stage methods
        super.act(delta);
        super.draw();

        // Back button Android
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);

        // Back button Android
        // On the MainMenu pressing the back button will exit the app.
        // On other screens the back button will switch the app back to the MainMenu
        boolean catchBAckKey = !(this instanceof MainMenuScreen);
        Gdx.input.setCatchBackKey(catchBAckKey);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}