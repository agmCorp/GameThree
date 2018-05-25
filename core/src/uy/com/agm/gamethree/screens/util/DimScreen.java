package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamethree.assets.Assets;
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
    private static final float BUTTONS_PAD = 20.0f;
    private static final float BUTTON_WIDTH = 100.0f;
    private static final float DIM_SCREEN_ALPHA = 0.5f;

    private PlayScreen screen;

    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleBig;
    private Label.LabelStyle labelStyleSmall;

    private Table centerTable;
    private Label messageLabel;

    private Table buttonsTable;
    private Label pauseLabel;
    private Label resumeLabel;
    private Label quitLabel;

    private Stack stack;

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

        labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Dim Texture
        Pixmap pixmap = new Pixmap(V_WIDTH, V_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, DIM_SCREEN_ALPHA);
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
        centerTable.setDebug(PlayScreen.DEBUG_MODE);

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
        // Define a new table used to display pause, resume and quit buttons
        buttonsTable = new Table();

        // Debug lines
        buttonsTable.setDebug(PlayScreen.DEBUG_MODE);

        // Bottom-Align table
        buttonsTable.bottom().padLeft(BUTTONS_PAD).padRight(BUTTONS_PAD);

        // Make the container fill the entire stage
        buttonsTable.setFillParent(true);

        // Define labels based on labelStyle
        pauseLabel = new Label(i18NGameThreeBundle.format("dimScreen.pause"), labelStyleSmall);
        quitLabel = new Label(i18NGameThreeBundle.format("dimScreen.quit"), labelStyleSmall);
        quitLabel.setAlignment(Align.right);
        resumeLabel = new Label(i18NGameThreeBundle.format("dimScreen.resume"), labelStyleSmall);

        // Add values
        stack = new Stack();
        stack.add(pauseLabel);
        stack.add(resumeLabel);
        buttonsTable.add(stack).width(BUTTON_WIDTH).left().expandX(); // Pause and Resume texts overlapped
        buttonsTable.add(quitLabel).width(BUTTON_WIDTH).right().expandX();

        // Events
        pauseLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        setGameStatePaused();
                        return true;
                    }
                });

        resumeLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        setGameStateRunning();
                        return true;
                    }
                });

        quitLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        quit();
                        return true;
                    }
                });

        // Add the table to the stage
        addActor(buttonsTable);

        // Initially hidden
        resumeLabel.setVisible(false);
        quitLabel.setVisible(false);
    }

    public void setGameStatePaused() {
        pauseLabel.setVisible(false);
        resumeLabel.setVisible(true);
        quitLabel.setVisible(true);
        buttonsTable.setVisible(true);
        showMessage(i18NGameThreeBundle.format("dimScreen.pauseMessage"));
        screen.setPlayScreenStatePaused(true);
    }

    public void hideButtons() {
        buttonsTable.setVisible(false);
    }

    public void showButtons() {
        buttonsTable.setVisible(true);
    }

    private void setGameStateRunning() {
        hideMessage();
        pauseLabel.setVisible(true);
        resumeLabel.setVisible(false);
        quitLabel.setVisible(false);
        if (!screen.getInfoScreen().isModalVisible()) {
            screen.setPlayScreenStateRunning();
        } else {
            buttonsTable.setVisible(false);
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
        if (screen.getPlayScreenState() == PlayScreen.PlayScreenState.RUNNING) {
            super.act(delta);
        }
        super.draw();
    }
}
