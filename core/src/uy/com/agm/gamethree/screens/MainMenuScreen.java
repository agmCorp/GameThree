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

/**
 * Created by AGM on 1/18/2018.
 */

public class MainMenuScreen extends AbstractScreen {
    private static final String TAG = MainMenuScreen.class.getName();

    private ProgressBar progressBar; // todo
    private final static int CANTDISPAROS = 25; // TODO
    private int contador = CANTDISPAROS; // TODO

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
/* */


table.row();
        progressBar = createProgressBar();
        init();
        table.add(progressBar).padTop(Constants.PAD_TOP).center();
        table.row();
        Label decLabel = new Label("disminuir", labelStyleNormal);
        table.add(decLabel).padTop(Constants.PAD_TOP).center();

        decLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        decrease();
                        return true;
                    }
                });
/* */
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

    private ProgressBar createProgressBar(){
        Drawable knob = createKnob();
        Drawable background = createLoadingBar();
        ProgressBar.ProgressBarStyle style= new ProgressBar.ProgressBarStyle(background, knob);
        style.knobBefore = knob;
        //return new ProgressBar(LOADING_MIN,LOADING_MAX,STEP_SIZE,false,style);
        return new ProgressBar(0, 100, 1, false, style);
    }

    private Drawable createLoadingBar() {
        // CONFIRMADO DEBO HACER DISPOSE DE LAS TEXTURES a las que hice new!
        Pixmap pixmap = new Pixmap(100, 5, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        Drawable background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        return background;
    }

    private Drawable createKnob() {
        //TiledDrawable knob = new TiledDrawable(new TextureRegion(new Texture(Gdx.files.internal(Constants.SLIDER_BACKGROUND))));
        Pixmap pixmap = new Pixmap(1, 5, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        TiledDrawable knob = new TiledDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        return knob;
    }

    private void init() {
        progressBar.setValue(100);
    }

    private void decrease() {
        contador--;
        if (contador > 0) {
            progressBar.setValue(progressBar.getValue() - MathUtils.round(100.0f / CANTDISPAROS));
        } else {
            progressBar.setValue(0);
        }

    }
}