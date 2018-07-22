package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.screens.AbstractScreen;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 *
 */

public class DimScreen extends AbstractScreen {
    private static final String TAG = DimScreen.class.getName();

    // Constants
    private static final float BUTTON_WIDTH = 100.0f;
    private static final float BUTTON_SIZE_SMALL = 30.0f;
    private static final float DIM_ALPHA = 0.5f;

    private PlayScreen screen;

    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleBig;

    private Table centerTable;
    private Label messageLabel;

    private Table buttonsTable;
    private ImageButton pause;
    private ImageButton resume;
    private ImageButton quit;
    private Stack stack;
    private Cell stackCell;

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
    }

    private void defineCenterTable() {
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
        messageLabel = new Label("MESSAGE", labelStyleBig);
        messageLabel.setAlignment(Align.center);

        // Add values
        centerTable.add(messageLabel);

        // Add our table to the stage
        addActor(centerTable);

        // Initially hidden
        centerTable.setVisible(false);
    }

    private void defineButtonsTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Define a new table used to display pause, resume and quit buttons
        buttonsTable = new Table();

        // Debug lines
        buttonsTable.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        buttonsTable.top();

        // Make the container fill the entire stage
        buttonsTable.setFillParent(true);

        // Define buttons
        pause = new ImageButton(new TextureRegionDrawable(assetScene2d.getPause()),
                new TextureRegionDrawable(assetScene2d.getPausePressed()));

        // WA: The pause button will take the entire cell (stackCell), but its image will be placed on the
        // top left corner of the cell with scaling fit.
        pause.getImage().setAlign(Align.topLeft);
        pause.getImage().setScaling(Scaling.fit);
        pause.left();

        resume = new ImageButton(new TextureRegionDrawable(assetScene2d.getResume()),
                new TextureRegionDrawable(assetScene2d.getResumePressed()));
        quit = new ImageButton(new TextureRegionDrawable(assetScene2d.getQuit()),
                new TextureRegionDrawable(assetScene2d.getQuitPressed()));

        // Add values
        stack = new Stack();
        stack.add(pause);
        stack.add(resume);
        stackCell = buttonsTable.add(stack).left(); // Pause and Resume are overlapped
        buttonsTable.add(quit).right().expandX();

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

        resume.addListener(
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

        quit.addListener(
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        quit();
                    }

                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });

        // Add the table to the stage
        addActor(buttonsTable);

        showPauseButton();
    }

    private void showPauseButton() {
        stackCell.size(BUTTON_WIDTH, BUTTON_SIZE_SMALL).left().top();
        buttonsTable.pack();
        pause.setVisible(true);
        resume.setVisible(false);
        quit.setVisible(false);
    }

    private void showResumeButton() {
        stackCell.size(quit.getWidth(), quit.getHeight()).left();
        buttonsTable.pack();
        pause.setVisible(false);
        resume.setVisible(true);
        quit.setVisible(true);
    }

    public void setGameStatePaused() {
        showResumeButton();
        buttonsTable.setVisible(true);
        showMessage(i18NGameThreeBundle.format("dimScreen.pauseMessage"));

        InfoScreen infoScreen = screen.getInfoScreen();
        if (infoScreen.isModalVisible()) { // Game already paused
            screen.pauseAudio();
            infoScreen.disableModal();
        } else {
            screen.setPlayScreenStatePaused(true);
        }
    }

    public void hideButtons() {
        buttonsTable.setVisible(false);
    }

    public void showButtons() {
        buttonsTable.setVisible(true);
    }

    public void setGameStateRunning() {
        hideMessage();
        showPauseButton();

        InfoScreen infoScreen = screen.getInfoScreen();
        if (infoScreen.isModalVisible()) { // Game must remain on pause
            buttonsTable.setVisible(false);
            infoScreen.enableModal();
            screen.resumeAudio();
        } else {
            screen.setPlayScreenStateRunning();
        }
    }

    private void quit() {
        if (getMessage().equals(i18NGameThreeBundle.format("dimScreen.confirm"))) {
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
        } else {
            showMessage(i18NGameThreeBundle.format("dimScreen.confirm"));
        }
    }

    private String getMessage() {
        String message = "";
        if (messageLabel.isVisible()) {
            message = messageLabel.getText().toString();
        }
        return message;
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
        centerTable.setVisible(true);
    }

    private void hideMessage() {
        messageLabel.setVisible(false);
        centerTable.setVisible(false);
    }

    @Override
    public void buildStage() {
        defineCenterTable();
        defineButtonsTable();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render(float delta) {
        // Calling to Stage methods
        if (screen.isPlayScreenStateRunning()) {
            super.act(delta);
        }
        super.draw();
    }
}
