package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;

import java.util.Locale;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.GameSettings;
import uy.com.agm.gamethree.screens.util.ScreenEnum;
import uy.com.agm.gamethree.screens.util.ScreenManager;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.DynamicHelpDef;
import uy.com.agm.gamethree.tools.LevelFactory;
import uy.com.agm.gamethree.widget.HealthBar;

/**
 * Created by AGM on 1/18/2018.
 */

public class Hud extends AbstractScreen {
    private static final String TAG = Hud.class.getName();

    // Constants
    private static final float BUTTONS_PAD = 20.0f;
    private static final float UPPER_TABLE_CELL_HEIGHT = 30.0f;
    private static final float BUTTON_WIDTH = 100.0f;
    private static final float HEALTHBAR_PAD_BOTTOM = 30.0f;
    private static final String FORMAT_SCORE = "%06d";
    private static final String FORMAT_TIME = "%03d";
    private static final String FORMAT_LIVES = "%02d";
    private static final String FORMAT_SILVER_BULLETS = "%02d";
    private static final String FORMAT_POWER_TIME = "%03d";
    private static final String FORMAT_FPS = "%02d";
    private static final int POWER_TIMER_NOTIFICATION = 3;
    private static final int LEVEL_TIMER_NOTIFICATION = 10;

    private PlayScreen screen;

    // Track help screens depending on the object's class name
    private ObjectMap<String, DynamicHelpDef> dynamicHelp;

    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleBig;
    private Label.LabelStyle labelStyleSmall;

    private Table upperTable;
    private int score;
    private Label scoreValueLabel;
    private int level;
    private int time;
    private float timeCount;
    private Label timeValueLabel;
    private int lives;
    private Label livesValueLabel;
    private int silverBullets;
    private Label silverBulletValueLablel;

    private Table abilityPowerTable;
    private Label abilityPowerNameLabel;
    private int abilityPowerTime;
    private float abilityPowerTimeCount;
    private Label abilityPowerTimeValueLabel;

    private Table weaponPowerTable;
    private Label weaponPowerNameLabel;
    private int weaponPowerTime;
    private float weaponPowerTimeCount;
    private Label weaponPowerTimeValueLabel;

    private Table centerTable;
    private Label messageLabel;
    private Image image;
    private float overlayTime;
    private float overlaySeconds;
    private boolean overlayTemporaryScreen;
    private boolean overlayTemporaryMessage;

    private Table bottomTable;

    private Table fpsTable;
    private int fps;
    private Label fpsValueLabel;

    private Table healthBarTable;
    private Label enemyNameLabel;
    private HealthBar healthBar;

    private Table buttonsTable;
    private Label pauseLabel;
    private Label resumeLabel;
    private Label gotItLabel;
    private Label quitLabel;

    private boolean timeIsUp; // True when the level time reaches 0

    private Stack stack;

    public Hud(PlayScreen screen, Integer level, Integer score, Integer time, Integer lives) {
        super();

        // Define tracking variables
        this.screen = screen;
        this.score = score;
        this.level = level;
        this.time = time;
        timeCount = 0;
        this.lives = lives;
        silverBullets = 0;
        abilityPowerTime = 0;
        abilityPowerTimeCount = 0;
        fps = 0;
        healthBar = new HealthBar();
        timeIsUp = false;
        overlayTime = 0;
        overlaySeconds = 0;
        overlayTemporaryScreen = false;
        overlayTemporaryMessage = false;

        // I18n
        i18NGameThreeBundle = Assets.getInstance().getI18NGameThree().getI18NGameThreeBundle();

        // Track help screens
        dynamicHelp = LevelFactory.getDynamicHelp(level);

        // Personal fonts
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();
    }

    private void defineUpperTable() {
        // Define a table used to organize our hud's labels
        upperTable = new Table();

        // Defaults for all cells
        upperTable.defaults().width(V_WIDTH / 4);

        // Cell height
        upperTable.row().height(UPPER_TABLE_CELL_HEIGHT);

        // Debug lines
        upperTable.setDebug(PlayScreen.DEBUG_MODE);

        // Top-Align table
        upperTable.top();

        // Make the table fill the entire stage
        upperTable.setFillParent(true);

        // Define our labels based on labelStyle
        Label scoreLabel = new Label(i18NGameThreeBundle.format("hud.score"), labelStyleSmall);
        scoreLabel.setAlignment(Align.center);
        Label timeLabel = new Label(i18NGameThreeBundle.format("hud.time"), labelStyleSmall);
        timeLabel.setAlignment(Align.center);

        // Add labels to the table
        upperTable.add(scoreLabel);
        upperTable.add(timeLabel);
        upperTable.add(new Image(new TextureRegionDrawable(Assets.getInstance().getHero().getHeroHead()), Scaling.fit));
        upperTable.add(new Image(new TextureRegionDrawable(Assets.getInstance().getSilverBullet().getSilverBulletStand()), Scaling.fit));

        // Add a second row to our table
        upperTable.row().height(UPPER_TABLE_CELL_HEIGHT);

        // Define label values based on labelStyle
        scoreValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_SCORE, score), labelStyleSmall);
        scoreValueLabel.setAlignment(Align.center);
        timeValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_TIME, time), labelStyleSmall);
        timeValueLabel.setAlignment(Align.center);
        livesValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_LIVES, lives), labelStyleSmall);
        livesValueLabel.setAlignment(Align.center);
        silverBulletValueLablel = new Label(String.format(Locale.getDefault(), FORMAT_SILVER_BULLETS, silverBullets), labelStyleSmall);
        silverBulletValueLablel.setAlignment(Align.center);

        // Add values
        upperTable.add(scoreValueLabel);
        upperTable.add(timeValueLabel);
        upperTable.add(livesValueLabel);
        upperTable.add(silverBulletValueLablel);

        // Add a third row to our table
        upperTable.row();

        // Add ability power info
        defineAbilityPowerTable();
        upperTable.add(abilityPowerTable).colspan(upperTable.getColumns() / 2);

        // Add weapon power info
        defineWeaponPowerTable();
        upperTable.add(weaponPowerTable).colspan(upperTable.getColumns() / 2);

        // Add table to the stage
        addActor(upperTable);
    }

    private void defineAbilityPowerTable() {
        // Define a table used to organize the ability power info
        abilityPowerTable = new Table();

        // Debug lines
        abilityPowerTable.setDebug(PlayScreen.DEBUG_MODE);

        // Define a label based on labelStyle
        abilityPowerNameLabel = new Label("ABILITY_POWER_NAME", labelStyleSmall);

        // Add values
        abilityPowerTable.add(abilityPowerNameLabel);

        // Add a second row to our table
        abilityPowerTable.row();

        // Define a label based on labelStyle
        abilityPowerTimeValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_POWER_TIME, abilityPowerTime), labelStyleSmall);

        // Add values
        abilityPowerTable.add(abilityPowerTimeValueLabel).expandX();

        // Initially hidden
        abilityPowerTable.setVisible(false);
    }

    private void defineWeaponPowerTable() {
        // Define a table used to organize the weapon power info
        weaponPowerTable = new Table();

        // Debug lines
        weaponPowerTable.setDebug(PlayScreen.DEBUG_MODE);

        // Define a label based on labelStyle
        weaponPowerNameLabel = new Label("WEAPON_POWER_NAME", labelStyleSmall);

        // Add values
        weaponPowerTable.add(weaponPowerNameLabel);

        // Add a second row to our table
        weaponPowerTable.row();

        // Define a label based on labelStyle
        weaponPowerTimeValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_POWER_TIME, weaponPowerTime), labelStyleSmall);

        // Add values
        weaponPowerTable.add(weaponPowerTimeValueLabel).expandX();

        // Initially hidden
        weaponPowerTable.setVisible(false);
    }

    private void defineCenterTable() {
        // Define a new table used to display a message
        centerTable = new Table();

        // Debug lines
        centerTable.setDebug(PlayScreen.DEBUG_MODE);

        // Center-Align table
        centerTable.center();

        // Make the table fill the entire stage
        centerTable.setFillParent(true);

        // Define a label based on labelStyle
        messageLabel = new Label("MESSAGE", labelStyleBig);
        messageLabel.setAlignment(Align.center);
        image = new Image();
        image.setAlign(Align.center);

        // Add values
        stack = new Stack();
        stack.add(messageLabel);
        stack.add(image);
        centerTable.add(stack);

        // Add our table to the stage
        addActor(centerTable);

        // Initially hidden
        messageLabel.setVisible(false);
        image.setVisible(false);
        centerTable.setVisible(false);
    }

    private void defineBottomTable() {
        // Define a new table used to display a FPS counter and an Enemy's health bar
        bottomTable = new Table();

        // Debug lines
        bottomTable.setDebug(PlayScreen.DEBUG_MODE);

        // Bottom-Align table
        bottomTable.bottom();

        // Make the table fill the entire stage
        bottomTable.setFillParent(true);

        // Add health bar info
        defineHealthBarTable();
        bottomTable.add(healthBarTable);

        // FPS info
        defineFpsTable();

        if (PlayScreen.SHOW_FPS) {
            // Add a second row to the table
            bottomTable.row();
            // Add FPS info
            bottomTable.add(fpsTable);
        }

        // Add the table to the stage
        addActor(bottomTable);
    }

    private void defineFpsTable() {
        // Define a new table used to display a FPS counter
        fpsTable = new Table();

        // Debug lines
        fpsTable.setDebug(PlayScreen.DEBUG_MODE);

        // Define a label based on labelStyle
        Label fpsLabel = new Label(i18NGameThreeBundle.format("hud.FPS"), labelStyleSmall);

        // Add a label to the table
        fpsTable.add(fpsLabel);

        // Add a second row to our table
        fpsTable.row();

        // Define a label value based on labelStyle
        fpsValueLabel = new Label(String.format(Locale.getDefault(), FORMAT_POWER_TIME, fps), labelStyleSmall);

        // Add value
        fpsTable.add(fpsValueLabel);

        // Visible when SHOW_FPS is true
        fpsTable.setVisible(PlayScreen.SHOW_FPS);
    }

    private void defineHealthBarTable() {
        // Define a new table used to display an enemy's health bar
        healthBarTable = new Table();

        // Debug lines
        healthBarTable.setDebug(PlayScreen.DEBUG_MODE);

        // Define a label based on labelStyle
        enemyNameLabel = new Label("ENEMY_NAME", labelStyleSmall);

        // Add value
        healthBarTable.add(enemyNameLabel);

        // Add a second row to our table
        healthBarTable.row();

        // Add health bar
        healthBarTable.add(healthBar).padBottom(HEALTHBAR_PAD_BOTTOM);

        // Initially hidden
        healthBarTable.setVisible(false);
    }

    private void defineButtonsTable() {
        // Define a new table used to display pause, resume and quit buttons
        buttonsTable = new Table();

        // Debug lines
        buttonsTable.setDebug(PlayScreen.DEBUG_MODE);

        // Bottom-Align table
        buttonsTable.bottom().padLeft(BUTTONS_PAD).padRight(BUTTONS_PAD);

        // Make the container fill the entire stage
        buttonsTable.setFillParent(true);

        // Define labels based on labelStyle
        pauseLabel = new Label(i18NGameThreeBundle.format("hud.pause"), labelStyleSmall);
        quitLabel = new Label(i18NGameThreeBundle.format("hud.quit"), labelStyleSmall);
        quitLabel.setAlignment(Align.right);
        resumeLabel = new Label(i18NGameThreeBundle.format("hud.resume"), labelStyleSmall);
        gotItLabel = new Label(i18NGameThreeBundle.format("hud.gotIt"), labelStyleSmall);

        // Add values
        stack = new Stack();
        stack.add(pauseLabel);
        stack.add(resumeLabel);
        stack.add(gotItLabel);
        buttonsTable.add(stack).width(BUTTON_WIDTH).left().expandX(); // Pause, Resume and Got it texts overlapped
        buttonsTable.add(quitLabel).width(BUTTON_WIDTH).right().expandX();

        // Events
        pauseLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        setGameStatePaused();
                        return true;
                    }
                });

        resumeLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        setGameStateRunning();
                        return true;
                    }
                });

        gotItLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        setGameStateRunning();
                        return true;
                    }
                });

        quitLabel.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        // Audio FX
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        quit();
                        return true;
                    }
                });

        // Add the table to the stage
        addActor(buttonsTable);

        // Initially hidden
        resumeLabel.setVisible(false);
        gotItLabel.setVisible(false);
        quitLabel.setVisible(false);
    }

    public void setGameStatePaused() {
        pauseLabel.setVisible(false);
        resumeLabel.setVisible(true);
        gotItLabel.setVisible(false);
        quitLabel.setVisible(true);
        showMessage(i18NGameThreeBundle.format("hud.pauseMessage"));
        screen.setPlayScreenStatePaused(true);
    }

    public void setGameStateRunning() {
        pauseLabel.setVisible(true);
        resumeLabel.setVisible(false);
        gotItLabel.setVisible(false);
        quitLabel.setVisible(false);
        hideMessage();
        screen.setPlayScreenStateRunning();
    }

    private void quit() {
        if (getMessage().equals(i18NGameThreeBundle.format("hud.confirm"))) {
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
        } else {
            showMessage(i18NGameThreeBundle.format("hud.confirm"));
        }
    }

    public void showInitialHelp() {
        if (level == 1) {
            if (GameSettings.getInstance().isManualShooting()) {
                showImage(Assets.getInstance().getScene2d().getHelpInitialManual(), DynamicHelpDef.DEFAULT_HELP_SECONDS);
            } else {
                showImage(Assets.getInstance().getScene2d().getHelpInitialAutomatic(), DynamicHelpDef.DEFAULT_HELP_SECONDS);
            }
        }
    }

    @Override
    public void buildStage() {
        defineUpperTable();
        defineCenterTable();
        defineBottomTable();
        defineButtonsTable();
    }

    public void showAbilityPowerInfo(String abilityPowerName, int maxTime) {
        abilityPowerNameLabel.setText(abilityPowerName);
        abilityPowerTime = maxTime;
        abilityPowerTimeValueLabel.setText(String.format(Locale.getDefault(), FORMAT_POWER_TIME, abilityPowerTime));
        abilityPowerTable.setVisible(true);
    }

    public void showWeaponPowerInfo(String weaponPowerName, int maxTime) {
        weaponPowerNameLabel.setText(weaponPowerName);
        weaponPowerTime = maxTime;
        weaponPowerTimeValueLabel.setText(String.format(Locale.getDefault(), FORMAT_POWER_TIME, weaponPowerTime));
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

    public void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
        image.setVisible(false);
        centerTable.setVisible(true);
    }

    public void showMessage(String message, float seconds) {
        overlayTime = 0;
        overlaySeconds = seconds;
        overlayTemporaryMessage = true;
        showMessage(message);
    }

    public String getMessage() {
        String message = "";
        if (messageLabel.isVisible()) {
            message = messageLabel.getText().toString();
        }
        return message;
    }

    public void showHurryUpMessage() {
        showMessage(i18NGameThreeBundle.format("hud.hurryUp"));
    }

    public void showTimeIsUpMessage() {
        showMessage(i18NGameThreeBundle.format("hud.timeIsUp"));
    }

    public void showFightMessage() {
        showMessage(i18NGameThreeBundle.format("hud.fight"));
    }

    public void hideMessage() {
        messageLabel.setVisible(false);
        centerTable.setVisible(false);
    }

    public boolean isMessageVisible() {
        return messageLabel.isVisible();
    }

    public void showImage(TextureRegion textureRegion) {
        image.setDrawable(new TextureRegionDrawable(textureRegion));
        image.setScaling(Scaling.fit); // Default is Scaling.stretch.
        image.setVisible(true);
        messageLabel.setVisible(false);
        centerTable.setVisible(true);
    }

    public void showImage(TextureRegion textureRegion, float seconds) {
        overlayTime = 0;
        overlaySeconds = seconds;
        overlayTemporaryScreen = true;
        showImage(textureRegion);
    }

    public void showModalImage(TextureRegion textureRegion) {
        pauseLabel.setVisible(false);
        resumeLabel.setVisible(false);
        gotItLabel.setVisible(true);
        quitLabel.setVisible(false);
        showImage(textureRegion);
        screen.setPlayScreenStatePaused(false);
    }

    public void hideModalImage() {
        pauseLabel.setVisible(true);
        resumeLabel.setVisible(false);
        gotItLabel.setVisible(false);
        quitLabel.setVisible(false);
        hideImage();
        screen.setPlayScreenStateRunning();
    }

    public void hideImage() {
        image.setVisible(false);
        centerTable.setVisible(false);
    }

    public boolean isImageVisible() {
        return image.isVisible();
    }

    public void showFpsInfo() {
        fpsTable.setVisible(true);
    }

    public void hideFpsInfo() {
        fpsTable.setVisible(false);
    }

    public boolean isFpsInfoVisible() {
        return fpsTable.isVisible();
    }

    public void showHealthBarInfo(String enemyName, int energy) {
        enemyNameLabel.setText(enemyName);
        healthBar.setInitialEnergy(energy);
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
        overlayTemporaryScreen(dt);
        overlayTemporaryMessage(dt);
    }

    private void updateWorldTime(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (time > 0) {
                time--;
                if (time <= LEVEL_TIMER_NOTIFICATION) {
                    AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClock());
                    AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBeepB());
                    if (isMessageVisible()) {
                        hideMessage();
                    } else {
                        showHurryUpMessage();
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
                    if (abilityPowerTime <= POWER_TIMER_NOTIFICATION) {
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBeepA());
                    }
                    abilityPowerTimeValueLabel.setText(String.format(Locale.getDefault(), FORMAT_POWER_TIME, abilityPowerTime));
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
                    if (weaponPowerTime <= POWER_TIMER_NOTIFICATION) {
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBeepC());
                    }
                    weaponPowerTimeValueLabel.setText(String.format(Locale.getDefault(), FORMAT_POWER_TIME, weaponPowerTime));
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

    private void overlayTemporaryScreen(float dt) {
        if (overlayTemporaryScreen) {
            overlayTime += dt;
            if (overlayTime >= overlaySeconds) {
                overlayTemporaryScreen = false;
                overlayTime = 0;
                hideImage();
            }
        }
    }

    private void overlayTemporaryMessage(float dt) {
        if (overlayTemporaryMessage) {
            overlayTime += dt;
            if (overlayTime >= overlaySeconds) {
                overlayTemporaryMessage = false;
                overlayTime = 0;
                hideMessage();
            }
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

    public void decreaseLives(int quantity) {
        lives -= quantity;
        livesValueLabel.setText(String.format(Locale.getDefault(), FORMAT_LIVES, lives));
    }

    public void increaseSilverBullets(int quantity) {
        silverBullets += quantity;
        silverBulletValueLablel.setText(String.format(Locale.getDefault(), FORMAT_SILVER_BULLETS, silverBullets));
    }

    public void decreaseSilverBullets(int quantity) {
        silverBullets -= quantity;
        silverBulletValueLablel.setText(String.format(Locale.getDefault(), FORMAT_SILVER_BULLETS, silverBullets));
    }

    // Show help screens depending on the object's class name
    public void showDynamicHelp(String className, TextureRegion helpImage) {
        if (dynamicHelp.containsKey(className)){
            DynamicHelpDef dynamicHelpDef = dynamicHelp.get(className);
            if (dynamicHelpDef.isModal()) {
                screen.getHud().showModalImage(helpImage);
            } else {
                screen.getHud().showImage(helpImage, dynamicHelpDef.getSeconds());
            }
            dynamicHelp.remove(className);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void draw() {
        act();
        // Set our batch to now draw what the Hud camera sees.
        super.draw();
    }
}
