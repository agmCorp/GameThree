package uy.com.agm.gamethree.screens.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    private static final float DIM_SCREEN_ALPHA = 0.5f;
    private static final float BUTTON_SIZE_NORMAL = 68.0f;
    private static final float BUTTON_SIZE_SMALL = 30.0f;

    private PlayScreen screen;

    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleBig;

    private Table centerTable;
    private Label messageLabel;

    private Table buttonsTable;
    private Image pause;
    private Image resume;
    private Image quit;
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
        buttonsTable.bottom();

        // Make the container fill the entire stage
        buttonsTable.setFillParent(true);

        // Define images
        pause = new Image(Assets.getInstance().getScene2d().getPause());
        resume = new Image(Assets.getInstance().getScene2d().getResume());
        quit = new Image(Assets.getInstance().getScene2d().getQuit());
        quit.setAlign(Align.right);

        // Add values
        stack = new Stack();
        stack.add(pause);
        stack.add(resume);
        stackCell = buttonsTable.add(stack); // Pause and Resume are overlapped
        buttonsTable.add(quit).right().expandX();

        // Events
        pause.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        setGameStatePaused();
                        return true;
                    }
                });

        resume.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        setGameStateRunning();
                        return true;
                    }
                });

        quit.addListener(
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

        showPauseButton();
    }

    private void showPauseButton() {
        stackCell.size(BUTTON_SIZE_SMALL).left().bottom();
        buttonsTable.pack();
        pause.setVisible(true);
        resume.setVisible(false);
        quit.setVisible(false);
    }

    private void showResumeButton() {
        stackCell.size(BUTTON_SIZE_NORMAL).left();
        buttonsTable.pack();
        pause.setVisible(false);
        resume.setVisible(true);
        quit.setVisible(true);
    }

    public void setGameStatePaused() {
        showResumeButton();
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
        showPauseButton();
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
        if (screen.isPlayScreenStateRunning()) {
            super.act(delta);
        }
        super.draw();
    }
}
