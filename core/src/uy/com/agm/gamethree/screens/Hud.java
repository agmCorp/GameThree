package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.ui.HealthBar;

/**
 * Created by AGM on 1/18/2018.
 */

public class Hud extends AbstractScreen {
    private static final String TAG = Hud.class.getName();

    // Hero score/time Tracking Variables
    private int score;
    private int level;
    private int levelTimer;
    private boolean levelTimeIsUp; // True when the world levelTimer reaches 0
    private int lives;
    private float timeCount;
    private int powerTimer;
    private float timeCountPower;
    private boolean powerTimerVisible;
    private int fps;

    // Scene2D widgets
    private Label.LabelStyle labelStyle;
    private Label scoreValueLabel;
    private Label levelValueLabel;
    private Label levelTimerValueLabel;
    private Label livesValueLabel;
    private Label powerLabel;
    private Label powerValueLabel;
    private Label enemyNameLabel;
    private Label fpsValueLabel;
    private Label timeIsUpLabel;
    private Table upperTable;
    private Table timeIsUpTable;
    private Table bottomTable;

    // Health progressBar
    private HealthBar helthBar;

    public Hud(Integer level, Integer levelTimer, Integer lives) {
        super();

        // Define tracking variables
        score = 0;
        this.level = level;
        this.levelTimer = levelTimer;
        levelTimeIsUp = false;
        this.lives = lives;
        timeCount = 0;
        powerTimer = 0;
        timeCountPower = 0;
        powerTimerVisible = false;
        fps = 0;
        helthBar = new HealthBar();

        // Style
        labelStyle = new Label.LabelStyle();
    }

    private void defineUpperTable() {
        // Define a table used to organize our hud's labels
        upperTable = new Table();

        // Debug lines
        upperTable.setDebug(Constants.DEBUG_MODE);

        // Top-Align table
        upperTable.top();

        // Make the table fill the entire stage
        upperTable.setFillParent(true);

        // Personal fonts
        labelStyle.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define labels based on labelStyle
        Label scoreLabel = new Label("SCORE", labelStyle);
        scoreValueLabel = new Label(String.format("%06d", score), labelStyle);

        Label levelLabel = new Label("LEVEL", labelStyle);
        levelValueLabel = new Label(String.format("%02d", level), labelStyle);

        Label levelTimerLabel = new Label("TIME", labelStyle);
        levelTimerValueLabel = new Label(String.format("%03d", levelTimer), labelStyle);

        Label livesLabel = new Label("LIVES", labelStyle);
        livesValueLabel = new Label(String.format("%02d", lives), labelStyle);

        powerLabel = new Label("POWERNAME", labelStyle);
        powerValueLabel = new Label(String.format("%03d", powerTimer), labelStyle);

        // Add labels to the table giving them all equal width with expandX
        upperTable.add(scoreLabel).expandX();
        upperTable.add(levelLabel).expandX();
        upperTable.add(levelTimerLabel).expandX();
        upperTable.add(livesLabel).expandX();

        // Add a second row to our table
        upperTable.row();

        // Values
        upperTable.add(scoreValueLabel).expandX();
        upperTable.add(levelValueLabel).expandX();
        upperTable.add(levelTimerValueLabel).expandX();
        upperTable.add(livesValueLabel).expandX();

        // Add table to the stage
        addActor(upperTable);
    }

    private void defineBottomTable() {
        // Define a new table used to display an Enemy's health bar and a FPS counter
        bottomTable = new Table();

        // Debug lines
        bottomTable.setDebug(Constants.DEBUG_MODE);

        // Bottom-Align table
        bottomTable.bottom();

        // Make the table fill the entire stage
        bottomTable.setFillParent(true);

        // Personal fonts
        labelStyle.font = Assets.getInstance().getFonts().getDefaultSmall();

        // Define a label based on labelStyle
        enemyNameLabel = new Label("ENEMY_NAME", labelStyle);
        Label fpsLabel = new Label("FPS", labelStyle);
        fpsValueLabel = new Label(String.format("%02d", fps), labelStyle);

        if (Constants.DEBUG_MODE) {
            // Add previous label to the table giving it equal width with expandX
            bottomTable.add(fpsLabel).expandX();

            // Add a second row to our table
            bottomTable.row();

            // Value
            bottomTable.add(fpsValueLabel).expandX();
        }

        // Add table to the stage
        addActor(bottomTable);
    }

    private void defineTimeIsUpTable() {
        // Define a new table used to display "Iime is up" message
        timeIsUpTable = new Table();

        // Debug lines
        timeIsUpTable.setDebug(Constants.DEBUG_MODE);

        // Center-Align table
        timeIsUpTable.center();

        // Make the table fill the entire stage
        timeIsUpTable.setFillParent(true);

        // Define a label based on labelStyle
        labelStyleMessage.font = Assets.getInstance().getFonts().getDefaultBig();

        timeIsUpLabel = new Label("TIME IS UP!!", labelStyleMessage);

        // Add our table to the stage
        addActor(timeIsUpTable);
    }

    @Override
    public void buildStage() {
        defineUpperTable();
        defineBottomTable();
        defineTimeIsUpTable();
    }

    public void update(float dt) {
        // Update world levelTimer
        timeCount += dt;
        if(timeCount >= 1){
            if (levelTimer > 0) {
                levelTimer--;
                if (levelTimer <= Constants.LEVEL_TIMER_NOTIFICATION) {
                    AudioManager.getInstance().play(Assets.getInstance().getSounds().getClock());
                    AudioManager.getInstance().play(Assets.getInstance().getSounds().getBeepB());
                }
            } else {
                levelTimeIsUp = true;
            }
            levelTimerValueLabel.setText(String.format("%03d", levelTimer));
            timeCount = 0;
        }

        // Update power levelTimer
        if (powerTimerVisible) {
            timeCountPower += dt;
            if (timeCountPower >= 1) {
                if (powerTimer > 0) {
                    powerTimer--;
                    if (powerTimer <= Constants.POWER_TIMER_NOTIFICATION) {
                        AudioManager.getInstance().play(Assets.getInstance().getSounds().getBeepA());
                    }
                    powerValueLabel.setText(String.format("%03d", powerTimer));
                } else {
                    removePowerLabel();
                }
                timeCountPower = 0;
            }
        }

        // Update FPS
        if (Constants.DEBUG_MODE) {
            fps = Gdx.graphics.getFramesPerSecond();
            fpsValueLabel.setText(String.format("%02d", fps));
        }
    }

    public void addScore(int value) {
        score += value;
        scoreValueLabel.setText(String.format("%06d", score));
    }

    public void setPowerLabel(String powerName, int maxTime) {
        powerTimer = maxTime;
        powerLabel.setText(powerName);
        powerValueLabel.setText(String.format("%03d", powerTimer));

        if (!powerTimerVisible) {
            // Add a third row to our table
            upperTable.row();
            upperTable.add(powerLabel).colspan(4).expandX();
            upperTable.row();
            upperTable.add(powerValueLabel).colspan(4).expandX();
            powerTimerVisible = true;
        }
    }

    public void setHealthBar(String enemyName, int energy) {
        enemyNameLabel.setText(enemyName);
        helthBar.setInitialEnergy(energy);
        helthBar.setFull();
        bottomTable.row();
        bottomTable.add(helthBar).padTop(Constants.PAD_TOP).center();
    }

    public void removeHealthBar() {
        bottomTable.removeActor(enemyNameLabel);
        bottomTable.removeActor(helthBar);
    }

    public void decreaseHealth() {
        helthBar.decrease();
    }

    public void setTimeIsUpLabel() {
        timeIsUpTable.add(timeIsUpLabel).expandX();
    }

    private void removePowerLabel() {
        if (powerTimerVisible) {
            upperTable.removeActor(powerLabel);
            upperTable.removeActor(powerValueLabel);
            powerTimerVisible = false;
        }
    }

    public void removeMessageLabel() {
        timeIsUpTable.removeActor(timeIsUpLabel);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public boolean isLevelTimeIsUp() {
        return levelTimeIsUp;
    }

    public boolean isPowerTimeUp() {
        return !powerTimerVisible;
    }

    public boolean isPowerRunningOut() {
        return powerTimer <= Constants.POWER_TIMER_NOTIFICATION;
    }

    public void forcePowerTimeUp() {
        removePowerLabel();
    }

    public void decreaseLives(int quantity) {
        lives -= quantity;
        livesValueLabel.setText(String.format("%02d", lives));
    }

    public int getScore() {
        return score;
    }
}
