package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.UIFactory;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.ui.HealthBar;

/**
 * Created by AGM on 1/18/2018.
 */

public class MainMenuScreen extends AbstractScreen {
    private static final String TAG = MainMenuScreen.class.getName();
    private HealthBar helthBar; // todo

    public MainMenuScreen() {
        super();
        //  Start playing music
        AudioManager.getInstance().play(Assets.getInstance().getMusic().getSongMainMenu());
    }

    @Override
    public void buildStage() {
        // Personal fonts
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

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

        // Debug lines
        table.setDebug(Constants.DEBUG_MODE);

/* */


table.row();
        helthBar = new HealthBar(40, 40, 7);

        table.add(helthBar).padTop(Constants.PAD_TOP).center().expandX();;

        table.row();
        Label decLabel = new Label("disminuir", labelStyleNormal);
        table.add(decLabel).padTop(Constants.PAD_TOP).center();

        decLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        helthBar.decrease();
                        return true;
                    }
                });
/* */

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

        // Adds created table to stage
        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}