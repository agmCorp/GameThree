package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class MainMenuScreen extends AbstractScreen {
    private static final String TAG = MainMenuScreen.class.getName();

    private Label.LabelStyle labelStyleBig;
    private Label.LabelStyle labelStyleNormal;

    public MainMenuScreen() {
        super();
        //  Start playing music
        AudioManager.getInstance().play(Assets.getInstance().getMusic().getSongMainMenu());
    }

    @Override
    public void buildStage() {
        // Personal fonts
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Define our labels based on labelStyle
        Label startGameLabel = new Label("Start game", labelStyleNormal);
        Label settingsLabel = new Label("Settings", labelStyleNormal);
        Label exitGameLabel = new Label("Exit game", labelStyleNormal);

        // Set table structure
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(new Label("Menu", labelStyleBig)).center();
        table.row();
        table.add(startGameLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(settingsLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(exitGameLabel).padTop(Constants.PAD_TOP).center();

        // Debug lines
        table.setDebug(Constants.DEBUG_MODE);

        // Setting listeners
        startGameLabel.addListener( UIFactory.createListener(ScreenEnum.LEVEL_SELECT) );
        settingsLabel.addListener( UIFactory.createListener(ScreenEnum.PREFERENCES) );
        exitGameLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().play(Assets.getInstance().getSounds().getClick());
                        Gdx.app.exit();
                        return false;
                    }
                });

        // Adds created table to stage
        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}