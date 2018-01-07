package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 12/23/2017.
 */

public class GameOverScreen implements Screen {
    private static final String TAG = GameOverScreen.class.getName();

    // Scene2D.ui Stage and its own Viewport for GameOver
    public Stage stage; // todo Stage it's an InputListener, so basically all the input clicks can be redirected to the stage and the stage can handle it
    public Viewport viewport;

    private GameThree game;

    public GameOverScreen(GameThree game) {
        this.game = game;

        // Setup the GameOver viewport using a new camera separate from our gamecam
        // Define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.instance.fonts.defaultBig;

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.instance.fonts.defaultSmall;

        // Define our labels based on labelStyle
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("GAME OVER", labelStyleBig);
        Label playAgainLabel = new Label("Touch to play again", labelStyleSmall);
        table.add(gameOverLabel).center();
        table.row();
        table.add(playAgainLabel).padTop(10.0f).center();

        stage.addActor(table);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new PlayScreen(game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }
}
