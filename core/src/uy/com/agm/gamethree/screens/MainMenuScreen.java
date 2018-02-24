package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

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
        AudioManager.getInstance().play(Assets.getInstance().getMusic().getSongMainMenu());
    }

    @Override
    public void buildStage() {
        // I18n
        I18NBundle i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Set table structure
        Table table = new Table();

        // Design
        table.setBackground(new TextureRegionDrawable(Assets.getInstance().getScene2d().getTable()));

        // Debug lines
        table.setDebug(Constants.DEBUG_MODE);

        // Center-Align table
        table.center();

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        // Define our labels based on labelStyle
        Label menuLabel = new Label(i18NGameThreeBundle.format("mainMenu.title"), labelStyleBig);
        Label startGameLabel = new Label(i18NGameThreeBundle.format("mainMenu.startGame"), labelStyleNormal);
        Label settingsLabel = new Label(i18NGameThreeBundle.format("mainMenu.settings"), labelStyleNormal);
        Label exitGameLabel = new Label(i18NGameThreeBundle.format("mainMenu.exitGame"), labelStyleNormal);

        // Add values
        table.add(menuLabel);
        table.row();
        table.add(startGameLabel).padTop(Constants.PAD_TOP);
        table.row();
        table.add(settingsLabel).padTop(Constants.PAD_TOP);
        table.row();
        table.add(exitGameLabel).padTop(Constants.PAD_TOP * 2);

        // Events
        startGameLabel.addListener( UIFactory.createListener(ScreenEnum.SELECT_LEVEL) );
        settingsLabel.addListener( UIFactory.createListener(ScreenEnum.SETTINGS) );
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