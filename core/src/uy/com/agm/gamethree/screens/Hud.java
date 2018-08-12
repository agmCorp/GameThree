package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import java.util.Locale;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.assets.scene2d.AssetScene2d;
import uy.com.agm.gamethree.game.DebugConstants;
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
    private static final float POWERS_TABLE_PAD_BOTTOM = 5.0f;
    private static final float STATE_INFO_TABLE_CELL_HEIGHT = 30.0f;
    private static final float HEALTH_BAR_PAD_TOP = 30.0f;
    private static final float SCORE_WIDTH = 96.0f;
    private static final float TIME_WIDTH = 96.0f;
    private static final float LIVES_WIDTH = 96.0f;
    private static final float HEART_WIDTH = 96.0f;
    private static final float SILVER_BULLETS_WIDTH = 96.0f;
    private static final String FORMAT_SCORE = "%06d";
    private static final String FORMAT_TIME = "%03d";
    private static final String FORMAT_LIVES = "%02d";
    private static final String FORMAT_ENDURANCE = "%02d";
    private static final String FORMAT_SILVER_BULLETS = "%02d";
    private static final String FORMAT_FPS = "%02d";
    private static final int POWER_TIMER_NOTIFICATION = 3;
    private static final int LEVEL_TIMER_NOTIFICATION = 10;

    private PlayScreen screen;

    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleSmall;

    private Table bottomTable;

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

    private Table statusBarTable;

    private int score;
    private Label scoreValueLabel;
    private int level;
    private int time;
    private float timeCount;
    private Label timeValueLabel;
    private int lives;
    private Label livesValueLabel;
    private int initialEndurance;
    private Image heart;
    private int endurance;
    private Label enduranceValueLabel;
    private int silverBullets;
    private Label silverBulletValueLabel;

    private Table upperTable;

    private Table fpsTable;
    private int fps;
    private Label fpsValueLabel;

    private Table healthBarTable;
    private Label enemyNameLabel;
    private HealthBar healthBar;

    private boolean timeIsUp; // True when the level time reaches 0

    public Hud(PlayScreen screen, Integer level, Integer score, Integer time, Integer lives, Integer endurance) {
        super();

        // Define tracking variables
        this.screen = screen;
        this.score = score;
        this.level = level;
        this.time = time;
        this.timeCount = 0;
        this.lives = lives;
        this.initialEndurance = endurance;
        this.endurance = endurance;
        this.silverBullets = 0;
        this.abilityPowerTime = 0;
        this.abilityPowerTimeCount = 0;
        this.abilityBar = new HealthBar();
        this.weaponPowerTime = 0;
        this.weaponPowerTimeCount = 0;
        this.weaponBar = new HealthBar();
        this.fps = 0;
        this.healthBar = new HealthBar();
        this.timeIsUp = false;

        // I18n
        this.i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Personal fonts
        this.labelStyleSmall = new Label.LabelStyle();
        this.labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();
    }

    private void defineBottomTable() {
        // Define a table used to organize the HUD
        bottomTable = new Table();

        // Debug lines
        bottomTable.setDebug(DebugConstants.DEBUG_LINES);

        // Bottom-Align table
        bottomTable.bottom();

        // Make the table fill the entire stage
        bottomTable.setFillParent(true);

        // Add power info
        definePowersTable();
        bottomTable.add(powersTable).padBottom(POWERS_TABLE_PAD_BOTTOM);

        // Add a second row to the table
        bottomTable.row();

        // Add state info
        defineStatusBarTable();
        bottomTable.add(statusBarTable).colspan(bottomTable.getColumns());

        // Add bottomTable to the stage
        addActor(bottomTable);
    }

    private void defineStatusBarTable() {
        // UI assets
        AssetScene2d assetScene2d = Assets.getInstance().getScene2d();

        // Game assets
        Assets assetGame = Assets.getInstance();

        // Define a new table used to display score, high score, time, lives, heart and shurikens.
        statusBarTable = new Table();

        // Cell height
        statusBarTable.row().height(STATE_INFO_TABLE_CELL_HEIGHT);

        // Debug lines
        statusBarTable.setDebug(DebugConstants.DEBUG_LINES);

        // Define images
        AnimatedImage coin = new AnimatedImage(assetGame.getColOne().getCoinAnimation());

        Image trophy = new AnimatedImage();
        trophy.setDrawable(new TextureRegionDrawable(assetScene2d.getGoldTrophy()));
        trophy.setScaling(Scaling.fit);

        AnimatedImage hourglass = new AnimatedImage(assetScene2d.getHourglass().getHourglassAnimation());

        AnimatedImage heroHead = new AnimatedImage(assetScene2d.getHeroHead().getHeroHeadAnimation());

        heart = new Image();
        setHeartImage(100);

        AnimatedImage shuriken = new AnimatedImage(assetGame.getSilverBullet().getSilverBulletAnimation());

        // Add images to the table
        statusBarTable.add(coin).width(SCORE_WIDTH);
        statusBarTable.add(hourglass).width(TIME_WIDTH);
        statusBarTable.add(heroHead).width(LIVES_WIDTH);
        statusBarTable.add(heart).width(HEART_WIDTH);
        statusBarTable.add(shuriken).width(SILVER_BULLETS_WIDTH);

        // Add a second row
        statusBarTable.row();

        // Define label values based on labelStyle
        scoreValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_SCORE, score), labelStyleSmall);
        scoreValueLabel.setAlignment(Align.center);
        timeValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_TIME, time), labelStyleSmall);
        timeValueLabel.setAlignment(Align.center);
        livesValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_LIVES, lives), labelStyleSmall);
        livesValueLabel.setAlignment(Align.center);
        enduranceValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_ENDURANCE, this.endurance), labelStyleSmall);
        enduranceValueLabel.setAlignment(Align.center);
        silverBulletValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_SILVER_BULLETS, silverBullets), labelStyleSmall);
        silverBulletValueLabel.setAlignment(Align.center);

        // Add values
        statusBarTable.add(scoreValueLabel);
        statusBarTable.add(timeValueLabel);
        statusBarTable.add(livesValueLabel);
        statusBarTable.add(enduranceValueLabel);
        statusBarTable.add(silverBulletValueLabel);
    }

    private void setHeartImage(int index) {
        TextureRegion endurance = Assets.getInstance().getScene2d().getEndurance().getEnduranceStand(index);
        heart.setDrawable(new TextureRegionDrawable(endurance));
        heart.setScaling(Scaling.fit);
    }

    private void definePowersTable() {
        // Define a new table used to display ability power info and weapon power info.
        powersTable = new Table();

        // Debug lines
        powersTable.setDebug(DebugConstants.DEBUG_LINES);

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
        abilityPowerTable.setDebug(DebugConstants.DEBUG_LINES);

        // Define a label based on labelStyle
        abilityPowerNameLabel = new Label("ABILITY_POWER_NAME", labelStyleSmall);

        // Add values
        abilityPowerTable.add(abilityPowerNameLabel);

        // Add a second row to the table
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
        weaponPowerTable.setDebug(DebugConstants.DEBUG_LINES);

        // Define a label based on labelStyle
        weaponPowerNameLabel = new Label("WEAPON_POWER_NAME", labelStyleSmall);

        // Add values
        weaponPowerTable.add(weaponPowerNameLabel);

        // Add a second row to the table
        weaponPowerTable.row();

        // Add health bar
        weaponPowerTable.add(weaponBar);

        // Initially hidden
        weaponPowerTable.setVisible(false);
    }

    private void defineUpperTable() {
        // Define a new table used to display a FPS counter and an Enemy's health bar
        upperTable = new Table();

        // Debug lines
        upperTable.setDebug(DebugConstants.DEBUG_LINES);

        // Top-Align table
        upperTable.top();

        // Make the table fill the entire stage
        upperTable.setFillParent(true);

        // Add FPS info
        defineFpsTable();
        upperTable.add(fpsTable);

        // Add a second row to the table
        upperTable.row();

        // Add health bar info
        defineHealthBarTable();
        upperTable.add(healthBarTable);

        // Add the table to the stage
        addActor(upperTable);
    }

    private void defineFpsTable() {
        // Define a new table used to display a FPS counter
        fpsTable = new Table();

        // Debug lines
        fpsTable.setDebug(DebugConstants.DEBUG_LINES);

        // Define a label based on labelStyle
        Label fpsLabel = new Label(i18NGameThreeBundle.format("hud.FPS"), labelStyleSmall);

        // Add a label to the table
        fpsTable.add(fpsLabel);

        // Add a second row to the table
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
        healthBarTable.setDebug(DebugConstants.DEBUG_LINES);

        // Define a label based on labelStyle
        enemyNameLabel = new Label("ENEMY_NAME", labelStyleSmall);

        // Add value
        healthBarTable.add(enemyNameLabel);

        // Add a second row to the table
        healthBarTable.row();

        // Add health bar
        healthBarTable.add(healthBar).padBottom(HEALTH_BAR_PAD_TOP);

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

    public void decreaseEndurance(int quantity) {
        endurance -= quantity;
        if (endurance >= 0) {
            setHeartImage(endurance);
            enduranceValueLabel.setText(String.format(Locale.getDefault(), FORMAT_ENDURANCE, endurance));
            if (endurance == 1) {
                screen.getInfoScreen().showModalLoseLifeWarning();
            }
        }
    }

    public void refillEndurance() {
        endurance = initialEndurance;
        setHeartImage(endurance);
        enduranceValueLabel.setText(String.format(Locale.getDefault(), FORMAT_ENDURANCE, endurance));
    }

    public void emptyEndurance() {
        endurance = 0;
        setHeartImage(endurance);
        enduranceValueLabel.setText(String.format(Locale.getDefault(), FORMAT_ENDURANCE, endurance));
    }

    public void increaseSilverBullets(int quantity) {
        silverBullets += quantity;
        if (silverBullets >= 0) {
            silverBulletValueLabel.setText(String.format(Locale.getDefault(), FORMAT_SILVER_BULLETS, silverBullets));
        }
    }

    public void decreaseSilverBullets(int quantity) {
        silverBullets -= quantity;
        silverBulletValueLabel.setText(String.format(Locale.getDefault(), FORMAT_SILVER_BULLETS, silverBullets));
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
