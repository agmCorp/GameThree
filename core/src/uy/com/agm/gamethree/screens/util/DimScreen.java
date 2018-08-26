package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGM on 1/18/2018.
 *
 */

public class DimScreen extends AbstractScreen {
    private static final String TAG = DimScreen.class.getName();

    // Constants
    private static final float BUTTON_WIDTH = 100.0f;
    private static final float BUTTON_SIZE_SMALL = 30.0f;
    private static final float DIM_ALPHA = 0.7f;
    private static final int COLUMNS = 3;
    private static final float RESIZE_FACTOR = 0.8f;
    private static final float FADE_DURATION = 0.5f;

    private PlayScreen screen;

    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleBig;

    private Table centerTable;
    private Label messageLabel;

    private Table upperTable;
    private ImageButton pause;
    private ImageButton close;
    private ImageButton music;
    private ImageButton sound;
    private Cell pauseCell;
    private GameSettings prefs;
    private float defaultVolMusic;
    private float defaultVolSound;

    private TextureRegion dim;

    public DimScreen(PlayScreen screen) {
        super();

        // Define tracking variables
        this.screen = screen;

        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Personal fonts
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        // Dim Texture
        Pixmap pixmap = new Pixmap(V_WIDTH, V_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, DIM_ALPHA);
        pixmap.fill();
        dim = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();

        // Preferences
        prefs = GameSettings.getInstance();
        defaultVolMusic = prefs.isMusic() ? prefs.getVolMusic() : GameSettings.DEFAULT_VOLUME;
        defaultVolSound = prefs.isSound() ? prefs.getVolSound() : GameSettings.DEFAULT_VOLUME;
    }

    private void defineCenterTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Define a new table used to display a message
        centerTable = new Table();

        // Background color
        centerTable.setBackground(new TextureRegionDrawable(dim));

        // Debug lines
        centerTable.setDebug(DebugConstants.DEBUG_LINES);

        // Center-Align table
        centerTable.center();

        // Make the table fill the entire stage
        centerTable.setFillParent(true);

        // Define a label based on labelStyle
        messageLabel = new Label(i18NGameThreeBundle.format("dimScreen.pauseMessage"), labelStyleBig);
        messageLabel.setAlignment(Align.center);

        // Define buttons and images
        ImageButton levels = new ImageButton(new TextureRegionDrawable(assetScene2d.getLevels()),
                new TextureRegionDrawable(assetScene2d.getLevelsPressed()));

        ImageButton reload = new ImageButton(new TextureRegionDrawable(assetScene2d.getReload()),
                new TextureRegionDrawable(assetScene2d.getReloadPressed()));

        ImageButton playSmall = new ImageButton(new TextureRegionDrawable(assetScene2d.getPlaySmall()),
                new TextureRegionDrawable(assetScene2d.getPlaySmallPressed()));

        ImageButton home = new ImageButton(new TextureRegionDrawable(assetScene2d.getHome()),
                new TextureRegionDrawable(assetScene2d.getHomePressed()));
        // WA: To reduce the size of the button without losing the pressed effect,
        // we resize its image but not its cell.
        home.getImage().setScale(RESIZE_FACTOR);

        music = new ImageButton(new TextureRegionDrawable(assetScene2d.getMusic()),
                new TextureRegionDrawable(assetScene2d.getMusicPressed()),
                new TextureRegionDrawable(assetScene2d.getMusicChecked()));
        music.setChecked(!prefs.isMusic());
        // WA: To reduce the size of the button without losing the pressed effect,
        // we resize its image but not its cell.
        music.getImage().setScale(RESIZE_FACTOR);

        sound = new ImageButton(new TextureRegionDrawable(assetScene2d.getSound()),
                new TextureRegionDrawable(assetScene2d.getSoundPressed()),
                new TextureRegionDrawable(assetScene2d.getSoundChecked()));
        sound.setChecked(!prefs.isSound());
        // WA: To reduce the size of the button without losing the pressed effect,
        // we resize its image but not its cell.
        sound.getImage().setScale(RESIZE_FACTOR);

        // Add values
        centerTable.add(messageLabel).colspan(COLUMNS);
        centerTable.row().pad(AbstractScreen.PAD / 2);
        centerTable.add(levels).size(levels.getWidth(), levels.getHeight());
        centerTable.add(reload).size(reload.getWidth(), reload.getHeight());
        centerTable.add(playSmall).size(playSmall.getWidth(), playSmall.getHeight());
        centerTable.row();

        // Auxiliary table
        Table aux = new Table();
        aux.setDebug(DebugConstants.DEBUG_LINES);

        // WA: Center alignment
        float width = home.getWidth();
        aux.defaults().padLeft(width - RESIZE_FACTOR * width);

        // Add values
        aux.add(home).size(home.getWidth(), home.getHeight());
        aux.add(music).size(music.getWidth(), music.getHeight());
        aux.add(sound).size(sound.getWidth(), sound.getHeight());
        centerTable.add(aux).colspan(COLUMNS);

        // Events
        levels.addListener(UIFactory.screenNavigationListener(ScreenEnum.SELECT_LEVEL));
        reload.addListener(UIFactory.screenNavigationListener(ScreenEnum.PLAY_GAME, screen.getLevel()));
        playSmall.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        setGameStateRunning();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
        home.addListener(UIFactory.screenNavigationListener(ScreenEnum.MAIN_MENU));
        music.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        toggleMusic();
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
        sound.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        toggleSound();
                        save();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });

        // Add our table to the stage
        addActor(centerTable);

        // Initially hidden with alpha 0 and touch events disabled
        centerTable.getColor().a = 0;
        centerTable.setTouchable(Touchable.disabled);
    }


    private void toggleMusic() {
        boolean musicOn = !music.isChecked();
        prefs.setVolMusic(musicOn ? defaultVolMusic : 0);
        prefs.setMusic(musicOn);
    }

    private void toggleSound() {
        boolean soundOn = !sound.isChecked();
        prefs.setVolSound(soundOn ? defaultVolSound : 0);
        prefs.setSound(soundOn);
    }

    private void save() {
        GameSettings.getInstance().save();
    }

    private void defineUpperTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Define a new table used to display pause and close buttons
        upperTable = new Table();

        // Debug lines
        upperTable.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        upperTable.top();

        // Make the container fill the entire stage
        upperTable.setFillParent(true);

        // Define buttons
        pause = new ImageButton(new TextureRegionDrawable(assetScene2d.getPause()),
                new TextureRegionDrawable(assetScene2d.getPausePressed()));

        // WA: The pause button will take the entire cell (stackCell), but its image will be placed on the
        // top left corner of the cell with scaling fit.
        pause.getImage().setAlign(Align.topLeft);
        pause.getImage().setScaling(Scaling.fit);
        pause.left();

        close = new ImageButton(new TextureRegionDrawable(assetScene2d.getCross()),
                new TextureRegionDrawable(assetScene2d.getCrossPressed()));

        // Add values
        pauseCell = upperTable.add(pause).left().expandX();
        upperTable.add(close).right().expandX();

        // Events
        pause.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        setGameStatePaused();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });

        close.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        setGameStateRunning();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });

        // Add the table to the stage
        addActor(upperTable);

        showPauseButton();
    }

    private void showPauseButton() {
        pauseCell.size(BUTTON_WIDTH, BUTTON_SIZE_SMALL).left().top();
        upperTable.pack();
        pause.setVisible(true);
        close.setVisible(false);
    }

    private void showCloseButton() {
        pauseCell.size(close.getWidth(), close.getHeight()).right();
        upperTable.pack();
        pause.setVisible(false);
        close.setVisible(true);
    }

    public void setGameStatePaused() {
        showCloseButton();
        upperTable.setVisible(true);
        showPause();

        InfoScreen infoScreen = screen.getInfoScreen();
        if (infoScreen.isModalVisible()) { // Game already paused
            screen.pauseAudio();
            infoScreen.disableModal();
        } else {
            screen.setPlayScreenStatePaused(true);
        }
    }

    public void hideButtons() {
        upperTable.setVisible(false);
    }

    public void showButtons() {
        upperTable.setVisible(true);
    }

    public void setGameStateRunning() {
        hidePause();
        showPauseButton();

        InfoScreen infoScreen = screen.getInfoScreen();
        if (infoScreen.isModalVisible()) { // Game must remain on pause
            upperTable.setVisible(false);
            infoScreen.enableModal();
            screen.resumeAudio();
        } else {
            screen.setPlayScreenStateRunning();
        }
    }

    private void showPause() {
        centerTable.addAction(sequence(fadeIn(FADE_DURATION), run(new Runnable() {
            public void run () {
                centerTable.setTouchable(Touchable.enabled);
            }
        })));
    }

    private void showPauseFast() {
        centerTable.getColor().a = 1;
        centerTable.setTouchable(Touchable.enabled);
    }

    private void hidePause() {
        centerTable.addAction(sequence(run(new Runnable() {
            public void run () {
                centerTable.setTouchable(Touchable.disabled);
            }
        }), fadeOut(FADE_DURATION)));
    }

    @Override
    public void buildStage() {
        defineCenterTable();
        defineUpperTable();
    }

    @Override
    protected void clearScreen() {
        // Nothing to do here
    }

    @Override
    protected void goBack() {
        // Nothing to do here
    }
}
