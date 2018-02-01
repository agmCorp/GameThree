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
    private boolean levelTimeIsUp; // True when the world levelTimer reaches 0
    private int lives;
    private float timeCount;
    private int powerTimer;
    private float timeCountPower;
    private boolean powerTimerVisible;
    private int fps;

    // Scene2D widgets
    private Label scoreValueLabel;
    private Label levelValueLabel;
    private Label levelTimerValueLabel;
    private Label livesValueLabel;
    private Label powerLabel;
    private Label powerValueLabel;
    private Label fpsValueLabel;
    private Label timeIsUpLabel;
    private Table hudTable;
    private Table timeIsUpTable;

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
    }

    @Override
    public void buildStage() {
        // Define a table used to organize our hud's labels
        hudTable = new Table();

        // Debug lines
        hudTable.setDebug(Constants.DEBUG_MODE);

        // Top-Align table
        hudTable.top();

        // Make the table fill the entire stage
        hudTable.setFillParent(true);

        // Personal fonts
        Label.LabelStyle labelStyle = new Label.LabelStyle();
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
        hudTable.add(scoreLabel).expandX();
        hudTable.add(levelLabel).expandX();
        hudTable.add(levelTimerLabel).expandX();
        hudTable.add(livesLabel).expandX();

        // Add a second row to our table
        hudTable.row();

        // Values
        hudTable.add(scoreValueLabel).expandX();
        hudTable.add(levelValueLabel).expandX();
        hudTable.add(levelTimerValueLabel).expandX();
        hudTable.add(livesValueLabel).expandX();

        // Add table to the stage
        addActor(hudTable);

        if (Constants.DEBUG_MODE) {
            // Define a new table used to display our FPS counter
            Table fpsTable = new Table();

            // Debug lines
            fpsTable.setDebug(Constants.DEBUG_MODE);

            // Bottom-Align table
            fpsTable.bottom();

            // Make the table fill the entire stage
            fpsTable.setFillParent(true);

            // Define a label based on labelStyle
            Label fpsLabel = new Label("FPS", labelStyle);
            fpsValueLabel = new Label(String.format("%02d", fps), labelStyle);

            // Add previous label to the table giving it equal width with expandX
            fpsTable.add(fpsLabel).expandX();

            // Add a second row to our table
            fpsTable.row();

            // Value
            fpsTable.add(fpsValueLabel).expandX();

            // Add table to the stage
            addActor(fpsTable);
        }

        // Define a new table used to display "Iime is up" message
        timeIsUpTable = new Table();

        // Debug lines
        timeIsUpTable.setDebug(Constants.DEBUG_MODE);

        // Center-Align table
        timeIsUpTable.center();

        // Make the table fill the entire stage
        timeIsUpTable.setFillParent(true);

        // Define a label based on labelStyle
        Label.LabelStyle labelStyleMessage = new Label.LabelStyle();
        labelStyleMessage.font = Assets.getInstance().getFonts().getDefaultBig();
        timeIsUpLabel = new Label("TIME IS UP!!", labelStyleMessage);

        // Add our table to the stage
        addActor(timeIsUpTable);
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
            hudTable.row();
            hudTable.add(powerLabel).colspan(4).expandX();
            hudTable.row();
            hudTable.add(powerValueLabel).colspan(4).expandX();
            powerTimerVisible = true;
        }
    }

    public void setTimeIsUpLabel() {
        timeIsUpTable.add(timeIsUpLabel).expandX();
    }

    private void removePowerLabel() {
        if (powerTimerVisible) {
            hudTable.removeActor(powerLabel);
            hudTable.removeActor(powerValueLabel);
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


//  private HealthBar helthBar; // todo

/*


        table.row();
                helthBar = new HealthBar(27);
                table.add(helthBar).padTop(Constants.PAD_TOP).center().expandX();

                table.row();
                Label decLabel = new Label("disminuir", labelStyleNormal);
                table.add(decLabel).padTop(Constants.PAD_TOP).center();

                decLabel.addListener(
                new InputListener() {
@Override
public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        helthBar.decrease();
        return true;
        }
        });
*/
