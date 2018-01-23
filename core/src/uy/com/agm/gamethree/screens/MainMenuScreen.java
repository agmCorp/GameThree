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

    public MainMenuScreen() {
        super();
        //  Start playing music
        AudioManager.instance.play(Assets.instance.music.getSongMainMenu());
    }

    @Override
    public void buildStage() {
        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.instance.fonts.defaultBig;

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.instance.fonts.defaultNormal;

        // Define our labels based on labelStyle
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label titleLabel = new Label("Menu", labelStyleBig);
        Label startGameLabel = new Label("Start game", labelStyleNormal);
        Label settingsLabel = new Label("Settings", labelStyleNormal);
        Label exitGameLabel = new Label("Exit game", labelStyleNormal);

        table.add(titleLabel).center();
        table.row();
        table.add(startGameLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(settingsLabel).padTop(Constants.PAD_TOP).center();
        table.row();
        table.add(exitGameLabel).padTop(Constants.PAD_TOP).center();

        addActor(table);

        // Setting listeners
        startGameLabel.addListener( UIFactory.createListener(ScreenEnum.LEVEL_SELECT) );
        settingsLabel.addListener( UIFactory.createListener(ScreenEnum.PREFERENCES) );
        exitGameLabel.addListener(
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