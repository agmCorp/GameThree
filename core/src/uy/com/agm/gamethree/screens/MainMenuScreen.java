package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;

/**
 * Created by AGM on 1/18/2018.
 */

public class MainMenuScreen extends AbstractScreen {
    private static final String TAG = MainMenuScreen.class.getName();

    public MainMenuScreen() {
        super();
    }

    @Override
    public void buildStage() {
        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.instance.fonts.defaultBig;

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.instance.fonts.defaultSmall;

        // Define our labels based on labelStyle
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label menu = new Label("Menu", labelStyleBig);
        Label startGame = new Label("Start game", labelStyleSmall);
        Label options = new Label("Options", labelStyleSmall);
        Label exitGame = new Label("Exit game", labelStyleSmall);

        table.add(menu).center();
        table.row();
        table.add(startGame).padTop(10.0f).center();
        table.row();
        table.add(options).padTop(10.0f).center();
        table.row();
        table.add(exitGame).padTop(10.0f).center();

        addActor(table);

        // Setting listeners
        startGame.addListener( UIFactory.createListener(ScreenEnum.LEVEL_SELECT) );
        options.addListener( UIFactory.createListener(ScreenEnum.PREFERENCES) );
        exitGame.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Gdx.app.exit();
                        return false;
                    }
                });
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}