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
import uy.com.agm.gamethree.ui.HealthBar;

/**
 * Created by AGM on 1/18/2018.
 */

public class Hud extends AbstractScreen {
    private static final String TAG = Hud.class.getName();

    private PlayScreen screen;
    private Label.LabelStyle labelStyle;

    private Table upperTable;
    private int score;
    private Label scoreValueLabel;
    private int level;
    private Label levelValueLabel;
    private int time;
    private float timeCount;
    private Label timeValueLabel;
    private int lives;
    private Label livesValueLabel;

    private Table powerTable;
    private Label powerNameLabel;
    private int powerTime;
    private float powerTimeCount;
    private Label powerTimeValueLabel;

    private Table centerTable;
    private Label messageLabel;

    private Table bottomTable;

    private Table fpsTable;
    private Label fpsLabel;
    private int fps;
    private Label fpsValueLabel;

    private Table healthBarTable;
    private Label enemyNameLabel;
    private HealthBar healthBar;

    private Table buttonsTable;
    private Label pauseLabel;
    private Label resumeLabel;
    private Label quitLabel;

    private boolean timeIsUp; // True when the level time reaches 0

    public Hud(PlayScreen screen, Integer level, Integer time, Integer lives) {
        super();

        // Define tracking variables
        this.screen = screen;
        labelStyle = new Label.LabelStyle();
        score = 0;
        this.level = level;
        this.time = time;
        timeCount = 0;
        this.lives = lives;
        powerTime = 0;
        powerTimeCount = 0;
        fps = 0;
        healthBar = new HealthBar();
        timeIsUp = false;
    }

    private void defineUpperTable() {
        // Define a table used to organize our hud's labels
        upperTable = new Table();

        // Cell height
        //upperTable.defaults().height(Constants.HUD_CELL_HEIGHT); todo

        // Debug lines
        upperTable.setDebug(Constants.DEBUG_MODE);

        // Top-Align table
        upperTable.top();

        // Make the table fill the entire stage
        upperTable.setFillParent(true);

        // Personal fonts
        labelStyle.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Add labels to the table giving them all equal width with expandX
        upperTable.add(new Label("SCORE", labelStyle)).expandX();
        upperTable.add(new Label("LEVEL", labelStyle)).expandX();
        upperTable.add(new Label("TIME", labelStyle)).expandX();
        upperTable.add(new Label("LIVES", labelStyle)).expandX();

        // Add a second row to our table
        upperTable.row();

        // Define label values based on labelStyle
        scoreValueLabel = new Label(String.format("%06d", score), labelStyle);
        levelValueLabel = new Label(String.format("%02d", level), labelStyle);
        timeValueLabel = new Label(String.format("%03d", time), labelStyle);
        livesValueLabel = new Label(String.format("%02d", lives), labelStyle);

        // Add values
        upperTable.add(scoreValueLabel).expandX();
        upperTable.add(levelValueLabel).expandX();
        upperTable.add(timeValueLabel).expandX();
        upperTable.add(livesValueLabel).expandX();

        // Add a third row to our table
        upperTable.row();

        // Add power info
        definePowerTable();
        upperTable.add(powerTable).colspan(4);

        // Add table to the stage
        addActor(upperTable);
    }

    private void definePowerTable() {
        // Define a table used to organize the power info
        powerTable = new Table();

        // Cell height
        //powerTable.defaults().height(Constants.HUD_CELL_HEIGHT); todo

        // Debug lines
        powerTable.setDebug(Constants.DEBUG_MODE);

        // Personal fonts
        labelStyle.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define a label based on labelStyle
        powerNameLabel = new Label("POWERNAME", labelStyle);

        // Add values
        powerTable.add(powerNameLabel).expandX();

        // Add a second row to the table
        powerTable.row();

        // Define a label based on labelStyle
        powerTimeValueLabel = new Label(String.format("%03d", powerTime), labelStyle);

        // Add values
        powerTable.add(powerTimeValueLabel).expandX();

        // Initially hidden
        powerTable.setVisible(false);
    }

    private void defineCenterTable() {
        // Define a new table used to display a message
        centerTable = new Table();

        // Debug lines
        centerTable.setDebug(Constants.DEBUG_MODE);

        // Center-Align table
        centerTable.center();

        // Make the table fill the entire stage
        centerTable.setFillParent(true);

        // Personal fonts
        labelStyle.font = Assets.getInstance().getFonts().getDefaultBig();

        // Define a label based on labelStyle
        messageLabel = new Label("MESSAGE", labelStyle);

        // Add values
        centerTable.add(messageLabel).expandX();

        // Initially hidden
        centerTable.setVisible(false);

        // Add our table to the stage
        addActor(centerTable);
    }

    private void defineBottomTable() {
        // Define a new table used to display a FPS counter and an Enemy's health bar
        bottomTable = new Table();

        // Debug lines
        bottomTable.setDebug(Constants.DEBUG_MODE);

        // Bottom-Align table
        bottomTable.bottom();

        // Make the table fill the entire stage
        bottomTable.setFillParent(true);

        // Add health bar info
        defineHealthBarTable();
        bottomTable.add(healthBarTable).expandX();

        // FPS info
        defineFpsTable();

        if (Constants.DEBUG_MODE) {
            // Add a second row to the table
            bottomTable.row();
            // Add FPS info
            bottomTable.add(fpsTable).expandX();
        }

        // Add the table to the stage
        addActor(bottomTable);
    }

    private void defineFpsTable() {
        // Define a new table used to display a FPS counter
        fpsTable = new Table();

        // Debug lines
        fpsTable.setDebug(Constants.DEBUG_MODE);

        // Personal fonts
        labelStyle.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define a label based on labelStyle
        fpsLabel = new Label("FPS", labelStyle);

        // Add a label to the table giving it equal width with expandX
        fpsTable.add(fpsLabel).expandX();

        // Add a second row to our table
        fpsTable.row();

        // Define a label value based on labelStyle
        fpsValueLabel = new Label(String.format("%02d", fps), labelStyle);

        // Add value
        fpsTable.add(fpsValueLabel).expandX();

        // Hidden if not in debug mode
        fpsTable.setVisible(Constants.DEBUG_MODE);
    }

    private void defineHealthBarTable() {
        // Define a new table used to display an enemy's health bar
        healthBarTable = new Table();

        // Debug lines
        healthBarTable.setDebug(Constants.DEBUG_MODE);

        // Personal fonts
        labelStyle.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define a label based on labelStyle
        enemyNameLabel = new Label("ENEMY_NAME", labelStyle);

        // Add value
        healthBarTable.add(enemyNameLabel).expandX();

        // Add a second row to our table
        healthBarTable.row();

        // Add health bar
        healthBarTable.add(healthBar).expandX().padBottom(Constants.HUD_HEALTHBAR_PADBOTTOM);

        // Initially hidden
        healthBarTable.setVisible(false);
    }

    private void defineButtonsTable() {
        // Define a new table used to display pause, resume and quit buttons
        buttonsTable = new Table();

        // Debug lines
        buttonsTable.setDebug(Constants.DEBUG_MODE);

        // Bottom-Align table
        buttonsTable.bottom().padLeft(Constants.HUD_BUTTONS_PAD).padRight(Constants.HUD_BUTTONS_PAD);

        // Make the container fill the entire stage
        buttonsTable.setFillParent(true);

        // Personal fonts
        labelStyle.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define labels based on labelStyle
        pauseLabel = new Label("||", labelStyle);
        quitLabel = new Label("QUIT GAME", labelStyle);
        resumeLabel = new Label("RESUME GAME", labelStyle);

        // Add values
        buttonsTable.add(pauseLabel).width(Constants.HUD_PAUSE_WIDTH).left().expandX();
        buttonsTable.add(resumeLabel).left().expandX();
        buttonsTable.add(quitLabel).right().expandX();

        // Events
        pauseLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        setGameStatePaused();
                        return true;
                    }
                });

        // Events
        resumeLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        setGameStateRunning();
                        return true;
                    }
                });

        // Events
        quitLabel.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

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
        showMessage("PAUSED");
        screen.setPlayScreenStatePaused();
    }

    public void setGameStateRunning() {
        pauseLabel.setVisible(true);
        resumeLabel.setVisible(false);
        quitLabel.setVisible(false);
        hideMessage();
        screen.setPlayScreenStateRunning();
    }

    @Override
    public void buildStage() {
        defineUpperTable();
        defineCenterTable();
        defineBottomTable();
        defineButtonsTable();
    }

    public void showPowerInfo(String powerName, int maxTime) {
        powerNameLabel.setText(powerName);
        powerTime = maxTime;
        powerTimeValueLabel.setText(String.format("%03d", powerTime));
        powerTable.setVisible(true);
    }

    public void hidePowerInfo() {
        powerTable.setVisible(false);
    }

    public boolean isPowerInfoVisible() {
        return powerTable.isVisible();
    }


    public void showMessage(String message) {
        messageLabel.setText(message);
        centerTable.setVisible(true);
    }

    public void showHurryUpMessage() {
        showMessage("HURRY UP!");
    }

    public void showTimeIsUpMessage() {
        showMessage("TIME IS UP!!");
    }

    public void hideMessage() {
        centerTable.setVisible(false);
    }

    public boolean isMessageVisible() {
        return centerTable.isVisible();
    }

    public void showFpsInfo() {
        fpsTable.setVisible(true);
    }

    public void hideFpsInfo() {
        fpsTable.setVisible(false);
    }

    public boolean isFpsInfoVisible() {
        return fpsTable.isVisible();
    }

    public void showHealthBarInfo(String enemyName, int energy) {
        enemyNameLabel.setText(enemyName);
        healthBar.setInitialEnergy(energy);
        healthBarTable.setVisible(true);
    }

    public void hideHealthBarInfo() {
        healthBarTable.setVisible(false);
    }

    public boolean isHealthBarInfoVisible() {
        return healthBarTable.isVisible();
    }

    public void update(float dt) {
        // Update world time
        timeCount += dt;
        if(timeCount >= 1){
            if (time > 0) {
                time--;
                if (time <= Constants.LEVEL_TIMER_NOTIFICATION) {
                    AudioManager.getInstance().play(Assets.getInstance().getSounds().getClock());
                    AudioManager.getInstance().play(Assets.getInstance().getSounds().getBeepB());
                    if (isMessageVisible()) {
                        hideMessage();
                    } else {
                        showHurryUpMessage();
                    }
                }
            } else {
                timeIsUp = true;
            }
            timeValueLabel.setText(String.format("%03d", time));
            timeCount = 0;
        }

        // Update power time
        if (isPowerInfoVisible()) {
            powerTimeCount += dt;
            if (powerTimeCount >= 1) {
                if (powerTime > 0) {
                    powerTime--;
                    if (powerTime <= Constants.POWER_TIMER_NOTIFICATION) {
                        AudioManager.getInstance().play(Assets.getInstance().getSounds().getBeepA());
                    }
                    powerTimeValueLabel.setText(String.format("%03d", powerTime));
                } else {
                    hidePowerInfo();
                }
                powerTimeCount = 0;
            }
        }

        // Update FPS
        if (isFpsInfoVisible()) {
            fps = Gdx.graphics.getFramesPerSecond();
            fpsValueLabel.setText(String.format("%02d", fps));
        }
    }

    public void addScore(int value) {
        score += value;
        scoreValueLabel.setText(String.format("%06d", score));
    }

    public int getScore() {
        return score;
    }

    public void decreaseHealth() {
        healthBar.decrease();
    }

    public boolean isTimeIsUp() {
        return timeIsUp;
    }

    public boolean isPowerTimeUp() {
        return powerTime <= 0;
    }

    public boolean isPowerRunningOut() {
        return powerTime <= Constants.POWER_TIMER_NOTIFICATION;
    }

    public void forcePowerTimeUp() {
        hidePowerInfo();
    }

    public void decreaseLives(int quantity) {
        lives -= quantity;
        livesValueLabel.setText(String.format("%02d", lives));
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void draw () {
        act();
        // Set our batch to now draw what the Hud camera sees.
        super.draw();
    }
}
