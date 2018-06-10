package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.InfoScreen;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.widget.AnimatedImage;
import uy.com.agm.gamethree.widget.HealthBar;

/**
 * Created by AGM on 1/18/2018.
 */

public class Hud extends AbstractScreen {
    private static final String TAG = Hud.class.getName();

    // Constants
    private static final float UPPER_TABLE_CELL_HEIGHT = 30.0f;
    private static final float HEALTH_BAR_PAD_BOTTOM = 30.0f;
    private static final float SCORE_WIDTH = 110.0f;
    private static final float HIGH_SCORE_WIDTH = 130.0f;
    private static final float TIME_WIDTH = 60.0f;
    private static final float LIVES_WIDTH = 60.0f;
    private static final float SKULLS_WIDTH = 60.0f;
    private static final float SILVER_BULLETS_WIDTH = 60.0f;
    private static final String FORMAT_SCORE = "%06d";
    private static final String FORMAT_HIGH_SCORE = "%d";
    private static final String FORMAT_TIME = "%03d";
    private static final String FORMAT_LIVES = "%02d";
    private static final String FORMAT_SKULLS = "%02d";
    private static final String FORMAT_SILVER_BULLETS = "%02d";
    private static final String FORMAT_FPS = "%02d";
    private static final int POWER_TIMER_NOTIFICATION = 3;
    private static final int LEVEL_TIMER_NOTIFICATION = 10;

    private PlayScreen screen;

    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleSmall;

    private Table upperTable;
    private int score;
    private Label scoreValueLabel;
    private int highScore;
    private Label highScoreValueLabel;
    private int level;
    private int time;
    private float timeCount;
    private Label timeValueLabel;
    private int lives;
    private Label livesValueLabel;
    private int skulls;
    private Label skullsValueLabel;
    private int silverBullets;
    private Label silverBulletValueLablel;

    private Table powersTable;

    private Table abilityPowerTable;
    private Label abilityPowerNameLabel;
    private int abilityPowerTime;
    private float abilityPowerTimeCount;
    private HealthBar abilityBar;

    private Table weaponPowerTable;
    private Label weaponPowerNameLabel;
    private int weaponPowerTime;
    private float weaponPowerTimeCount;
    private HealthBar weaponBar;

    private Table bottomTable;

    private Table fpsTable;
    private int fps;
    private Label fpsValueLabel;

    private Table healthBarTable;
    private Label enemyNameLabel;
    private HealthBar healthBar;

    private boolean timeIsUp; // True when the level time reaches 0

    public Hud(PlayScreen screen, Integer level, Integer score, Integer time, Integer lives, Integer skulls) {
        super();

        // Define tracking variables
        this.screen = screen;
        this.score = score;
        this.highScore = GameSettings.getInstance().getHighScore();
        this.level = level;
        this.time = time;
        timeCount = 0;
        this.lives = lives;
        this.skulls = skulls;
        silverBullets = 0;
        abilityPowerTime = 0;
        abilityPowerTimeCount = 0;
        abilityBar = new HealthBar();
        weaponPowerTime = 0;
        weaponPowerTimeCount = 0;
        weaponBar = new HealthBar();
        fps = 0;
        healthBar = new HealthBar();
        timeIsUp = false;

        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Personal fonts
        labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();
    }

    private void defineUpperTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Define a table used to organize our hud's labels
        upperTable = new Table();

        // Cell height
        upperTable.row().height(UPPER_TABLE_CELL_HEIGHT);

        // Debug lines
        upperTable.setDebug(DebugConstants.DEBUG_MODE);

        // Top-Align table
        upperTable.top();

        // Make the table fill the entire stage
        upperTable.setFillParent(true);

        // Define our labels based on labelStyle
        Label scoreLabel = new Label(i18NGameThreeBundle.format("hud.score"), labelStyleSmall);
        scoreLabel.setAlignment(Align.center);
        Label highScoreLabel = new Label(i18NGameThreeBundle.format("hud.highScore"), labelStyleSmall);
        highScoreLabel.setAlignment(Align.center);

        // Add labels to the table
        upperTable.add(scoreLabel).width(SCORE_WIDTH);
        upperTable.add(highScoreLabel).width(HIGH_SCORE_WIDTH);

        AnimatedImage hourglass = new AnimatedImage();
        hourglass.setAnimation(assetScene2d.getHourglass().getHourglassAnimation());
        upperTable.add(hourglass).width(TIME_WIDTH);
        AnimatedImage heroHead = new AnimatedImage();
        heroHead.setAnimation(assetScene2d.getHeroHead().getHeroHeadAnimation());
        upperTable.add(heroHead).width(LIVES_WIDTH);
        AnimatedImage skullHead = new AnimatedImage();
        skullHead.setAnimation(assetScene2d.getSkullHead().getSkullHeadAnimation());
        upperTable.add(skullHead).width(SKULLS_WIDTH);
        AnimatedImage shuriken = new AnimatedImage();
        shuriken.setAnimation(Assets.getInstance().getSilverBullet().getSilverBulletAnimation());
        upperTable.add(shuriken).width(SILVER_BULLETS_WIDTH);

        // Add a second row to our table
        upperTable.row().height(UPPER_TABLE_CELL_HEIGHT);

        // Define label values based on labelStyle
        scoreValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_SCORE, score), labelStyleSmall);
        scoreValueLabel.setAlignment(Align.center);
        highScoreValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_HIGH_SCORE, highScore), labelStyleSmall);
        highScoreValueLabel.setAlignment(Align.center);
        timeValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_TIME, time), labelStyleSmall);
        timeValueLabel.setAlignment(Align.center);
        livesValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_LIVES, lives), labelStyleSmall);
        livesValueLabel.setAlignment(Align.center);
        skullsValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_SKULLS, skulls), labelStyleSmall);
        skullsValueLabel.setAlignment(Align.center);
        silverBulletValueLablel = new Label(String.format(Locale.getDefault(), FORMAT_SILVER_BULLETS, silverBullets), labelStyleSmall);
        silverBulletValueLablel.setAlignment(Align.center);

        // Add values
        upperTable.add(scoreValueLabel);
        upperTable.add(highScoreValueLabel);
        upperTable.add(timeValueLabel);
        upperTable.add(livesValueLabel);
        upperTable.add(skullsValueLabel);
        upperTable.add(silverBulletValueLablel);

        // Add a third row to our table
        upperTable.row();

        // Add power info
        definePowersTable();
        upperTable.add(powersTable).colspan(upperTable.getColumns());

        // Add table to the stage
        addActor(upperTable);
    }

    private void definePowersTable() {
        // Define a new table used to display ability power info and weapon power info.
        powersTable = new Table();

        // Debug lines
        powersTable.setDebug(DebugConstants.DEBUG_MODE);

        // Add ability power info
        defineAbilityPowerTable();
        powersTable.add(abilityPowerTable).width(V_WIDTH / 2);

        // Add weapon power info
        defineWeaponPowerTable();
        powersTable.add(weaponPowerTable).width(V_WIDTH / 2);
    }

    private void defineAbilityPowerTable() {
        // Define a table used to organize the ability power info
        abilityPowerTable = new Table();

        // Debug lines
        abilityPowerTable.setDebug(DebugConstants.DEBUG_MODE);

        // Define a label based on labelStyle
        abilityPowerNameLabel = new Label("ABILITY_POWER_NAME", labelStyleSmall);

        // Add values
        abilityPowerTable.add(abilityPowerNameLabel);

        // Add a second row to our table
        abilityPowerTable.row();

        // Add health bar
        abilityPowerTable.add(abilityBar);

        // Initially hidden
        abilityPowerTable.setVisible(false);
    }

    private void defineWeaponPowerTable() {
        // Define a table used to organize the weapon power info
        weaponPowerTable = new Table();

        // Debug lines
        weaponPowerTable.setDebug(DebugConstants.DEBUG_MODE);

        // Define a label based on labelStyle
        weaponPowerNameLabel = new Label("WEAPON_POWER_NAME", labelStyleSmall);

        // Add values
        weaponPowerTable.add(weaponPowerNameLabel);

        // Add a second row to our table
        weaponPowerTable.row();

        // Add health bar
        weaponPowerTable.add(weaponBar);

        // Initially hidden
        weaponPowerTable.setVisible(false);
    }

    private void defineBottomTable() {
        // Define a new table used to display a FPS counter and an Enemy's health bar
        bottomTable = new Table();

        // Debug lines
        bottomTable.setDebug(DebugConstants.DEBUG_MODE);

        // Bottom-Align table
        bottomTable.bottom();

        // Make the table fill the entire stage
        bottomTable.setFillParent(true);

        // Add health bar info
        defineHealthBarTable();
        bottomTable.add(healthBarTable);

        // Add a second row to the table
        bottomTable.row();

        // Add FPS info
        defineFpsTable();
        bottomTable.add(fpsTable);

        // Add the table to the stage
        addActor(bottomTable);
    }

    private void defineFpsTable() {
        // Define a new table used to display a FPS counter
        fpsTable = new Table();

        // Debug lines
        fpsTable.setDebug(DebugConstants.DEBUG_MODE);

        // Define a label based on labelStyle
        Label fpsLabel = new Label(i18NGameThreeBundle.format("hud.FPS"), labelStyleSmall);

        // Add a label to the table
        fpsTable.add(fpsLabel);

        // Add a second row to our table
        fpsTable.row();

        // Define a label value based on labelStyle
        fpsValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_FPS, fps), labelStyleSmall);

        // Add value
        fpsTable.add(fpsValueLabel);

        // Visible when SHOW_FPS is true
        fpsTable.setVisible(DebugConstants.SHOW_FPS);
    }

    private void defineHealthBarTable() {
        // Define a new table used to display an enemy's health bar
        healthBarTable = new Table();

        // Debug lines
        healthBarTable.setDebug(DebugConstants.DEBUG_MODE);

        // Define a label based on labelStyle
        enemyNameLabel = new Label("ENEMY_NAME", labelStyleSmall);

        // Add value
        healthBarTable.add(enemyNameLabel);

        // Add a second row to our table
        healthBarTable.row();

        // Add health bar
        healthBarTable.add(healthBar).padBottom(HEALTH_BAR_PAD_BOTTOM);

        // Initially hidden
        healthBarTable.setVisible(false);
    }

    @Override
    public void buildStage() {
        defineUpperTable();
        defineBottomTable();
    }

    public void showAbilityPowerInfo(String abilityPowerName, int maxTime) {
        abilityPowerNameLabel.setText(abilityPowerName);
        abilityPowerTime = maxTime;
        abilityPowerTimeCount = 0;
        abilityBar.setInitialEnergy(POWER_TIMER_NOTIFICATION, abilityPowerTime);
        abilityPowerTable.setVisible(true);
    }

    public void showWeaponPowerInfo(String weaponPowerName, int maxTime) {
        weaponPowerNameLabel.setText(weaponPowerName);
        weaponPowerTime = maxTime;
        weaponPowerTimeCount = 0;
        weaponBar.setInitialEnergy(POWER_TIMER_NOTIFICATION, weaponPowerTime);
        weaponPowerTable.setVisible(true);
    }

    public void hideAbilityPowerInfo() {
        abilityPowerTable.setVisible(false);
    }

    public void hideWeaponPowerInfo() {
        weaponPowerTable.setVisible(false);
    }

    public boolean isAbilityPowerInfoVisible() {
        return abilityPowerTable.isVisible();
    }

    public boolean isWeaponPowerInfoVisible() {
        return weaponPowerTable.isVisible();
    }

    public boolean isFpsInfoVisible() {
        return fpsTable.isVisible();
    }

    public void showHealthBarInfo(String enemyName, int energy) {
        enemyNameLabel.setText(enemyName);
        healthBar.setInitialEnergy(energy / 3, energy);
        healthBarTable.setVisible(true);
    }

    public void hideHealthBarInfo() {
        healthBarTable.setVisible(false);
    }

    public boolean isHealthBarInfoVisible() {
        return healthBarTable.isVisible();
    }

    public void update(float dt) {
        updateWorldTime(dt);
        updatePowersTime(dt);
        updateFPS();
    }

    private void updateWorldTime(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (time > 0) {
                time--;
                if (time <= LEVEL_TIMER_NOTIFICATION) {
                    AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClock());
                    AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBeepB());
                    InfoScreen infoScreen = screen.getInfoScreen();
                    if (infoScreen.isMessageVisible()) {
                        infoScreen.hideInfo();
                    } else {
                        infoScreen.showHurryUpMessage();
                    }
                }
            } else {
                timeIsUp = true;
            }
            timeValueLabel.setText(String.format(Locale.getDefault(), FORMAT_TIME, time));
            timeCount = 0;
        }
    }

    private void updatePowersTime(float dt) {
        updateAbilityPowerTime(dt);
        updateWeaponPowerTime(dt);
    }

    private void updateAbilityPowerTime(float dt) {
        if (isAbilityPowerInfoVisible()) {
            abilityPowerTimeCount += dt;
            if (abilityPowerTimeCount >= 1) {
                if (abilityPowerTime > 0) {
                    abilityPowerTime--;
                    abilityBar.decrease();
                    if (abilityPowerTime < POWER_TIMER_NOTIFICATION) {
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBeepA());
                    }
                } else {
                    hideAbilityPowerInfo();
                }
                abilityPowerTimeCount = 0;
            }
        }
    }

    private void updateWeaponPowerTime(float dt) {
        if (isWeaponPowerInfoVisible()) {
            weaponPowerTimeCount += dt;
            if (weaponPowerTimeCount >= 1) {
                if (weaponPowerTime > 0) {
                    weaponPowerTime--;
                    weaponBar.decrease();
                    if (weaponPowerTime < POWER_TIMER_NOTIFICATION) {
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBeepC());
                    }
                } else {
                    hideWeaponPowerInfo();
                }
                weaponPowerTimeCount = 0;
            }
        }
    }

    private void updateFPS() {
        if (isFpsInfoVisible()) {
            fps = Gdx.graphics.getFramesPerSecond();
            fpsValueLabel.setText(String.format(Locale.getDefault(), FORMAT_FPS, fps));
        }
    }

    public void addScore(int value) {
        score += value;
        scoreValueLabel.setText(String.format(Locale.getDefault(), FORMAT_SCORE, score));
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

    public boolean isAbilityPowerTimeUp() {
        return abilityPowerTime <= 0;
    }

    public boolean isWeaponPowerTimeUp() {
        return weaponPowerTime <= 0;
    }

    public boolean isAbilityPowerRunningOut() {
        return abilityPowerTime <= POWER_TIMER_NOTIFICATION;
    }

    public boolean isWeaponPowerRunningOut() {
        return weaponPowerTime <= POWER_TIMER_NOTIFICATION;
    }

    public void forcePowersTimeUp() {
        hideAbilityPowerInfo();
        hideWeaponPowerInfo();
    }

    public void increaseLives(int quantity) {
        lives += quantity;
        livesValueLabel.setText(String.format(Locale.getDefault(), FORMAT_LIVES, lives));
    }

    public void decreaseLives(int quantity) {
        lives -= quantity;
        livesValueLabel.setText(String.format(Locale.getDefault(), FORMAT_LIVES, lives));
    }

    public void increaseSkulls(int quantity) {
        skulls += quantity;
        skullsValueLabel.setText(String.format(Locale.getDefault(), FORMAT_SKULLS, skulls));
    }

    public void decreaseSkulls(int quantity) {
        skulls -= quantity;
        if (skulls >= 0) {
            skullsValueLabel.setText(String.format(Locale.getDefault(), FORMAT_SKULLS, skulls));
        }
    }

    public int getSkulls() {
        return skulls;
    }

    public void increaseSilverBullets(int quantity) {
        silverBullets += quantity;
        if (silverBullets >= 0) {
            silverBulletValueLablel.setText(String.format(Locale.getDefault(), FORMAT_SILVER_BULLETS, silverBullets));
        }
    }

    public void decreaseSilverBullets(int quantity) {
        silverBullets -= quantity;
        silverBulletValueLablel.setText(String.format(Locale.getDefault(), FORMAT_SILVER_BULLETS, silverBullets));
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
