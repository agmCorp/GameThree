package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;
import uy.com.agm.gamethree.screens.util.ScreenTransitionEnum;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public abstract class AbstractScreen extends Stage implements Screen {
    private static final String TAG = AbstractScreen.class.getName();

    // Constants

    // GUI Width (pixels)
    // Visible game world is V_WIDTH / PPM meters wide
    public static final int V_WIDTH = 480;

    // GUI Height (pixels)
    // Visible game world is V_WIDTH / PPM meters tall
    public static final int V_HEIGHT = 800;

    // GUI util
    protected static final float PAD = 30.0f;
    public static final Color COLOR_LABEL_PRESSED = new Color(0xad5D00ff);
    public static final Color DEFAULT_COLOR = Color.WHITE;

    protected AbstractScreen() {
        // We don't scale the viewPort (see PlayScreen constructor), so we work with pixels
        super(new FitViewport(V_WIDTH, V_HEIGHT, new OrthographicCamera()));
    }

    // Subclasses must load actors in this method
    public abstract void buildStage();

    @Override
    public void render(float delta) {
        clearScreen();

        // Calling to Stage methods
        super.act(delta);
        super.draw();

        // Back button Android
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            goBack();
        }
    }

    protected void clearScreen() {
        // Clear the screen with white
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    protected void goBack() {
        playClick();
        ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_DOWN);
    }

    protected void playClick() {
        // Audio FX
        AudioManager.getInstance().stopSound();
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
    }

    public InputProcessor getInputProcessor() {
        return this;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);

        // Back button Android
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}