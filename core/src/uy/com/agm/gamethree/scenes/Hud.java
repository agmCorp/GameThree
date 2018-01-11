package uy.com.agm.gamethree.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.game.Constants;

/**
 * Created by AGM on 12/3/2017.
 */

public class Hud implements Disposable {
    private static final String TAG = Hud.class.getName();

    // Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    public Viewport viewport;

    // Hero score/time Tracking Variables
    private Integer score;
    private Integer level;
    private Integer levelTimer;
    private boolean levelTimeUp; // True when the world levelTimer reaches 0
    private float timeCount;
    private Integer powerTimer;
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

    private Label powerLabel;
    private Label powerValueLabel;

    private Label fpsLabel;
    private Label fpsValueLabel;

    private Table table;

    public Hud(SpriteBatch sb) {
        // Define our tracking variables
        score = 0;
        level = 1;
        levelTimer = Constants.TIMER_LEVEL_ONE;
        levelTimeUp = false;
        timeCount = 0;
        powerTimer = 0;
        timeCountPower = 0;
        powerTimerVisible = false;
        fps = 0;

        // Setup the HUD viewport using a new camera separate from our gamecam
        // Define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

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

        powerLabel = new Label("POWERNAME", labelStyle);
        powerValueLabel = new Label(String.format("%03d", powerTimer), labelStyle);

        // Add our labels to our table giving them all equal width with expandX
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(levelTimerLabel).expandX();

        // Add a second row to our table
        table.row();

        // Values
        table.add(scoreValueLabel).expandX();
        table.add(levelValueLabel).expandX();
        table.add(levelTimerValueLabel).expandX();

        // Add our table to the stage
        stage.addActor(table);

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
            stage.addActor(fpsTable);
        }
    }

    public void update(float dt){
        // Update world levelTimer
        timeCount += dt;
        if(timeCount >= 1){
            if (levelTimer > 0) {
                levelTimer--;
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
                    if (powerTimer <= Constants.TIMER_NOTIFICATION) {
                        AudioManager.instance.play(Assets.instance.sounds.powerTimer, 1);
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
            table.add().expandX();
            table.add(powerLabel).expandX();
            table.add().expandX();
            table.row();
            table.add().expandX();
            table.add(powerValueLabel).expandX();
            table.add().expandX();
            powerTimerVisible = true;
        }
    }

    public void removePowerLabel() {
        if (powerTimerVisible) {
            table.removeActor(powerLabel);
            table.removeActor(powerValueLabel);
            powerTimerVisible = false;
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isLevelTimeUp() {
        return levelTimeUp;
    }

    public boolean isPowerTimeUp() {
        return !powerTimerVisible;
    }
}
