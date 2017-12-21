package uy.com.agm.gamethree.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
    private Integer worldTimer;
    private boolean timeUp; // True when the world timer reaches 0
    private float timeCount;
    private float timeCountPower;
    private Integer score;
    private Integer powerTimer;
    private boolean powerTimerVisible;

    // Scene2D widgets
    private Label countdownLabel;
    private Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label gameThreeLabel;
    private Label powerLabel;
    private Label countdownPowerLabel;
    private Table table;

    public Hud(SpriteBatch sb) {
        // Define our tracking variables
        worldTimer = Constants.TIMER_LEVEL_ONE;
        timeCount = 0;
        timeCountPower = 0;
        score = 0;
        powerTimer = 0;
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

        // Define our labels using the String, and a Label style consisting of a font and color
        gameThreeLabel = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("LEVEL", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        powerLabel = new Label("POWER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        countdownPowerLabel = new Label(String.format("%03d", powerTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // Add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(gameThreeLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        // Add a second row to our table
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        // Add our table to the stage
        stage.addActor(table);
    }

    public void update(float dt){
        // Update world timer
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }

        // Update power timer
        if (powerTimerVisible) {
            timeCountPower += dt;
            if (timeCountPower >= 1) {
                if (powerTimer > 0) {
                    powerTimer--;
                    countdownPowerLabel.setText(String.format("%03d", powerTimer));
                } else {
                    removePowerLabel();
                }
                timeCountPower = 0;
            }
        }
    }

    public void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    public void setPowerLabel(int maxTime) {
        powerTimer = maxTime;
        countdownPowerLabel.setText(String.format("%03d", powerTimer)); // actualizo el texto de la variable

        if (!powerTimerVisible) {
            // Add a third row to our table
            table.row();
            table.add().expandX();//
            table.add(powerLabel).expandX(); // dice power nomas
            table.add().expandX();//
            table.row();
            table.add().expandX();//
            table.add(countdownPowerLabel).expandX(); // tiene el valor de la variable powerTimer
            table.add().expandX();
            powerTimerVisible = true;
        }
    }

    private void removePowerLabel() {
        if (powerTimerVisible) {
            table.removeActor(powerLabel);
            table.removeActor(countdownPowerLabel);
            powerTimerVisible = false;
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeUp() {
        return timeUp;
    }
}
