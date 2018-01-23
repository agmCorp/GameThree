package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.Constants;
import uy.com.agm.gamethree.tools.AudioManager;

/**
 * Created by AGM on 1/18/2018.
 */

public class Hud extends AbstractScreen {
    private static final String TAG = Hud.class.getName();

    // Hero score/time Tracking Variables
    private int score;
    private int level;
    private int levelTimer;
    private boolean levelTimeUp; // True when the world levelTimer reaches 0
    private int lives;
    private float timeCount;
    private int powerTimer;
    private float timeCountPower;
    private boolean powerTimerVisible;
    private int fps;

    // Scene2D widgets
    private Label scoreLabel;
    private Label scoreValueLabel;

    private Label levelLabel;
    private Label levelValueLabel;

    private Label levelTimerLabel;
    private Label levelTimerValueLabel;

    private Label livesLabel;
    private Label livesValueLabel;

    private Label powerLabel;
    private Label powerValueLabel;

    private Label fpsLabel;
    private Label fpsValueLabel;

    private Table table;

    public Hud(Integer level, Integer levelTimer, Integer lives) {
        super();

        // Define our tracking variables
        score = 0;
        this.level = level;
        this.levelTimer = levelTimer;
        levelTimeUp = false;
        this.lives = lives;
        timeCount = 0;
        powerTimer = 0;
        timeCountPower = 0;
        powerTimerVisible = false;
        fps = 0;
    }

    @Override
    public void buildStage() {
        // Define a table used to organize our hud's labels
        table = new Table();

        // Debug lines
        table.setDebug(Constants.DEBUG_MODE);

        // Top-Align table
        table.top();

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Assets.instance.fonts.defaultSmall;

        // Define our labels based on labelStyle
        scoreLabel = new Label("SCORE", labelStyle);
        scoreValueLabel = new Label(String.format("%06d", score), labelStyle);

        levelLabel = new Label("LEVEL", labelStyle);
        levelValueLabel = new Label(String.format("%02d", level), labelStyle);

        levelTimerLabel = new Label("TIME", labelStyle);
        levelTimerValueLabel = new Label(String.format("%03d", levelTimer), labelStyle);

        livesLabel = new Label("LIVES", labelStyle);
        livesValueLabel = new Label(String.format("%02d", lives), labelStyle);

        powerLabel = new Label("POWERNAME", labelStyle);
        powerValueLabel = new Label(String.format("%03d", powerTimer), labelStyle);

        // Add our labels to our table giving them all equal width with expandX
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(levelTimerLabel).expandX();
        table.add(livesLabel).expandX();

        // Add a second row to our table
        table.row();

        // Values
        table.add(scoreValueLabel).expandX();
        table.add(levelValueLabel).expandX();
        table.add(levelTimerValueLabel).expandX();
        table.add(livesValueLabel).expandX();

        // Add our table to the stage
        addActor(table);

        if (Constants.DEBUG_MODE) {
            // Define a new table used to display our FPS counter
            Table fpsTable = new Table();

            // Debug lines
            fpsTable.setDebug(Constants.DEBUG_MODE);

            // Bottom-Align table
            fpsTable.bottom();

            // Make the table fill the entire stage
            fpsTable.setFillParent(true);

            // Define our labels based on labelStyle
            fpsLabel = new Label("FPS", labelStyle);
            fpsValueLabel = new Label(String.format("%02d", fps), labelStyle);

            // Add our label to our table giving it equal width with expandX
            fpsTable.add(fpsLabel).expandX();

            // Add a second row to our table
            fpsTable.row();

            // Value
            fpsTable.add(fpsValueLabel).expandX();

            // Add our table to the stage
            addActor(fpsTable);
        }
    }

    public void update(float dt) {
        // Update world levelTimer
        timeCount += dt;
        if(timeCount >= 1){
            if (levelTimer > 0) {
                levelTimer--;
                if (levelTimer <= Constants.LEVEL_TIMER_NOTIFICATION) {
                    AudioManager.instance.play(Assets.instance.sounds.getLevelTimer());
                }
            } else {
                levelTimeUp = true;
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
                        AudioManager.instance.play(Assets.instance.sounds.getPowerTimer());
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
            table.row();
            table.add(powerLabel).colspan(4).expandX();
            table.row();
            table.add(powerValueLabel).colspan(4).expandX();
            powerTimerVisible = true;
        }
    }

    private void removePowerLabel() {
        if (powerTimerVisible) {
            table.removeActor(powerLabel);
            table.removeActor(powerValueLabel);
            powerTimerVisible = false;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public boolean isLevelTimeUp() {
        return levelTimeUp;
    }

    public boolean isPowerTimeUp() {
        return !powerTimerVisible;
    }

    public void forcePowerTimeUp() {
        removePowerLabel();
    }

    public void decreaseLives(int quantity) {
        lives -= quantity;
        livesValueLabel.setText(String.format("%02d", lives));
    }
}
