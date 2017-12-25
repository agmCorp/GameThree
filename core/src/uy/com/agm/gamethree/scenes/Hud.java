package uy.com.agm.gamethree.scenes;

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
import uy.com.agm.gamethree.tools.Constants;

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

    // Scene2D widgets
    private Label scoreLabel;
    private Label scoreValueLabel;

    private Label levelLabel;
    private Label levelValueLabel;

    private Label levelTimerLabel;
    private Label levelTimerValueLabel;

    private Label powerLabel;
    private Label powerValueLabel;


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

        // Setup the HUD viewport using a new camera separate from our gamecam
        // Define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        // Define a table used to organize our hud's labels
        table = new Table();

        // Debug
        table.setDebug(Constants.DEBUG_BOUNDARIES);

        // Top-Align table
        table.top();

        // Make the table fill the entire stage
        table.setFillParent(true);

        // todo arreglar esto
        Label.LabelStyle l = new Label.LabelStyle();
        l.font = Assets.instance.fonts.defaultSmall;


        // Define our labels using the String, and a Label style consisting of a font and color
        scoreLabel = new Label("SCORE", l);
        scoreValueLabel = new Label(String.format("%06d", score), l);

        levelLabel = new Label("LEVEL",l);
        levelValueLabel = new Label(String.format("%02d", level), l);

        levelTimerLabel = new Label("TIME", l);
        levelTimerValueLabel = new Label(String.format("%03d", levelTimer), l);

        powerLabel = new Label("POWERNAME", l);
        powerValueLabel = new Label(String.format("%03d", powerTimer), l);

        // Add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(levelTimerLabel).expandX();

        // Add a second row to our table
        table.row();
        table.add(scoreValueLabel).expandX();
        table.add(levelValueLabel).expandX();
        table.add(levelTimerValueLabel).expandX();

        // Add our table to the stage
        stage.addActor(table);
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

    private void removePowerLabel() {
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
