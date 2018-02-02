package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
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
    private float timeCount;
    private int levelTimer;
    private int lives;

    private float timeCountPower;
    private int powerTimer;

    private boolean levelTimeIsUp; // True when the world levelTimer reaches 0

    private int fps;

    private boolean powerTimerVisible;
    private boolean messageVisible;
    private boolean fpsVisible;
    private boolean heathBarVisible;

    // Scene2D widgets
    private Label.LabelStyle labelStyle;
    private Label scoreValueLabel;
    private Label levelValueLabel;
    private Label levelTimerValueLabel;
    private Label livesValueLabel;

    private Label powerLabel;
    private Label powerValueLabel;
    private Cell cellPowerLabel;
    private Cell cellPowerValueLabel;

    private Label enemyNameLabel;
    private Label marginBottom;
    private HealthBar healthBar;
    private Cell cellEnemyNameLabel;
    private Cell cellMarginBottom;
    private Cell cellHealthBar;

    private Label fpsLabel;
    private Label fpsValueLabel;
    private Cell cellFpsLabel;
    private Cell cellFpsValueLabel;

    private Label messageLabel;
    private Cell cellMessageLabel;

    private Table upperTable;
    private Table centerTable;
    private Table bottomTable;

    public Hud(Integer level, Integer levelTimer, Integer lives) {
        super();

        // Define tracking variables
        score = 0;
        this.level = level;
        timeCount = 0;
        this.levelTimer = levelTimer;
        this.lives = lives;
        timeCountPower = 0;
        powerTimer = 0;
        levelTimeIsUp = false;
        fps = 0;
        labelStyle = new Label.LabelStyle();
        healthBar = new HealthBar();
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
        levelTimerValueLabel = new Label(String.format("%03d", levelTimer), labelStyle);
        livesValueLabel = new Label(String.format("%02d", lives), labelStyle);

        // Add values
        upperTable.add(scoreValueLabel).expandX();
        upperTable.add(levelValueLabel).expandX();
        upperTable.add(levelTimerValueLabel).expandX();
        upperTable.add(livesValueLabel).expandX();

        // Add a third row to our table
        upperTable.row();

        // Define a label based on labelStyle
        powerLabel = new Label("POWERNAME", labelStyle);

        // Add values
        upperTable.add(powerLabel).colspan(4).expandX();

        // Add a fourth row to our table
        upperTable.row();

        // Define a label based on labelStyle
        powerValueLabel = new Label(String.format("%03d", powerTimer), labelStyle);

        // Add values
        upperTable.add(powerValueLabel).colspan(4).expandX();

        // Add table to the stage
        addActor(upperTable);

        // Get cells to hide/show
        powerTimerVisible = true;
        cellPowerLabel = upperTable.getCell(powerLabel);
        cellPowerValueLabel = upperTable.getCell(powerValueLabel);
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
        fpsLabel = new Label("FPS", labelStyle);

        // Add a label to the table giving it equal width with expandX
        bottomTable.add(fpsLabel).expandX();

        // Add a second row to our table
        bottomTable.row();

        // Define a label value based on labelStyle
        fpsValueLabel = new Label(String.format("%02d", fps), labelStyle);

        // Add value
        bottomTable.add(fpsValueLabel).expandX();

        // Add a second row to our table
        bottomTable.row();

        // Define a label based on labelStyle
        enemyNameLabel = new Label("ENEMY_NAME", labelStyle);

        // Add value
        bottomTable.add(enemyNameLabel).expandX();

        // Add a third row to our table
        bottomTable.row();

        // Add heathbar
        bottomTable.add(healthBar).expandX();

        // Add a fourth row to our table
        bottomTable.row();

        // Define a margin-label based on labelStyle (WA: bottomTable.add().padBottom(..) doesn't resize after removing actor)
        marginBottom = new Label("", labelStyle);

        // Add margin
        bottomTable.add(marginBottom).expandX();

        // Add table to the stage
        addActor(bottomTable);

        // Get cells to hide/show
        fpsVisible = true;
        cellFpsLabel = bottomTable.getCell(fpsLabel);
        cellFpsValueLabel = bottomTable.getCell(fpsValueLabel);

        heathBarVisible = true;
        cellEnemyNameLabel = bottomTable.getCell(enemyNameLabel);
        cellHealthBar = bottomTable.getCell(healthBar);
        cellMarginBottom = bottomTable.getCell(marginBottom);
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

        // Add our table to the stage
        addActor(centerTable);

        // Get cells to hide/show
        messageVisible = true;
        cellMessageLabel = centerTable.getCell(messageLabel);
    }

    @Override
    public void buildStage() {
        defineUpperTable();
        defineBottomTable();
        defineCenterTable();
        initTables();
    }

    private void initTables() {
        removePowerHud();
        removeMessage();
        removeHealthBarHud();
        if (!Constants.DEBUG_MODE) {
            removeFpsHud();
        }
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
                    if (messageVisible) {
                        removeMessage();
                    } else {
                        setHurryUpMessage();
                    }
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
                    removePowerHud();
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

    public int getScore() {
        return score;
    }

    public void setPowerHud(String powerName, int maxTime) {
        powerLabel.setText(powerName);
        powerTimer = maxTime;
        powerValueLabel.setText(String.format("%03d", powerTimer));

        if (!powerTimerVisible) {
            // Show values
            cellPowerLabel.setActor(powerLabel);
            cellPowerValueLabel.setActor(powerValueLabel);
            powerTimerVisible = true;
        }
    }

    private void removePowerHud() {
        if (powerTimerVisible) {
            upperTable.removeActor(powerLabel);
            upperTable.removeActor(powerValueLabel);
            powerTimerVisible = false;
        }
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
        if (!messageVisible) {
            // Show values
            cellMessageLabel.setActor(messageLabel);
            messageVisible = true;
        }
    }

    public void removeMessage() {
        if (messageVisible) {
            centerTable.removeActor(messageLabel);
            messageVisible = false;
        }
    }

    public void setFpsHud() {
        if (!fpsVisible) {
            // Show values
            cellFpsLabel.setActor(fpsLabel);
            cellFpsValueLabel.setActor(fpsValueLabel);
            fpsVisible = true;
        }
    }

    public void removeFpsHud() {
        if (fpsVisible) {
            bottomTable.removeActor(fpsLabel);
            bottomTable.removeActor(fpsValueLabel);
            fpsVisible = false;
        }
    }

    public void setHealthBarHud(String enemyName, int energy) {
        enemyNameLabel.setText(enemyName);
        healthBar.setInitialEnergy(energy);

        if (!heathBarVisible) {
            // Show values
            cellEnemyNameLabel.setActor(enemyNameLabel);
            cellHealthBar.setActor(healthBar);
            cellMarginBottom.setActor(marginBottom);
            heathBarVisible = true;
        }
    }

    public void removeHealthBarHud() {
        if (heathBarVisible) {
            bottomTable.removeActor(enemyNameLabel);
            bottomTable.removeActor(healthBar);
            bottomTable.removeActor(marginBottom);
            heathBarVisible = false;
        }
    }

    public void decreaseHealth() {
        healthBar.decrease();
    }

    public void setTimeIsUpMessage() {
        setMessage("TIME IS UP!!");
    }

    public void setHurryUpMessage() {
        setMessage("HURRY UP!!");
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
        removePowerHud();
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
        super.draw();
    }
}
