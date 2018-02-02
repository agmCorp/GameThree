package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
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
    private Label marginBottom;
    private Label fpsValueLabel;
    private Label timeIsUpLabel;
    private Table upperTable;
    private Table timeIsUpTable;
    private Table bottomTable;
    private HealthBar healthBar;

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
        powerLabel = new Label("POWERNAME", labelStyle);
        powerValueLabel = new Label(String.format("%03d", powerTimer), labelStyle);

        // Add values
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

        // Define labels based on labelStyle
        enemyNameLabel = new Label("ENEMY_NAME", labelStyle);
        marginBottom = new Label("", labelStyle);

        // Debug
        if (Constants.DEBUG_MODE) {
            // Add a label to the table giving it equal width with expandX
            bottomTable.add(new Label("FPS", labelStyle)).expandX();

            // Add a second row to our table
            bottomTable.row();

            // Define label value based on labelStyle
            fpsValueLabel = new Label(String.format("%02d", fps), labelStyle);

            // Add value
            bottomTable.add(fpsValueLabel).expandX();

            // Add new row to insert more cells
            bottomTable.row();
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

        // Personal fonts
        labelStyle.font = Assets.getInstance().getFonts().getDefaultBig();

        timeIsUpLabel = new Label("TIME IS UP!!", labelStyle);

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
        healthBar.setInitialEnergy(energy);

        bottomTable.add(enemyNameLabel).expandX();
        bottomTable.row();
        bottomTable.add(healthBar).expandX();
        bottomTable.row();
        bottomTable.add(marginBottom).expandX(); // WA: bottomTable.add(healthBar).padBottom(..) doesn't disappear after removing healthBar
    }

    public void removeHealthBar() {
        bottomTable.removeActor(enemyNameLabel);
        bottomTable.removeActor(healthBar);
        bottomTable.removeActor(marginBottom);
    }

    public void decreaseHealth() {
        healthBar.decrease();
    }

    public void setTimeIsUpLabel() {
        timeIsUpTable.add(timeIsUpLabel).expandX();
    }

    private void removePowerLabel() {
        /*
        * todo
        * hud:
Para setear las alturas hago upperTable.setDefaults().width(30);
Para ocultar y mostrar:

para remover:
ell c = upperTable.getCell(powerLabel);
upperTable.removeActor(powerLabel);

y luego con c.setActor(powerLabel) lo vuelvo a meter.
        * */
        if (powerTimerVisible) {
            upperTable.removeActor(powerLabel);
            upperTable.removeActor(powerValueLabel);
            powerTimerVisible = false;
        }
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

    @Override
    public void draw () {
        act();
        super.draw();
    }
}
